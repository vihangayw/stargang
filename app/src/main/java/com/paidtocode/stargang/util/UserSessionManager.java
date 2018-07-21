package com.paidtocode.stargang.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.paidtocode.stargang.BuildConfig;
import com.paidtocode.stargang.NFCApplication;

/**
 * Created by vihanga on 5/11/18 in ion-app.
 * Save and manage all shared preferences of the app.
 */
public class UserSessionManager {

	private static final String PREF_NAME = BuildConfig.APPLICATION_ID + ".pref";
	private static final String KEY_VEHICLE_LIST = "VehicleTypes";
	private static final String KEY_OPERATOR = "Operator";
	private static final String KEY_POINT = "Pint";


	private final static UserSessionManager instance =
			new UserSessionManager(NFCApplication.getInstance().getApplicationContext());

	private final SharedPreferences pref;
	private final SharedPreferences.Editor editor;

	private UserSessionManager(Context context) {
		int PRIVATE_MODE = 0;
		pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public static UserSessionManager getInstance() {
		return instance;
	}

	public void createVehicleList(String json) {
		editor.putString(KEY_VEHICLE_LIST, json);
		editor.commit();
	}

	public void createPoint(String point) {
		editor.putString(KEY_POINT, point);
		editor.commit();
	}

	public void createOperator(String json) {
		editor.putString(KEY_OPERATOR, json);
		editor.commit();
	}

	public void removeOperator() {
		editor.remove(KEY_OPERATOR);
		editor.commit();
	}

//	public VehicleTypeList getVehicleList() {
//		String modulePref = getVehicleListPref();
//		if (!TextUtils.isEmpty(modulePref)) {
//			try {
//				return new ObjectMapper().readValue(modulePref, VehicleTypeList.class);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
//
//	public Operator getOperator() {
//		String modulePref = getKeyOperatorPref();
//		if (!TextUtils.isEmpty(modulePref)) {
//			try {
//				return new ObjectMapper().readValue(modulePref, Operator.class);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}

	private String getVehicleListPref() {
		return pref.getString(KEY_VEHICLE_LIST, null);
	}

	public String getPointName() {
		return pref.getString(KEY_POINT, "");
	}

	private String getKeyOperatorPref() {
		return pref.getString(KEY_OPERATOR, null);
	}

	public void clearPref() {
		editor.clear();
		editor.commit();
	}

}
