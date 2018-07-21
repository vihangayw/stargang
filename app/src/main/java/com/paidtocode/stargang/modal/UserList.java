package com.paidtocode.stargang.modal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vihanga on 7/21/18 in stargang.
 */
public class UserList implements Serializable {

	private List<User> users;

	public UserList() {
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
