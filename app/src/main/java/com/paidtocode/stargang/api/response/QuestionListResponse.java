package com.paidtocode.stargang.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paidtocode.stargang.modal.Question;

import java.util.List;

/**
 * Created by vihanga on 3/20/17.
 */

public class QuestionListResponse extends Ancestor<List<Question>> {
	QuestionListResponse(@JsonProperty("message") String message,
	                     @JsonProperty("data") List<Question> data,
	                     @JsonProperty("status") boolean code) {
		super(message, code, data);
	}
}
