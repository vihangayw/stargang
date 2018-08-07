package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vihanga on 3/20/17.
 */

public class StringResponse extends Ancestor<String> {
	StringResponse(@JsonProperty("message") String message,
	               @JsonProperty("data") String data,
	               @JsonProperty("status") boolean code) {
		super(message, code, data);
	}
}
