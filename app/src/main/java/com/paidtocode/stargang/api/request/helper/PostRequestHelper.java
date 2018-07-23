package com.paidtocode.stargang.api.request.helper;


import com.paidtocode.stargang.api.APIHelper;

/**
 * Created by Vihanga on 3/8/2018.
 */

public interface PostRequestHelper {

	void getMyPost(int page, int limit, APIHelper.PostManResponseListener listener);

	void addPost(String caption, APIHelper.PostManResponseListener listener);

	void getWallPosts(int page, int limit, APIHelper.PostManResponseListener listener);

	void getAllComments(int page, int limit, String postID, APIHelper.PostManResponseListener listener);

	void addComment(String comment, String postID, APIHelper.PostManResponseListener listener);

	void likeUnlike(String postID, APIHelper.PostManResponseListener listener);
}
