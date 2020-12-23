package com.nameniap.mpgtracker.model;

import java.io.Serializable;
import java.util.Date;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String token;
	private final String userName;
	private final Date lastLoginDate;
	private final String userType;

	public JwtResponse(String token, User user) {
		this.token = token;
		this.userName = user.getUserName();
		this.lastLoginDate = user.getLastLoginDt();
		this.userType = user.getUserType();
	}

	public String getToken() {
		return this.token;
	}

	public String getUserName() {
		return userName;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public String getUserType() {
		return userType;
	}
	
}
