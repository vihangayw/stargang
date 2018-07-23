package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paidtocode.stargang.modal.Comment;

import java.util.List;

/**
 * Created by vihanga on 3/20/17.
 */

public class CommentListResponse extends Ancestor<List<Comment>> {
	CommentListResponse(@JsonProperty("message") String message,
	                    @JsonProperty("data") List<Comment> data,
	                    @JsonProperty("status") boolean code) {
		super(message, code, data);
	}
}
