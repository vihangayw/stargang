package com.paidtocode.stargang.api;

/**
 * Created by vihanga on 7/21/18 in stargang.
 */
public class APIURLHelper {

	private static final String BASE_URL = "http://209.97.172.234/index.php/api";

	private static final String REGISTER = "/register";

	public static String getRegisterURL() {
		return BASE_URL.concat(REGISTER);
	}
}
