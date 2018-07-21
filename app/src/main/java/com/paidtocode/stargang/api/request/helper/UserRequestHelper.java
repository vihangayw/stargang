package com.paidtocode.stargang.api.request.helper;


import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.modal.Register;

/**
 * Created by Vihanga on 3/8/2018.
 */

public interface UserRequestHelper {

	void registerUser(Register register, APIHelper.PostManResponseListener listener);

	void loginUser(String email, String pw, APIHelper.PostManResponseListener listener);

	void getUserList(int page, int limit, APIHelper.PostManResponseListener listener);
}
