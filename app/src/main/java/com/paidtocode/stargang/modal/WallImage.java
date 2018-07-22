package com.paidtocode.stargang.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by vihanga on 7/21/18 in stargang.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WallImage implements Serializable {

	private String image;

	public WallImage() {
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
