package com.paidtocode.stargang.api.response.factory;


import com.paidtocode.stargang.api.response.MobitelAncestor;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by vihanga on 3/20/17.
 */

public interface MobitelAncestorsFactory {
	MobitelAncestor parse(JSONObject response) throws IOException;
}
