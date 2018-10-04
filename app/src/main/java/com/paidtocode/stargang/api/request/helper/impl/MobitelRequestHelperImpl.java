package com.paidtocode.stargang.api.request.helper.impl;

import com.android.volley.Request;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.request.helper.MobitelRequestHelper;
import com.paidtocode.stargang.api.response.factory.impl.AncestorSubscribeResponseFactory;
import com.paidtocode.stargang.modal.Subscribe;
import com.paidtocode.stargang.util.JsonService;

import org.json.JSONException;

public class MobitelRequestHelperImpl implements MobitelRequestHelper {
	@Override
	public void subscribe(String no, APIHelper.PostManMobitelResponseListener lis) throws JSONException {
		if (no.startsWith("94")) no = no.replaceFirst("94", "0");
		if (no.startsWith("+94")) no = no.replaceFirst("\\+94", "0");
		APIHelper.getInstance().sendAuthJsonWithParams(lis, new AncestorSubscribeResponseFactory(),
				Request.Method.POST, "https://apphub.mobitel.lk/mobext/mapi/subscription/addSubscription",
				JsonService.toJsonNode(new Subscribe(322, no)));
	}

	@Override
	public void unsubscribe(String no, APIHelper.PostManMobitelResponseListener postManMobitelResponseListener) throws JSONException {

		if (no.startsWith("94")) no = no.replaceFirst("94", "0");
		if (no.startsWith("+94")) no = no.replaceFirst("\\+94", "0");
		APIHelper.getInstance().sendAuthJsonWithParams(postManMobitelResponseListener, new AncestorSubscribeResponseFactory(),
				Request.Method.POST, "https://apphub.mobitel.lk/mobext/mapi/subscription/unSubscribe",
				JsonService.toJsonNode(new Subscribe(322, no)));
	}
}
