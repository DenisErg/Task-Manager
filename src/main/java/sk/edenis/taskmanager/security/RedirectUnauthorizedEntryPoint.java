package sk.edenis.taskmanager.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sk.edenis.taskmanager.constant.UserConsts;

@Component
public class RedirectUnauthorizedEntryPoint implements AuthenticationEntryPoint {

	private static final String REDIRECT_URL = UserConsts.LOGIN_URL + "?error=Unauthorized: Please log in to continue.";

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.sendRedirect(REDIRECT_URL);
	}

}
