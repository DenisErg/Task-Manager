package sk.edenis.taskmanager.service;

import jakarta.servlet.http.HttpServletResponse;
import sk.edenis.taskmanager.dto.LoginUserDTO;
import sk.edenis.taskmanager.dto.RegisterUserDTO;
import sk.edenis.taskmanager.model.User;

public interface AuthenticationService {
	
	public User signup(RegisterUserDTO input);
	
	public User authenticate(LoginUserDTO input, HttpServletResponse response);
	
	public void logout(String jwt, HttpServletResponse response);

}
