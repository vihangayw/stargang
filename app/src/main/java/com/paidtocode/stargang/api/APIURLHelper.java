package com.paidtocode.stargang.api;

/**
 * Created by vihanga on 7/21/18 in stargang.
 */
public class APIURLHelper {

	private static final String BASE_URL = "http://209.97.172.234/index.php/api";

	private static final String REGISTER = "/register";
	private static final String LOGIN = "/login";
	private static final String ALL_USERS = "/allUsers";
	private static final String SUBSCRIBE = "/subscribe";
	private static final String UNSUBSCRIBE = "/unsubscribe";
	private static final String EDIT_PROFILE = "/editUser";

	public static String getRegisterURL() {
		return BASE_URL.concat(REGISTER);
	}

	public static String getLogin() {
		return BASE_URL.concat(LOGIN);
	}

	public static String getAllUsers() {
		return BASE_URL.concat(ALL_USERS);
	}

	public static String subscribeURL() {
		return BASE_URL.concat(SUBSCRIBE);
	}

	public static String unsubscribeURL() {
		return BASE_URL.concat(UNSUBSCRIBE);
	}

	public static String editProfileURL() {
		return BASE_URL.concat(EDIT_PROFILE);
	}
}
