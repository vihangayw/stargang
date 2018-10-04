package com.paidtocode.stargang.api.request.helper;


import com.paidtocode.stargang.api.APIHelper;

import org.json.JSONException;

/**
 * Created by Vihanga on 3/8/2018.
 */

public interface MobitelRequestHelper {

	void subscribe(String no, APIHelper.PostManMobitelResponseListener postManMobitelResponseListener) throws JSONException;

	void unsubscribe(String no, APIHelper.PostManMobitelResponseListener postManMobitelResponseListener) throws JSONException;
}
