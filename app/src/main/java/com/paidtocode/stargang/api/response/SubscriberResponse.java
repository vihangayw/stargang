package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by vihanga on 3/20/17.
 */

public class SubscriberResponse extends MobitelAncestor<String> {
	SubscriberResponse(@JsonProperty("message") String message,
	                   @JsonProperty("data") String data,
	                   @JsonProperty("status") int code) {
		super(message, code, data);
	}
}
