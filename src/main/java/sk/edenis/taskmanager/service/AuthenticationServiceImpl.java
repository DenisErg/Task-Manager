package sk.edenis.taskmanager.service;

import java.util.NoSuchElementException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sk.edenis.taskmanager.constant.UserConsts;
import sk.edenis.taskmanager.dto.LoginUserDTO;
import sk.edenis.taskmanager.dto.RegisterUserDTO;
import sk.edenis.taskmanager.exception.UserLoginException;
import sk.edenis.taskmanager.exception.UserRegistrationException;
import sk.edenis.taskmanager.model.User;
import sk.edenis.taskmanager.repository.UserRepository;
import sk.edenis.taskmanager.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@Override
	public User signup(RegisterUserDTO input) {
		if (!isEmailAvailable(input.getEmail())) {
			throw new UserRegistrationException("Email is already in use!");
		}

		if (!input.getPassword().equals(input.getPasswordConfirmation())) {
			throw new UserRegistrationException("Passwords do not match!");
		}

		User user = new User();
		user.setFullName(input.getFullname());
		user.setPassword(passwordEncoder.encode(input.getPassword()));
		user.setEmail(input.getEmail());
		return userRepository.save(user);
	}

	@Override
	public User authenticate(LoginUserDTO input, HttpServletResponse response) {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(HashUtil.hashString(input.getEmail()), input.getPassword()));
		} catch (BadCredentialsException e) {
			throw new UserLoginException("Invalid email or password!");
		}

		User authenticatedUser = userRepository.findByEmail(input.getEmail()).get();		

		Cookie jwtCookie = createJWTCookie(jwtService.generateToken(authenticatedUser),jwtService.getExpirationTime());

		response.addCookie(jwtCookie);

		return authenticatedUser;
	}

	@Override
	public void logout(String jwt, HttpServletResponse response) {	
		jwtService.blacklistToken(jwt);
		response.addCookie(createJWTCookie("",0));		
	}
	
	private boolean isEmailAvailable(String email) {
		try {
			userRepository.findByEmail(email).get();
			return false;
		} catch (NoSuchElementException e) {
			return true;
		}
	}
	
	private Cookie createJWTCookie(String token, int expiration) {
		Cookie jwtCookie = new Cookie(UserConsts.JWT_COOKIE_NAME, token);
		jwtCookie.setHttpOnly(true);
		jwtCookie.setSecure(false); //https not implemented yet
		jwtCookie.setPath("/");
		jwtCookie.setMaxAge(expiration);
		
		return jwtCookie;
		
	}
}
