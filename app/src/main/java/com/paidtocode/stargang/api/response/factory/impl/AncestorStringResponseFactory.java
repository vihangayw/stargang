package com.paidtocode.stargang.api.response.factory.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.paidtocode.stargang.api.response.Ancestor;
import com.paidtocode.stargang.api.response.StringResponse;
import com.paidtocode.stargang.api.response.factory.AncestorsFactory;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by vihanga on 3/20/17.
 */

public class AncestorStringResponseFactory implements AncestorsFactory {
	@Override
	public Ancestor parse(JSONObject response) throws IOException {
		return new ObjectMapper().readValue(response.toString(), StringResponse.class);
	}

}