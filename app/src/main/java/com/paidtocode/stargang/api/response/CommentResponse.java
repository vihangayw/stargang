package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paidtocode.stargang.modal.Comment;

/**
 * Created by vihanga on 3/20/17.
 */

public class CommentResponse extends Ancestor<Comment> {
	CommentResponse(@JsonProperty("message") String message,
	                @JsonProperty("data") Comment data,
	                @JsonProperty("status") boolean code) {
		super(message, code, data);
	}
}
