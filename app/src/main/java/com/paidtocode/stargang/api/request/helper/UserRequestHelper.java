package com.paidtocode.stargang.api.request.helper;


import com.paidtocode.stargang.api.APIHelper;

/**
 * Created by Vihanga on 3/8/2018.
 */

public interface UserRequestHelper {

	void vehicleOut(String vehicleType, String deviceID, String cardID, String locationID,
	                APIHelper.PostManResponseListener listener);


}
