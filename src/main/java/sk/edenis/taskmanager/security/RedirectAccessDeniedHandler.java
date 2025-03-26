package sk.edenis.taskmanager.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sk.edenis.taskmanager.constant.UserConsts;

@Component
public class RedirectAccessDeniedHandler implements AccessDeniedHandler {

	private static final String REDIRECT_URL = UserConsts.LOGIN_URL + "?error=You do not have permission to access this resource.";

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException {
		response.sendRedirect(REDIRECT_URL);
	}

}
