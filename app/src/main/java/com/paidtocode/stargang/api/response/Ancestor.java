package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vihanga on 27/2/18.
 */

public class Ancestor<T> {

	private String message;
	private boolean status;
	private T data;

	@JsonCreator
	protected Ancestor(@JsonProperty("message") String message,
	                   @JsonProperty("status") boolean status,
	                   @JsonProperty("data") T data) {
		this.message = message;
		this.data = data;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}

	public boolean getStatus() {
		return status;
	}
}