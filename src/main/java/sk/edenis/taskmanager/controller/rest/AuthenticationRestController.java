package sk.edenis.taskmanager.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import sk.edenis.taskmanager.constant.UserConsts;
import sk.edenis.taskmanager.dto.ApiResponse;
import sk.edenis.taskmanager.dto.LoginUserDTO;
import sk.edenis.taskmanager.dto.RegisterUserDTO;
import sk.edenis.taskmanager.service.AuthenticationService;
import sk.edenis.taskmanager.util.ControllerUtil;
import sk.edenis.taskmanager.util.ErrorUtil;

@RestController
@RequestMapping("/api")
public class AuthenticationRestController {

	private final AuthenticationService authenticationService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationRestController.class);

	public AuthenticationRestController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping(UserConsts.SIGN_UP_URL)
	public ResponseEntity<?> register(@RequestBody @Valid RegisterUserDTO registerUserDTO
										,BindingResult bindingResult) {
		
		logger.info("📡 Receiving sign-up registerUserDTO request: POST");

		if (bindingResult.hasErrors()) {
			return ResponseEntity
					.badRequest()
					.body(ErrorUtil.formatErrorMessage("Invalid or missing fields", bindingResult));
		}
		
		authenticationService.signup(registerUserDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(UserConsts.USER_CREATED_MESSAGE));
	}

	@PostMapping(UserConsts.LOGIN_URL)
	public ResponseEntity<?> authenticate(@RequestBody @Valid LoginUserDTO loginUserDTO,
											BindingResult bindingResult,
											HttpServletResponse response) {
		
		logger.info("📡 Receiving login loginUserDTO request: POST");
		
		if (bindingResult.hasErrors()) {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(ErrorUtil.formatErrorMessage("Invalid or missing fields", bindingResult));
		}
		
		authenticationService.authenticate(loginUserDTO, response);
		return ResponseEntity.ok(new ApiResponse("Login successful."));
	}
	
	@PostMapping(UserConsts.LOGOUT_URL)
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
		logger.info("📡 Receiving logout request: POST");
		
		String jwt = ControllerUtil.getJWT(request);
		
		authenticationService.logout(jwt, response);
		return ResponseEntity.ok(new ApiResponse("Logout successful."));
	}
}