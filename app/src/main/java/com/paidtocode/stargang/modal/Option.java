package com.paidtocode.stargang.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Option implements Serializable {

	private String qaID;
	private String answers;
	private String correct;

	public Option() {
	}

	public Option(String qaID, String answers, String correct) {
		this.qaID = qaID;
		this.answers = answers;
		this.correct = correct;
	}

	public String getQaID() {
		return qaID;
	}

	public void setQaID(String qaID) {
		this.qaID = qaID;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public String getCorrect() {
		return correct;
	}

	public void setCorrect(String correct) {
		this.correct = correct;
	}
}
