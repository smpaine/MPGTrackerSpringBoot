package com.nameniap.mpgtracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "passkey_credentials")
public class PasskeyCredential extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "credential_id", nullable = false, unique = true, length = 512)
    private String credentialId;

    @Lob
    @Column(name = "public_key_cose", nullable = false)
    private byte[] publicKeyCose;

    @Column(name = "sign_count", nullable = false)
    private long signCount;

    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDt;

    @Column(name = "origin", length = 255)
    private String origin;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getCredentialId() { return credentialId; }
    public void setCredentialId(String credentialId) { this.credentialId = credentialId; }

    public byte[] getPublicKeyCose() { return publicKeyCose; }
    public void setPublicKeyCose(byte[] publicKeyCose) { this.publicKeyCose = publicKeyCose; }

    public long getSignCount() { return signCount; }
    public void setSignCount(long signCount) { this.signCount = signCount; }

    public Date getCreatedDt() { return createdDt; }
    public void setCreatedDt(Date createdDt) { this.createdDt = createdDt; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
}
