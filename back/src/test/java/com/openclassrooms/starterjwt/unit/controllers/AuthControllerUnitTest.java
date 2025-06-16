package com.openclassrooms.starterjwt.unit.controllers;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {

	@Mock
	UserRepository userRepository;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private JwtUtils jwtUtils;
	@Mock
	private PasswordEncoder passwordEncoder;
	@InjectMocks
	private AuthController authController;

	@Test
	void shouldAuthenticateUser() {
		//Arrange
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("test@test.com");
		loginRequest.setPassword("test!1234");

		UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@test.com", "userFirstName", "userLastName", true, "test!1234");
		Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
		Authentication authenticationResponse = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		User mockedUser = new User().setEmail("test@test.com")
				.setPassword("test!1234")
				.setFirstName("userFirstName")
				.setLastName("userLastName")
				.setAdmin(true);

		when(authenticationManager.authenticate(authenticationRequest)).thenReturn(authenticationResponse);
		when(jwtUtils.generateJwtToken(authenticationResponse)).thenReturn("jwt");
		when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(mockedUser));

		//Act
		ResponseEntity<?> response = authController.authenticateUser(loginRequest);

		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertNotNull(response.getBody());
		JwtResponse jwtResponse = (JwtResponse) response.getBody();
		assertThat(jwtResponse.getFirstName()).isEqualTo("userFirstName");
		assertThat(jwtResponse.getLastName()).isEqualTo("userLastName");
		assertThat(jwtResponse.getAdmin()).isEqualTo(true);
		assertThat(jwtResponse.getToken()).isEqualTo("jwt");
	}

	@Test
	void shouldGetIsAdminFalseWhenAuthenticateUser() {
		//Arrange
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("test@test.com");
		loginRequest.setPassword("test!1234");
		UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@test.com", "userFirstName", "userLastName", true, "test!1234");

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		when(authenticationManager.authenticate(any())).thenReturn(authentication);
		when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt");
		when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

		//Act
		ResponseEntity<?> response = authController.authenticateUser(loginRequest);

		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		JwtResponse jwtResponse = (JwtResponse) response.getBody();
		assertNotNull(jwtResponse);
		assertThat(jwtResponse.getFirstName()).isEqualTo("userFirstName");
		assertThat(jwtResponse.getLastName()).isEqualTo("userLastName");
		assertThat(jwtResponse.getAdmin()).isEqualTo(false);
		assertThat(jwtResponse.getToken()).isEqualTo("jwt");
	}

	@Test
	void shouldRegisterUser() {
		//Arrange
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("test@test.com");
		signupRequest.setFirstName("userFirstName");
		signupRequest.setLastName("userLastName");
		signupRequest.setPassword("test!1234");

		when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
		when(passwordEncoder.encode("test!1234")).thenReturn("encodedPassword");

		//Act
		ResponseEntity<?> response = authController.registerUser(signupRequest);

		//Assert
		assertNotNull(response.getBody());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		MessageResponse messageResponse = (MessageResponse) response.getBody();
		assertThat(messageResponse.getMessage()).isEqualTo("User registered successfully!");
	}

	@Test
	void shouldReturnBadRequestWhenRegisterUser() {
		//Arrange
		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("test@test.com");
		signupRequest.setFirstName("userFirstName");
		signupRequest.setLastName("userLastName");
		signupRequest.setPassword("test!1234");

		when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

		//Act
		ResponseEntity<?> response = authController.registerUser(signupRequest);

		//Assert
		assertNotNull(response.getBody());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		MessageResponse messageResponse = (MessageResponse) response.getBody();
		assertThat(messageResponse.getMessage()).isEqualTo("Error: Email is already taken!");
	}
}