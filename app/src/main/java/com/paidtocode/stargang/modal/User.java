package com.paidtocode.stargang.modal;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vihanga on 7/21/18 in stargang.
 */
public class User implements Serializable {

	private String id;
	private String email;
	private String fullName;
	private String image;
	private String info;
	private boolean subscribe;
	private List<UserType> type;

	public User() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public boolean isSubscribe() {
		return subscribe;
	}

	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}

	public List<UserType> getType() {
		return type;
	}

	public void setType(List<UserType> type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User)) return false;
		User user = (User) o;
		return TextUtils.equals(getId(), user.getId());
	}

}
