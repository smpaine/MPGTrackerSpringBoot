package com.nameniap.mpgtracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "passkey_challenges")
public class PasskeyChallenge extends BaseEntity {

    @Lob
    @Column(name = "request_json", nullable = false)
    private String requestJson;

    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDt;

    public String getRequestJson() { return requestJson; }
    public void setRequestJson(String requestJson) { this.requestJson = requestJson; }

    public Date getCreatedDt() { return createdDt; }
    public void setCreatedDt(Date createdDt) { this.createdDt = createdDt; }
}
