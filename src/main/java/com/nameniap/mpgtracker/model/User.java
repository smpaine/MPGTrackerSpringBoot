package com.nameniap.mpgtracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6229822343488826704L;

	@Column(name = "username")
    @NotEmpty
    private String userName;

    @Column(name = "password")
    @NotEmpty
    private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
