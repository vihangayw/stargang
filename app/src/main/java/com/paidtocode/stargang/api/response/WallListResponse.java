package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paidtocode.stargang.modal.Wall;

import java.util.List;

/**
 * Created by vihanga on 3/20/17.
 */

public class WallListResponse extends Ancestor<List<Wall>> {
	WallListResponse(@JsonProperty("message") String message,
	                 @JsonProperty("data") List<Wall> data,
	                 @JsonProperty("status") boolean code) {
		super(message, code, data);
	}
}
