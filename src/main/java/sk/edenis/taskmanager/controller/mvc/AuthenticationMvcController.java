package sk.edenis.taskmanager.controller.mvc;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sk.edenis.taskmanager.constant.TaskConsts;
import sk.edenis.taskmanager.constant.UserConsts;
import sk.edenis.taskmanager.dto.LoginUserDTO;
import sk.edenis.taskmanager.dto.RegisterUserDTO;
import sk.edenis.taskmanager.exception.UserLoginException;
import sk.edenis.taskmanager.exception.UserRegistrationException;
import sk.edenis.taskmanager.util.ControllerUtil;

@Controller
public class AuthenticationMvcController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationMvcController.class);
	
	private final WebClient webClient;

	public AuthenticationMvcController(WebClient.Builder webClientBuilder
										,@Value("${user.api.base-url}") String userApiBaseUrl) {
		this.webClient = webClientBuilder.baseUrl(userApiBaseUrl).build();
	}
	
    @GetMapping(UserConsts.LOGIN_URL)
    public String loginPage(@RequestParam(required = false) String error, Model model,RedirectAttributes attributes) {    	
        model.addAttribute("error", error);
        return UserConsts.LOGIN_HTML;
    }

    @GetMapping(UserConsts.SIGN_UP_URL)
    public String signUpPage() {
        return UserConsts.SIGN_UP_HTML;
    }

	@PostMapping(UserConsts.SIGN_UP_URL)
	public RedirectView register(@ModelAttribute RegisterUserDTO registerUserDto, RedirectAttributes attributes) {
		
		logger.info("📡 Sending sign-up user request: POST");
		
 		try {  			
 			webClient.post()
	 		    .uri(UserConsts.SIGN_UP_URL)
	 		    .bodyValue(registerUserDto)
	 		    .exchangeToMono(response -> {
	 		        return ControllerUtil.handleResponse(response, UserRegistrationException::new);
	 		    })
	 		    .block(); 	
 			attributes.addFlashAttribute("email", registerUserDto.getEmail());
			return new RedirectView(UserConsts.LOGIN_URL);
		} catch (Exception e) {
			
			attributes.addFlashAttribute("fullname", registerUserDto.getFullname());
			attributes.addFlashAttribute("error", e.getMessage());
			logger.error("Register error: " + e.getMessage());
			return new RedirectView(UserConsts.SIGN_UP_URL);
		}
	}

	@PostMapping(UserConsts.LOGIN_URL)
	public RedirectView authenticate(@ModelAttribute LoginUserDTO loginUserDto, RedirectAttributes attributes, HttpServletResponse servletResponse) {	
		
		logger.info("📡 Sending login user request: POST");
		try {			
			webClient.post()
 		    .uri(UserConsts.LOGIN_URL)
 		    .bodyValue(loginUserDto)
 		    .exchangeToMono(response -> {	  
 		    	forwardCookies(response, servletResponse);
 		    	return ControllerUtil.handleResponse(response, UserLoginException::new);		        
 		    })
 		    .block();
			
			attributes.addFlashAttribute("email", loginUserDto.getEmail());
			return new RedirectView(TaskConsts.TASK_LIST_URL);
		} catch (Exception  e) {
			logger.error("Login error: " + e.getMessage());
			return new RedirectView(UserConsts.LOGIN_ERROR_URL + e.getMessage());
		}
	}
	
	@PostMapping(UserConsts.LOGOUT_URL)
	public RedirectView logout(HttpServletRequest request, HttpServletResponse servletResponse, RedirectAttributes attributes) {
		logger.info("📡 Sending logout request: POST");
		try {	
			
			String jwt = ControllerUtil.getJWT(request);
			
			webClient.post()
				.uri(UserConsts.LOGOUT_URL)
	        	.cookies(cookies -> cookies.add(UserConsts.JWT_COOKIE_NAME,jwt))
	            .exchangeToMono(response -> {
	            	forwardCookies(response, servletResponse);
	            	return ControllerUtil.handleResponse(response, IllegalStateException::new);
	            })
	 		    .block();
			
			return new RedirectView(UserConsts.LOGIN_ERROR_URL + UserConsts.USER_LOGGED_OUT_MESSAGE);
		} catch (Exception  e) {
			logger.error("Logout error: " + e.getMessage());
			return new RedirectView(TaskConsts.TASK_LIST_URL + e.getMessage());
		}
	}
	
	private ClientResponse forwardCookies(ClientResponse response, HttpServletResponse servletResponse) {
		response.cookies().values().stream()
	        .flatMap(Collection::stream)
	        .forEach(cookie -> servletResponse.addHeader("Set-Cookie", cookie.toString()));	
		return response;
	}
	
}
