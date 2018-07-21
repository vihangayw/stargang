package com.paidtocode.stargang.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by vihanga on 7/21/18 in stargang.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserType implements Serializable {

	private String iduserType;
	private String userType;

	public UserType() {
	}

	public String getIduserType() {
		return iduserType;
	}

	public void setIduserType(String iduserType) {
		this.iduserType = iduserType;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
}
