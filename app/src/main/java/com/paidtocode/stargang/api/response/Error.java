package com.paidtocode.stargang.api.response;

import com.android.volley.VolleyError;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Vihanga on 27/2/18.
 */

public class Error extends Ancestor<String> {


	public Error(@JsonProperty("message") String message,
	             @JsonProperty("data") String data,
	             @JsonProperty("status") boolean status) {
		super(message, status, data);
	}

	public Error(VolleyError ignored) {
		super(String.valueOf(ignored.networkResponse.statusCode),
				false,
				new String(ignored.networkResponse.data));
	}

	public Error(Throwable ignored) {
		super(ignored.getLocalizedMessage(), false, ignored.toString());
	}

}
