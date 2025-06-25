package com.openclassrooms.starterjwt.unit.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService unit tests")
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	private User mockedUser;

	@BeforeEach
	public void setup() {
		mockedUser = new User()
				.setId(1L)
				.setEmail("test@test.com")
				.setFirstName("userFirstName")
				.setLastName("userLastName")
				.setAdmin(false);
	}

	@Test
	@DisplayName("it should test to delete a user")
	void shouldDeleteUser() {
		//Act
		userService.delete(1L);
		//Assert
		verify(userRepository).deleteById(1L);
	}

	@Test
	@DisplayName("it should test to find a user by his id")
	void shouldFindUserById() {
		//Arrange
		when(userRepository.findById(1L)).thenReturn(Optional.of(mockedUser));
		//Act
		User findedUser = userService.findById(1L);
		//Assert
		assertThat(findedUser).isEqualTo(mockedUser);
	}
}