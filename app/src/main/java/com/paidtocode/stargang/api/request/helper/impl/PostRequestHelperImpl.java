package com.paidtocode.stargang.api.request.helper.impl;

import com.android.volley.Request;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.APIURLHelper;
import com.paidtocode.stargang.api.request.helper.PostRequestHelper;
import com.paidtocode.stargang.api.response.factory.impl.AncestorPostListResponseFactory;
import com.paidtocode.stargang.api.response.factory.impl.AncestorPostResponseFactory;
import com.paidtocode.stargang.api.response.factory.impl.AncestorWallListResponseFactory;
import com.paidtocode.stargang.util.UserSessionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vihanga on 7/22/18 in stargang.
 */
public class PostRequestHelperImpl implements PostRequestHelper {

	@Override
	public void getMyPost(int page, int limit, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("page", String.valueOf(page));
		paramMap.put("orderBy", "descending");
		paramMap.put("post", String.valueOf(limit));
		paramMap.put("uid", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorPostListResponseFactory(),
				Request.Method.POST, APIURLHelper.getOldPostURL(), paramMap);
	}

	@Override
	public void addPost(String caption, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("ptext", caption.trim());
		paramMap.put("uid", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendMultipartRequestPosts(listener, new AncestorPostResponseFactory(),
				Request.Method.POST, APIURLHelper.addPostURL(), paramMap);
	}

	@Override
	public void getWallPosts(int page, int limit, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("page", String.valueOf(page));
		paramMap.put("post", String.valueOf(limit));
		paramMap.put("userId", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorWallListResponseFactory(),
				Request.Method.POST, APIURLHelper.getWallPostsURL(), paramMap);
	}
}
