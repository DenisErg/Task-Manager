package sk.edenis.taskmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sk.edenis.taskmanager.constant.UserConsts;
import sk.edenis.taskmanager.exception.MissingFieldException;
import sk.edenis.taskmanager.service.JwtServiceImpl;
import sk.edenis.taskmanager.util.ControllerUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	
	private final HandlerExceptionResolver handlerExceptionResolver;

	private final JwtServiceImpl jwtService;
	private final UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtServiceImpl jwtService, UserDetailsService userDetailsService,
			HandlerExceptionResolver handlerExceptionResolver) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
	                                 @NonNull HttpServletResponse response,
	                                 @NonNull FilterChain filterChain) throws ServletException, IOException {

		if (isPublicPath(request.getRequestURI())) {
			filterChain.doFilter(request, response);
			return;
		}

		String jwt;
		
		try {
			jwt = ControllerUtil.getJWT(request);
		} catch (MissingFieldException e) {
			logger.warn("JWT Token is missing in cookies!");
			filterChain.doFilter(request, response);
			return;
		}

		if (jwtService.isTokenBlacklisted(jwt)) {
			logger.warn("Blocked token used: {}", jwt);
			filterChain.doFilter(request, response);
			return;
		}

		try {
			authenticate(jwt, request);
			filterChain.doFilter(request, response);
		} catch (Exception exception) {
			logger.error("Authentication error on [{}]: {}", request.getRequestURI(), exception.getMessage());
			handlerExceptionResolver.resolveException(request, response, null, exception);
			response.sendRedirect("/public/login");
		}
	}
	
	private void authenticate(String jwt, HttpServletRequest request) {
		String hashedEmail = jwtService.extractUsername(jwt);

		if (hashedEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) return;

		UserDetails userDetails = userDetailsService.loadUserByUsername(hashedEmail);

		if (jwtService.isTokenValid(jwt, userDetails)) {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());

			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
	}
	
	private boolean isPublicPath(String uri) {
	    return uri.matches("^/(api/auth|auth|public)/.*");
	}
}
