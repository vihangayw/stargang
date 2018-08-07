package com.paidtocode.stargang.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Answer implements Serializable {

	private String qID;
	private String qaID;

	public Answer() {
	}

	public Answer(String qID, String qaID) {
		this.qID = qID;
		this.qaID = qaID;
	}

	public String getqID() {
		return qID;
	}

	public void setqID(String qID) {
		this.qID = qID;
	}

	public String getQaID() {
		return qaID;
	}

	public void setQaID(String qaID) {
		this.qaID = qaID;
	}
}
