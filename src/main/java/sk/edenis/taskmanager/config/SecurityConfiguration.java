package sk.edenis.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import sk.edenis.taskmanager.constant.UserConsts;
import sk.edenis.taskmanager.security.JwtAuthenticationFilter;
import sk.edenis.taskmanager.security.RedirectAccessDeniedHandler;
import sk.edenis.taskmanager.security.RedirectUnauthorizedEntryPoint;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	private final AuthenticationProvider authenticationProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final RedirectAccessDeniedHandler accessDeniedHandler;
	private final RedirectUnauthorizedEntryPoint authenticationEntryPoint;

	public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
								AuthenticationProvider authenticationProvider, 
								RedirectAccessDeniedHandler accessDeniedHandler,
								RedirectUnauthorizedEntryPoint authenticationEntryPoint) {
		this.authenticationProvider 	= authenticationProvider;
		this.jwtAuthenticationFilter 	= jwtAuthenticationFilter;
		this.accessDeniedHandler 		= accessDeniedHandler;
		this.authenticationEntryPoint 	= authenticationEntryPoint;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**", "/api/auth/**", "/public/**")
				.permitAll().anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler)
														 .authenticationEntryPoint(authenticationEntryPoint));
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(List.of("http://localhost:8005"));
		configuration.setAllowedMethods(List.of("GET", "POST"));
		configuration.setAllowedHeaders(List.of(UserConsts.JWT_COOKIE_NAME, "Content-Type"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
