package com.nameniap.mpgtracker.model;

import java.util.Date;

public class PasskeyCredentialInfo {

    private final Integer id;
    private final Date createdDt;
    private final String origin;
    private final String userAgent;

    public PasskeyCredentialInfo(Integer id, Date createdDt, String origin, String userAgent) {
        this.id = id;
        this.createdDt = createdDt;
        this.origin = origin;
        this.userAgent = userAgent;
    }

    public Integer getId() { return id; }
    public Date getCreatedDt() { return createdDt; }
    public String getOrigin() { return origin; }
    public String getUserAgent() { return userAgent; }
}
