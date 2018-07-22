package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vihanga on 3/20/17.
 */

public class ObjectResponse extends Ancestor<Object> {
	ObjectResponse(@JsonProperty("message") String message,
	               @JsonProperty("data") Object data,
	               @JsonProperty("status") boolean code) {
		super(message, code, data);
	}
}
