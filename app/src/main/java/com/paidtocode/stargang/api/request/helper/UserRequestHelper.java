package com.paidtocode.stargang.api.request.helper;


import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.modal.Register;
import com.paidtocode.stargang.modal.User;

/**
 * Created by Vihanga on 3/8/2018.
 */

public interface UserRequestHelper {

	void registerUser(Register register, APIHelper.PostManResponseListener listener);

	void loginUser(String email, String pw, APIHelper.PostManResponseListener listener);

	void logout(APIHelper.PostManResponseListener listener);

	void changePW(String oldPW, String newPW, APIHelper.PostManResponseListener listener);

	void getUserList(int page, int limit, APIHelper.PostManResponseListener listener);

	void getUserProfile(APIHelper.PostManResponseListener listener);

	void doSubscribe(User user, APIHelper.PostManResponseListener listener);

	void doUnsubscribe(User user, APIHelper.PostManResponseListener listener);

	void editProfile(String bd, String name, String info, APIHelper.PostManResponseListener listener);

	void getUserList(String trim, int page, int limit, APIHelper.PostManResponseListener postManResponseListener);
}
