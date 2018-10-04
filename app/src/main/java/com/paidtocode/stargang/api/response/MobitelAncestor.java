package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vihanga on 27/2/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MobitelAncestor<T> {

	private String message;
	private int status;
	private T data;

	@JsonCreator
	protected MobitelAncestor(@JsonProperty("message") String message,
	                          @JsonProperty("status") int status,
	                          @JsonProperty("reference") T data) {
		this.message = message;
		this.data = data;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}