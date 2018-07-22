package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paidtocode.stargang.modal.Post;

import java.util.List;

/**
 * Created by vihanga on 3/20/17.
 */

public class PostListResponse extends Ancestor<List<Post>> {
	PostListResponse(@JsonProperty("message") String message,
	                 @JsonProperty("data") List<Post> data,
	                 @JsonProperty("status") boolean code) {
		super(message, code, data);
	}
}
