package com.nameniap.mpgtracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nameniap.mpgtracker.model.JwtResponse;
import com.nameniap.mpgtracker.model.MessageResponse;
import com.nameniap.mpgtracker.service.PasskeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/passkey")
public class PasskeyController {

    private static final Logger logger = LoggerFactory.getLogger(PasskeyController.class);

    @Autowired
    private PasskeyService passkeyService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Returns PublicKeyCredentialCreationOptions for the authenticated user.
     * Requires a valid JWT (user must already be logged in to register a passkey).
     */
    @GetMapping("/register/options")
    public ResponseEntity<?> startRegistration(Authentication authentication) {
        try {
            Map<String, Object> options = passkeyService.startRegistration(authentication.getName());
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            logger.error("Error starting passkey registration", e);
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Verifies the attestation response and stores the new passkey credential.
     * Requires a valid JWT.
     */
    @PostMapping("/register/verify")
    public ResponseEntity<?> finishRegistration(@RequestBody PasskeyVerifyRequest request,
                                                 HttpServletRequest httpRequest) {
        try {
            String credentialJson = objectMapper.writeValueAsString(request.getCredential());
            String userAgent = httpRequest.getHeader("User-Agent");
            passkeyService.finishRegistration(request.getChallengeId(), credentialJson, userAgent);
            return ResponseEntity.ok(new MessageResponse("Passkey registered successfully"));
        } catch (Exception e) {
            logger.error("Error finishing passkey registration", e);
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Returns PublicKeyCredentialRequestOptions for the given username.
     * Public endpoint — no JWT required.
     */
    @GetMapping("/auth/options")
    public ResponseEntity<?> startAuthentication(
            @RequestParam(required = false) String userName) {
        try {
            Map<String, Object> options = passkeyService.startAuthentication(userName);
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            logger.error("Error starting passkey authentication for user {}", userName, e);
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Lists registered passkeys for a user. Requires a valid JWT.
     */
    @GetMapping("/credentials/{userId}")
    public ResponseEntity<?> listCredentials(@PathVariable Integer userId) {
        return ResponseEntity.ok(passkeyService.listCredentialsForUser(userId));
    }

    /**
     * Deletes a passkey by its DB id. Requires a valid JWT.
     */
    @DeleteMapping("/credentials/{id}")
    public ResponseEntity<?> deleteCredential(@PathVariable Integer id) {
        try {
            passkeyService.deleteCredential(id);
            return ResponseEntity.ok(new MessageResponse("Passkey deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting passkey {}", id, e);
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Verifies the assertion response and returns a JWT on success.
     * Public endpoint — no JWT required.
     */
    @PostMapping("/auth/verify")
    public ResponseEntity<?> finishAuthentication(@RequestBody PasskeyVerifyRequest request) {
        try {
            String credentialJson = objectMapper.writeValueAsString(request.getCredential());
            JwtResponse jwtResponse = passkeyService.finishAuthentication(
                    request.getChallengeId(), credentialJson);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            logger.error("Passkey authentication failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Authentication failed"));
        }
    }
}
