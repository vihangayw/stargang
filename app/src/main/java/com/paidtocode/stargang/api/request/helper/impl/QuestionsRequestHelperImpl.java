package com.paidtocode.stargang.api.request.helper.impl;

import com.android.volley.Request;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.APIURLHelper;
import com.paidtocode.stargang.api.request.helper.QuestionsRequestHelper;
import com.paidtocode.stargang.api.response.factory.impl.AncestorQuestoinsListResponseFactory;
import com.paidtocode.stargang.util.UserSessionManager;

import java.util.HashMap;
import java.util.Map;

public class QuestionsRequestHelperImpl implements QuestionsRequestHelper {

	@Override
	public void questionsList(APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("userId", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener,
				new AncestorQuestoinsListResponseFactory(),
				Request.Method.POST,
				APIURLHelper.getQuestoinstURL(),
				paramMap);
	}

}
