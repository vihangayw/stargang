package com.paidtocode.stargang.api.request.helper.impl;

import com.android.volley.Request;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.APIURLHelper;
import com.paidtocode.stargang.api.request.helper.PostRequestHelper;
import com.paidtocode.stargang.api.response.factory.impl.AncestorCommentListResponseFactory;
import com.paidtocode.stargang.api.response.factory.impl.AncestorCommentResponseFactory;
import com.paidtocode.stargang.api.response.factory.impl.AncestorLikeResponseFactory;
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
	public void getPost(String uid, int page, int limit, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("page", String.valueOf(page));
		paramMap.put("orderBy", "descending");
		paramMap.put("post", String.valueOf(limit));
		paramMap.put("uid", uid);
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

	@Override
	public void getAllComments(int page, int limit, String postID, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("page", String.valueOf(page));
		paramMap.put("comments", String.valueOf(limit));
		paramMap.put("postId", postID);
		paramMap.put("userId", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorCommentListResponseFactory(),
				Request.Method.POST, APIURLHelper.getViewCommentURL(), paramMap);
	}

	@Override
	public void addComment(String comment, String postID, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("comment", comment);
		paramMap.put("postId", postID);
		paramMap.put("userId", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorCommentResponseFactory(),
				Request.Method.POST, APIURLHelper.getAddCommentURL(), paramMap);
	}

	@Override
	public void likeUnlike(String postID, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("postId", postID);
		paramMap.put("userId", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorLikeResponseFactory(),
				Request.Method.POST, APIURLHelper.getLikePostsURL(), paramMap);
	}
}
