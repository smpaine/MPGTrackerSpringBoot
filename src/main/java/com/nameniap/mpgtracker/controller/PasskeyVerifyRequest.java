package com.nameniap.mpgtracker.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PasskeyVerifyRequest {

    private Integer challengeId;
    private JsonNode credential;

    public Integer getChallengeId() { return challengeId; }
    public void setChallengeId(Integer challengeId) { this.challengeId = challengeId; }

    public JsonNode getCredential() { return credential; }
    public void setCredential(JsonNode credential) { this.credential = credential; }
}
