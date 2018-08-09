package com.paidtocode.stargang.api.request.helper.impl;

import com.android.volley.Request;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.APIURLHelper;
import com.paidtocode.stargang.api.request.helper.QuestionsRequestHelper;
import com.paidtocode.stargang.api.response.factory.impl.AncestorQuestoinsListResponseFactory;
import com.paidtocode.stargang.api.response.factory.impl.AncestorStringResponseFactory;
import com.paidtocode.stargang.modal.AnswerList;
import com.paidtocode.stargang.util.JsonService;
import com.paidtocode.stargang.util.UserSessionManager;

import org.json.JSONException;

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

	@Override
	public void answerQs(AnswerList answerList, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("userId", UserSessionManager.getInstance().getUser().getId());
		try {
			paramMap.put("data", JsonService.toJsonNode(answerList).toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener,
				new AncestorStringResponseFactory(),
				Request.Method.POST,
				APIURLHelper.getQuestoinsAnswerURL(),
				paramMap);
	}

}
