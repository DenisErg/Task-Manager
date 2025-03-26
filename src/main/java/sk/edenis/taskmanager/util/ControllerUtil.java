package sk.edenis.taskmanager.util;

import java.util.function.Function;

import org.springframework.web.reactive.function.client.ClientResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import reactor.core.publisher.Mono;
import sk.edenis.taskmanager.constant.UserConsts;
import sk.edenis.taskmanager.exception.MissingFieldException;

public class ControllerUtil {
	
	public static <T extends RuntimeException> Mono<String> handleResponse(ClientResponse response, Function<String, T> exceptionFactory) {
		if (response.statusCode().is2xxSuccessful()) {
	            return response.bodyToMono(String.class);
	    } else {
	            return response.bodyToMono(String.class)
	                .flatMap(body -> Mono.error(exceptionFactory.apply(ErrorUtil.extractErrorMessage(body))));
	    }
	}
	
	public static  String getJWT(HttpServletRequest request) {
    	String jwt = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (UserConsts.JWT_COOKIE_NAME.equals(cookie.getName())) {
                    jwt = cookie.getValue();   
                }
            }
        }
        
        if (jwt == null) { throw new MissingFieldException("JWT Token is missing in cookies!"); }
        
        return jwt;
    }

}
