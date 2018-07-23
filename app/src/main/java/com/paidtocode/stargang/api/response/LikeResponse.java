package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paidtocode.stargang.modal.Like;

/**
 * Created by vihanga on 3/20/17.
 */

public class LikeResponse extends Ancestor<Like> {
	LikeResponse(@JsonProperty("message") String message,
	             @JsonProperty("data") Like data,
	             @JsonProperty("status") boolean code) {
		super(message, code, data);
	}
}
