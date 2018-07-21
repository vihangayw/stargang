package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paidtocode.stargang.modal.UserList;

/**
 * Created by vihanga on 3/20/17.
 */

public class UserListResponse extends Ancestor<UserList> {
	UserListResponse(@JsonProperty("message") String message,
	                 @JsonProperty("data") UserList data,
	                 @JsonProperty("status") boolean code) {
		super(message, code, data);
	}
}
