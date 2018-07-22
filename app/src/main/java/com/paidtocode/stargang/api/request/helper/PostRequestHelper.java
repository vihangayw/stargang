package com.paidtocode.stargang.api.request.helper;


import com.paidtocode.stargang.api.APIHelper;

/**
 * Created by Vihanga on 3/8/2018.
 */

public interface PostRequestHelper {

	void getMyPost(int page, int limit, APIHelper.PostManResponseListener listener);

}
