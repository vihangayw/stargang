package com.paidtocode.stargang.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paidtocode.stargang.BuildConfig;
import com.paidtocode.stargang.NFCApplication;
import com.paidtocode.stargang.modal.Signup;

import java.io.IOException;

/**
 * Created by vihanga on 5/11/18 in ion-app.
 * Save and manage all shared preferences of the app.
 */
public class UserSessionManager {

	private static final String PREF_NAME = BuildConfig.APPLICATION_ID + ".pref";
	private static final String KEY_VEHICLE_LIST = "VehicleTypes";
	private static final String KEY_OPERATOR = "Operator";
	private static final String KEY_POINT = "Pint";
	private static final String KEY_AUTH_TOKEN = "Token";
	private static final String KEY_IS_LOGIN = "IsLogin";
	private static final String KEY_USER = "User";


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

	public void createUser(String json) {
		editor.putBoolean(KEY_IS_LOGIN, true);
		editor.putString(KEY_USER, json);
		editor.commit();
	}

	public void createToken(String token) {
		editor.putString(KEY_AUTH_TOKEN, token);
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
	public Signup getUser() {
		String modulePref = getUserPref();
		if (!TextUtils.isEmpty(modulePref)) {
			try {
				return new ObjectMapper().readValue(modulePref, Signup.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private String getUserPref() {
		return pref.getString(KEY_USER, null);
	}

	public String getAuthToken() {
		return pref.getString(KEY_AUTH_TOKEN, null);
	}

	public boolean isLogin() {
		return pref.getBoolean(KEY_IS_LOGIN, false);
	}

	public void clearPref() {
		editor.clear();
		editor.commit();
	}

}
