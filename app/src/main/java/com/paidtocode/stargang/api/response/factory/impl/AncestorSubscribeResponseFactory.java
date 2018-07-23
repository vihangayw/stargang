package com.paidtocode.stargang.api.response.factory.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.paidtocode.stargang.api.response.MobitelAncestor;
import com.paidtocode.stargang.api.response.SubscriberResponse;
import com.paidtocode.stargang.api.response.factory.MobitelAncestorsFactory;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by vihanga on 3/20/17.
 */

public class AncestorSubscribeResponseFactory implements MobitelAncestorsFactory {
	@Override
	public MobitelAncestor parse(JSONObject response) throws IOException {
		return new ObjectMapper().readValue(response.toString(), SubscriberResponse.class);
	}

}
