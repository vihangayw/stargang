package com.paidtocode.stargang.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paidtocode.stargang.BuildConfig;
import com.paidtocode.stargang.StarGangApplication;
import com.paidtocode.stargang.modal.Signup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vihanga on 5/11/18 in ion-app.
 * Save and manage all shared preferences of the app.
 */
public class UserSessionManager {

	private static final String PREF_NAME = BuildConfig.APPLICATION_ID + ".pref";
	private static final String KEY_EXP = "EXP";
	private static final String KEY_AUTH_TOKEN = "Token";
	private static final String KEY_IS_LOGIN = "IsLogin";
	private static final String KEY_USER = "User";
	private static final String KEY_FIREBASE_UID = "FirebaseUID";
	private static final String KEY_FIREBASE_NOTIFICATION_TOKEN = "FirebaseToken";
	private static final String KEY_FIREBASE_AUTH_VERIFICATION_ID = "VerificationID";
	private static final String KEY_FIREBASE_AUTH_RESEND_TOKEN = "ResendToken";

	private final static UserSessionManager instance =
			new UserSessionManager(StarGangApplication.getInstance().getApplicationContext());

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

	public void createFirebaseUid(String uID) {
		editor.putString(KEY_FIREBASE_UID, uID);
		// commit changes
		editor.commit();
	}

	public void creataEXP() {
		editor.putString(KEY_EXP, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
		// commit changes
		editor.commit();
	}

	public String getExp() {
		return pref.getString(KEY_EXP, null);
	}

	public void createAuthRetry(String vid, String retryToken) {
		editor.putString(KEY_FIREBASE_AUTH_VERIFICATION_ID, vid);
		editor.putString(KEY_FIREBASE_AUTH_RESEND_TOKEN, retryToken);
		// commit changes
		editor.commit();
	}

	public void createFirebaseToken(String token) {
		editor.putString(KEY_FIREBASE_NOTIFICATION_TOKEN, token);
		editor.commit();
	}

	public String getVerificationId() {
		return pref.getString(KEY_FIREBASE_AUTH_VERIFICATION_ID, null);
	}

	public String getFirebaseToken() {
		return pref.getString(KEY_FIREBASE_NOTIFICATION_TOKEN, null);
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

	public Signup getUser() {
		String modulePref = getUserPref();
		if (!TextUtils.isEmpty(modulePref)) {
			try {
				Signup signup = new ObjectMapper().readValue(modulePref, Signup.class);
				Log.d("SessionManager", "id: " + signup.getId());
				return signup;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private String getUserPref() {
		return pref.getString(KEY_USER, null);
	}

	public String getAuthToken() {
		String string = pref.getString(KEY_AUTH_TOKEN, null);
		Log.d("SessionManager", "tkn: " + string);
		return string;
	}

	public boolean isLogin() {
		return pref.getBoolean(KEY_IS_LOGIN, false);
	}

	public void clearPref() {
		editor.clear();
		editor.commit();
	}

	public void logout() {
		editor.remove(KEY_AUTH_TOKEN);
		editor.remove(KEY_IS_LOGIN);
		editor.remove(KEY_USER);
		editor.remove(KEY_FIREBASE_UID);
		editor.remove(KEY_FIREBASE_NOTIFICATION_TOKEN);
		editor.remove(KEY_FIREBASE_AUTH_VERIFICATION_ID);
		editor.remove(KEY_FIREBASE_AUTH_RESEND_TOKEN);
		editor.commit();
	}

}
