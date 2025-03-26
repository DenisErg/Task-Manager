package sk.edenis.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterUserDTO {

	@NotBlank(message = "Email is required")
	@Email
	private String email;

	@NotBlank(message = "Password is required")
	private String password;

	@NotBlank(message = "Password confirmation is required")
	private String passwordConfirmation;

	@NotBlank(message = "Fullname is required")
	private String fullname;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

}
