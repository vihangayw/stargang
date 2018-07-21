package com.paidtocode.stargang.api.request.helper.impl;

import com.paidtocode.stargang.api.APIHelper;
import com.paidtocode.stargang.api.request.helper.UserRequestHelper;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Vihanga on 3/15/2018.
 */

public class UserRequestHelperImpl implements UserRequestHelper {

	@Override
	public void vehicleOut(String vehicleType, String deviceID, String cardID, String locationID,
	                       APIHelper.PostManResponseListener listener) {
		Map<String, String> paramMap = new HashMap<>();
//		paramMap.put("device_id", deviceID);
//		paramMap.put("location", locationID);
//		paramMap.put("nfc_number", cardID);
//		paramMap.put("vehicle", vehicleType);
//		paramMap.put("operator", operator != null && !TextUtils.isEmpty(operator.getLoID()) ? operator.getLoID() : "");
//		APIHelper.getInstance().sendAuthStringRequestsWithParams(listener, new AncestorVehicleOutResponseFactory(),
//				Request.Method.POST, BASE_URL.concat("/Nfc_mobile/vehicleOut"), paramMap);
	}

}
