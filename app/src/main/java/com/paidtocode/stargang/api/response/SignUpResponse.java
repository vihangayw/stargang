package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paidtocode.stargang.modal.Signup;

/**
 * Created by vihanga on 3/20/17.
 */

public class SignUpResponse extends Ancestor<Signup> {
	SignUpResponse(@JsonProperty("message") String message,
	               @JsonProperty("data") Signup data,
	               @JsonProperty("status") boolean code) {
		super(message, code, data);
	}
}
