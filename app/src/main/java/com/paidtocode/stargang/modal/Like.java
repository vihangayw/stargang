package com.paidtocode.stargang.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by vihanga on 7/21/18 in stargang.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Like implements Serializable {

	private String like;

	public Like() {
	}

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		this.like = like;
	}
}
