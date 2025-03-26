package sk.edenis.taskmanager.constant;

public class UserConsts {

	//JWT COOKIE NAME
	public static final String JWT_COOKIE_NAME = "Authorization";
	
	//HTMLs
	public static final String LOGIN_HTML = "login";
	public static final String SIGN_UP_HTML = "sign-up";
	
	//URLs
	public static final String LOGIN_URL = "/auth/login";
	public static final String SIGN_UP_URL = "/auth/sign-up";
	public static final String LOGOUT_URL = "/auth/logout";
	public static final String LOGIN_ERROR_URL="/auth/login?error=";
	
	//MESSAGES
	public static final String USER_CREATED_MESSAGE = "User created.";
	public static final String USER_UNAUTHENTICATED_MESSAGE = "User is not authenticated!";
	public static final String USER_NOT_FOUND_MESSAGE = "User not found";
	public static final String USER_LOGGED_OUT_MESSAGE = "You have been logged out.";
	
}
