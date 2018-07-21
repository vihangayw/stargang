package com.paidtocode.stargang.api.request.helper.impl;

import com.android.volley.Request;
import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.APIURLHelper;
import com.paidtocode.stargang.api.request.helper.UserRequestHelper;
import com.paidtocode.stargang.api.response.factory.impl.AncestorSignupListResponseFactory;
import com.paidtocode.stargang.api.response.factory.impl.AncestorSignupResponseFactory;
import com.paidtocode.stargang.modal.Register;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Vihanga on 3/15/2018.
 */

public class UserRequestHelperImpl implements UserRequestHelper {

//	@Override
//	public void vehicleOut(String vehicleType, String deviceID, String cardID, String locationID,
//	                       APIHelper.PostManResponseListener listener) {
//		Map<String, String> paramMap = new HashMap<>();
////		paramMap.put("device_id", deviceID);
////		paramMap.put("location", locationID);
////		paramMap.put("nfc_number", cardID);
////		paramMap.put("vehicle", vehicleType);
////		paramMap.put("operator", operator != null && !TextUtils.isEmpty(operator.getLoID()) ? operator.getLoID() : "");
////		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorVehicleOutResponseFactory(),
////				Request.Method.POST, BASE_URL.concat("/Nfc_mobile/vehicleOut"), paramMap);
//	}

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
}
