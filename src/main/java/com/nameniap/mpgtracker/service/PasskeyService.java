package com.nameniap.mpgtracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nameniap.mpgtracker.config.JwtTokenUtil;
import com.nameniap.mpgtracker.model.JwtResponse;
import com.nameniap.mpgtracker.model.PasskeyCredentialInfo;
import com.nameniap.mpgtracker.model.PasskeyChallenge;
import com.nameniap.mpgtracker.model.PasskeyCredential;
import com.nameniap.mpgtracker.model.User;
import com.nameniap.mpgtracker.repository.PasskeyChallengeRepository;
import com.nameniap.mpgtracker.repository.PasskeyCredentialRepository;
import com.nameniap.mpgtracker.repository.UserRepository;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.AssertionResult;
import com.yubico.webauthn.CredentialRepository;
import com.yubico.webauthn.FinishAssertionOptions;
import com.yubico.webauthn.FinishRegistrationOptions;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.StartAssertionOptions;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import com.yubico.webauthn.data.UserIdentity;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PasskeyService implements CredentialRepository {

    private static final Logger logger = LoggerFactory.getLogger(PasskeyService.class);

    @Autowired
    private PasskeyCredentialRepository credentialRepo;

    @Autowired
    private PasskeyChallengeRepository challengeRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${passkey.rp-id}")
    private String rpId;

    @Value("${passkey.rp-origins}")
    private String[] rpOrigins;

    private RelyingParty relyingParty;

    // ObjectMapper with Jdk8Module for Optional support.
    // ByteArray carries its own @JsonSerialize/@JsonDeserialize annotations so
    // no additional WebAuthn-specific modules are needed.
    private final ObjectMapper webauthnMapper = new ObjectMapper().registerModule(new Jdk8Module());

    // Standard Jackson mapper for converting JsonNode responses
    private final ObjectMapper standardMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        RelyingPartyIdentity rpIdentity = RelyingPartyIdentity.builder()
                .id(rpId)
                .name("MPGTracker")
                .build();

        relyingParty = RelyingParty.builder()
                .identity(rpIdentity)
                .credentialRepository(this)
                .origins(new HashSet<>(Arrays.asList(rpOrigins)))
                .build();

        logger.info("PasskeyService initialized: rpId={} rpOrigins={}", rpId, Arrays.toString(rpOrigins));
    }

    // -------------------------------------------------------------------------
    // CredentialRepository implementation
    // -------------------------------------------------------------------------

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        User user = userRepo.findByUserName(username);
        if (user == null) return Collections.emptySet();
        return credentialRepo.findByUserId(user.getId()).stream()
                .map(c -> {
                    try {
                        return PublicKeyCredentialDescriptor.builder()
                                .id(ByteArray.fromBase64Url(c.getCredentialId()))
                                .build();
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid credential ID in database", e);
                    }
                })
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        User user = userRepo.findByUserName(username);
        if (user == null) return Optional.empty();
        return Optional.of(toUserHandle(user.getId()));
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        Integer userId = fromUserHandle(userHandle);
        return userRepo.findById(userId).map(User::getUserName);
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        return credentialRepo.findByCredentialId(credentialId.getBase64Url())
                .map(c -> {
                    try {
                        return RegisteredCredential.builder()
                                .credentialId(ByteArray.fromBase64Url(c.getCredentialId()))
                                .userHandle(userHandle)
                                .publicKeyCose(new ByteArray(c.getPublicKeyCose()))
                                .signatureCount(c.getSignCount())
                                .build();
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid credential ID in database", e);
                    }
                });
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        return credentialRepo.findByCredentialId(credentialId.getBase64Url())
                .map(c -> {
                    try {
                        return RegisteredCredential.builder()
                                .credentialId(ByteArray.fromBase64Url(c.getCredentialId()))
                                .userHandle(toUserHandle(c.getUserId()))
                                .publicKeyCose(new ByteArray(c.getPublicKeyCose()))
                                .signatureCount(c.getSignCount())
                                .build();
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid credential ID in database", e);
                    }
                })
                .map(Collections::singleton)
                .orElse(Collections.emptySet());
    }

    // -------------------------------------------------------------------------
    // Registration
    // -------------------------------------------------------------------------

    public Map<String, Object> startRegistration(String username) throws IOException {
        User user = userRepo.findByUserName(username);
        if (user == null) throw new IllegalArgumentException("User not found: " + username);

        PublicKeyCredentialCreationOptions creationOptions = relyingParty.startRegistration(
                StartRegistrationOptions.builder()
                        .user(UserIdentity.builder()
                                .name(username)
                                .displayName(username)
                                .id(toUserHandle(user.getId()))
                                .build())
                        .build()
        );

        PasskeyChallenge challenge = new PasskeyChallenge();
        challenge.setRequestJson(webauthnMapper.writeValueAsString(creationOptions));
        challenge.setCreatedDt(new Date());
        challenge = challengeRepo.save(challenge);

        Map<String, Object> response = new HashMap<>();
        response.put("challengeId", challenge.getId());
        // Parse through standard mapper so Spring MVC can serialize it without WebAuthn modules
        response.put("publicKey", standardMapper.readTree(
                webauthnMapper.writeValueAsString(creationOptions)));
        return response;
    }

    public void finishRegistration(Integer challengeId, String credentialJson, String userAgent)
            throws IOException, RegistrationFailedException {
        PasskeyChallenge challenge = challengeRepo.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

        PublicKeyCredentialCreationOptions creationOptions =
                webauthnMapper.readValue(challenge.getRequestJson(), PublicKeyCredentialCreationOptions.class);

        PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> pkc =
                PublicKeyCredential.parseRegistrationResponseJson(credentialJson);

        RegistrationResult result = relyingParty.finishRegistration(
                FinishRegistrationOptions.builder()
                        .request(creationOptions)
                        .response(pkc)
                        .build()
        );

        String username = creationOptions.getUser().getName();
        User user = userRepo.findByUserName(username);

        // Extract origin from clientDataJSON
        String origin = null;
        try {
            byte[] clientDataBytes = pkc.getResponse().getClientDataJSON().getBytes();
            com.fasterxml.jackson.databind.JsonNode clientData = standardMapper.readTree(clientDataBytes);
            origin = clientData.path("origin").asText(null);
        } catch (Exception e) {
            logger.warn("Could not extract origin from clientDataJSON", e);
        }

        PasskeyCredential credential = new PasskeyCredential();
        credential.setUserId(user.getId());
        credential.setCredentialId(result.getKeyId().getId().getBase64Url());
        credential.setPublicKeyCose(result.getPublicKeyCose().getBytes());
        credential.setSignCount(result.getSignatureCount());
        credential.setCreatedDt(new Date());
        credential.setOrigin(origin);
        credential.setUserAgent(userAgent);
        credentialRepo.save(credential);

        challengeRepo.deleteById(challengeId);
        logger.info("Passkey registered for user {}", username);
    }

    // -------------------------------------------------------------------------
    // Authentication
    // -------------------------------------------------------------------------

    public Map<String, Object> startAuthentication(String username) throws IOException {
        // username is optional — if omitted the browser discovers credentials on its own
        if (username != null && !username.isEmpty()) {
            User user = userRepo.findByUserName(username);
            if (user == null) throw new IllegalArgumentException("User not found: " + username);
        }

        AssertionRequest assertionRequest = relyingParty.startAssertion(
                StartAssertionOptions.builder()
                        .username(username != null && !username.isEmpty()
                                ? Optional.of(username)
                                : Optional.empty())
                        .build()
        );

        PasskeyChallenge challenge = new PasskeyChallenge();
        challenge.setRequestJson(webauthnMapper.writeValueAsString(assertionRequest));
        challenge.setCreatedDt(new Date());
        challenge = challengeRepo.save(challenge);

        Map<String, Object> response = new HashMap<>();
        response.put("challengeId", challenge.getId());
        response.put("publicKey", standardMapper.readTree(
                webauthnMapper.writeValueAsString(
                        assertionRequest.getPublicKeyCredentialRequestOptions())));
        return response;
    }

    public JwtResponse finishAuthentication(Integer challengeId, String credentialJson)
            throws IOException, AssertionFailedException {
        PasskeyChallenge challenge = challengeRepo.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

        AssertionRequest assertionRequest =
                webauthnMapper.readValue(challenge.getRequestJson(), AssertionRequest.class);

        PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> pkc =
                PublicKeyCredential.parseAssertionResponseJson(credentialJson);

        AssertionResult result = relyingParty.finishAssertion(
                FinishAssertionOptions.builder()
                        .request(assertionRequest)
                        .response(pkc)
                        .build()
        );

        // Update stored signature count (replay attack protection)
        credentialRepo.findByCredentialId(result.getCredential().getCredentialId().getBase64Url())
                .ifPresent(c -> {
                    c.setSignCount(result.getSignatureCount());
                    credentialRepo.save(c);
                });

        challengeRepo.deleteById(challengeId);

        User user = userRepo.findByUserName(result.getUsername());
        user.setLastLoginDt(Calendar.getInstance().getTime());
        userRepo.save(user);

        String token = jwtTokenUtil.generateToken(user);
        logger.info("Passkey authentication successful for user {}", result.getUsername());
        return new JwtResponse(token, user);
    }

    // -------------------------------------------------------------------------
    // Credential management
    // -------------------------------------------------------------------------

    public List<PasskeyCredentialInfo> listCredentialsForUser(Integer userId) {
        return credentialRepo.findByUserId(userId).stream()
                .map(c -> new PasskeyCredentialInfo(c.getId(), c.getCreatedDt(), c.getOrigin(), c.getUserAgent()))
                .collect(Collectors.toList());
    }

    public void deleteCredential(Integer id) {
        credentialRepo.deleteById(id);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private ByteArray toUserHandle(Integer userId) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(userId);
        return new ByteArray(buffer.array());
    }

    private Integer fromUserHandle(ByteArray handle) {
        return ByteBuffer.wrap(handle.getBytes()).getInt();
    }
}
