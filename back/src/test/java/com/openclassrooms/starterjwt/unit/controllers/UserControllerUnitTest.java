package com.openclassrooms.starterjwt.unit.controllers;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController unit tests")
class UserControllerUnitTest {
	@InjectMocks
	UserController userController;
	@Mock
	private UserMapper userMapper;
	@Mock
	private UserService userService;

	@Test
	@DisplayName("it should test to find user by his id")
	void shouldFindUserById() {
		//Arrange
		User user = new User().setId(1L);
		UserDto userDto = new UserDto();
		when(userMapper.toDto(user)).thenReturn(userDto);
		when(userService.findById(1L)).thenReturn(user);
		//Act
		ResponseEntity<?> response = userController.findById("1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(userDto);
	}

	@Test
	@DisplayName("it should test if user is not found")
	void shouldReturnNotFoundWhenFindUserById() {
		//Arrange
		when(userService.findById(1L)).thenReturn(null);
		//Act
		ResponseEntity<?> response = userController.findById("1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DisplayName("it should test if id is bad during find user by id")
	void shouldReturnBadRequestWhenFindUserById() {
		//Act
		ResponseEntity<?> response = userController.findById("abc");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("it should test to delete user")
	void shouldSaveUser() {
		//Arrange
		User user = new User().setId(1L).setEmail("test@test.com");
		UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@test.com", "userFirstName", "userLastName", false, "userPassword");
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
		when(userService.findById(1L)).thenReturn(user);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//Act
		ResponseEntity<?> response = userController.save("1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(userService).delete(1L);
	}

	@Test
	@DisplayName("it should test if unauthorized request")
	void shouldReturnUnauthorizedWhenSaveUser() {
		//Arrange
		User user = new User().setId(1L).setEmail("test1@test.com");
		UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@test.com", "userFirstName", "userLastName", false, "userPassword");
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
		when(userService.findById(1L)).thenReturn(user);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//Act
		ResponseEntity<?> response = userController.save("1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	@DisplayName("it should test if user is found before delete")
	void shouldReturnNotFoundWhenSaveUser() {
		//Arrange
		when(userService.findById(1L)).thenReturn(null);
		//Act
		ResponseEntity<?> response = userController.save("1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DisplayName("it should test if id is bad during delete user")
	void shouldReturnBadRequestWhenSaveUser() {
		//Act
		ResponseEntity<?> response = userController.save("abc");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
}