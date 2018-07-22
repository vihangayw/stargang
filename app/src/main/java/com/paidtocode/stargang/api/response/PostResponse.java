package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paidtocode.stargang.modal.Post;

/**
 * Created by vihanga on 3/20/17.
 */

public class PostResponse extends Ancestor<Post> {
	PostResponse(@JsonProperty("message") String message,
	             @JsonProperty("data") Post data,
	             @JsonProperty("status") boolean code) {
		super(message, code, data);
	}
}
