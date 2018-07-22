package com.paidtocode.stargang.api.request.helper.impl;

import com.android.volley.Request;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.APIURLHelper;
import com.paidtocode.stargang.api.request.helper.UserRequestHelper;
import com.paidtocode.stargang.api.response.factory.impl.AncestorObecjtResponseFactory;
import com.paidtocode.stargang.api.response.factory.impl.AncestorSignupListResponseFactory;
import com.paidtocode.stargang.api.response.factory.impl.AncestorSignupResponseFactory;
import com.paidtocode.stargang.api.response.factory.impl.AncestorUserListResponseFactory;
import com.paidtocode.stargang.modal.Register;
import com.paidtocode.stargang.modal.Signup;
import com.paidtocode.stargang.modal.User;
import com.paidtocode.stargang.util.UserSessionManager;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Vihanga on 3/15/2018.
 */

public class UserRequestHelperImpl implements UserRequestHelper {

	@Override
	public void registerUser(Register register, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("email", register.getEmail());
		paramMap.put("password", register.getPassword());
		paramMap.put("repassword", register.getRepassword());
		paramMap.put("userType", register.getUserType());
		paramMap.put("uMobile", register.getuMobile());
		APIHelper.getInstance().sendStringRequestsWithParams(listener, new AncestorSignupListResponseFactory(),
				Request.Method.POST, APIURLHelper.getRegisterURL(), paramMap);
	}

	@Override
	public void loginUser(String email, String pw, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("email", email);
		paramMap.put("password", pw);
		APIHelper.getInstance().sendStringRequestsWithParams(listener, new AncestorSignupResponseFactory(),
				Request.Method.POST, APIURLHelper.getLogin(), paramMap);
	}

	@Override
	public void logout(APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("userID", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorObecjtResponseFactory(),
				Request.Method.POST, APIURLHelper.getLogoutURL(), paramMap);
	}

	@Override
	public void changePW(String oldPW, String newPW, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("newPassword", newPW);
		paramMap.put("oldPassword", oldPW);
		paramMap.put("userID", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorObecjtResponseFactory(),
				Request.Method.POST, APIURLHelper.getPasswordResetURL(), paramMap);
	}

	@Override
	public void getUserList(int page, int limit, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("users", String.valueOf(limit));
		paramMap.put("page", String.valueOf(page));
		paramMap.put("userId", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorUserListResponseFactory(),
				Request.Method.POST, APIURLHelper.getAllUsers(), paramMap);
	}

	@Override
	public void getUserProfile(APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorSignupResponseFactory(),
				Request.Method.POST, APIURLHelper.getUserProfileURL(), paramMap);
	}

	@Override
	public void doSubscribe(User user, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("receiveID", user.getId());
		paramMap.put("sendID", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorUserListResponseFactory(),
				Request.Method.POST, APIURLHelper.subscribeURL(), paramMap);
	}

	@Override
	public void doUnsubscribe(User user, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("receiveID", user.getId());
		paramMap.put("sendID", UserSessionManager.getInstance().getUser().getId());
		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorUserListResponseFactory(),
				Request.Method.POST, APIURLHelper.unsubscribeURL(), paramMap);
	}

	@Override
	public void editProfile(String name, String info, APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
		Signup user = UserSessionManager.getInstance().getUser();
		paramMap.put("name", name);
		paramMap.put("info", info);
		paramMap.put("birthDay", "2001-01-01");
		paramMap.put("email", user.getEmail());
		paramMap.put("uid", user.getId());

		APIHelper.getInstance().sendMultipartRequestProfile(listener, new AncestorSignupResponseFactory(),
				Request.Method.POST, APIURLHelper.editProfileURL(), paramMap);
	}


}
