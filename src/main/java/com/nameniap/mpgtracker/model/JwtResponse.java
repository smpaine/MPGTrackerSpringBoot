package com.nameniap.mpgtracker.model;

import java.io.Serializable;
import java.util.Date;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String token;
	private final int id;
	private final String userName;
	private final String userType;
	private final Date lastLoginDt;
	

	public JwtResponse(String token, User user) {
		this.token = token;
		this.id = user.getId();
		this.userName = user.getUserName();
		this.lastLoginDt = user.getLastLoginDt();
		this.userType = user.getUserType();
	}

	public String getToken() {
		return this.token;
	}
	
	public int getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public Date getLastLoginDt() {
		return lastLoginDt;
	}

	public String getUserType() {
		return userType;
	}
	
}
