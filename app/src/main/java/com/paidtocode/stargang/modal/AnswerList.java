package com.paidtocode.stargang.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerList implements Serializable {

	private List<Answer> qa;

	public AnswerList() {
	}

	public AnswerList(List<Answer> qa) {
		this.qa = qa;
	}

	public List<Answer> getQa() {
		return qa;
	}

	public void setQa(List<Answer> qa) {
		this.qa = qa;
	}
}
