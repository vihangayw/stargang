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
	private static final String OLD_POST = "/oldPost";
	private static final String WALL = "/wall";
	private static final String ADD_POST = "/addPost";
	private static final String LOGOUT = "/logOut";
	private static final String USER_PROFILE = "/userInfo";
	private static final String RESET_PW = "/resetPassword";
	private static final String ADD_COMMENT = "/addComment";
	private static final String VIEW_COMMENT = "/viewComment";
	private static final String LIKE = "/like";

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

	public static String addPostURL() {
		return BASE_URL.concat(ADD_POST);
	}

	public static String getOldPostURL() {
		return BASE_URL.concat(OLD_POST);
	}

	public static String getWallPostsURL() {
		return BASE_URL.concat(WALL);
	}

	public static String getLikePostsURL() {
		return BASE_URL.concat(LIKE);
	}

	public static String getAddCommentURL() {
		return BASE_URL.concat(ADD_COMMENT);
	}

	public static String getViewCommentURL() {
		return BASE_URL.concat(VIEW_COMMENT);
	}

	public static String getUserProfileURL() {
		return BASE_URL.concat(USER_PROFILE);
	}

	public static String getPasswordResetURL() {
		return BASE_URL.concat(RESET_PW);
	}

	public static String getLogoutURL() {
		return BASE_URL.concat(LOGOUT);
	}
}
