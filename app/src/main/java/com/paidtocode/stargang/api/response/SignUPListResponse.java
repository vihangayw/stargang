package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paidtocode.stargang.modal.Signup;

import java.util.List;

/**
 * Created by vihanga on 3/20/17.
 */

public class SignUPListResponse extends Ancestor<List<Signup>> {
	SignUPListResponse(@JsonProperty("message") String message,
	                   @JsonProperty("data") List<Signup> data,
	                   @JsonProperty("status") boolean code) {
		super(message, code, data);
	}
}
