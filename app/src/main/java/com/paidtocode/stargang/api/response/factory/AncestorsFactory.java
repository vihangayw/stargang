package com.paidtocode.stargang.api.response.factory;


import com.paidtocode.stargang.api.response.Ancestor;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by vihanga on 3/20/17.
 */

public interface AncestorsFactory {
	Ancestor parse(JSONObject response) throws IOException;
}
