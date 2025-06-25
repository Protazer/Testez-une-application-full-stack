package com.openclassrooms.starterjwt.unit.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetailsServiceImpl unit tests")
class UserDetailsServiceImplTest {
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;

	@Test
	@DisplayName("it should test to load user by his userName")
	void shouldLoadUserByUsername() {
		//Arrange
		User mockedUser = new User()
				.setId(1L)
				.setEmail("test@studio.com")
				.setFirstName("userFirstName")
				.setLastName("userLastName")
				.setPassword("userPassword")
				.setAdmin(false);

		when(userRepository.findByEmail("test@studio.com")).thenReturn(Optional.of(mockedUser));

		//Act
		UserDetails userDetails = userDetailsService.loadUserByUsername("test@studio.com");

		//Assert
		assertNotNull(userDetails);
		assertEquals("test@studio.com", userDetails.getUsername());
	}

	@Test
	@DisplayName("it should test if user is loaded with bad userName")
	void shouldNotLoadUserByUsername() {
		//Arrange
		when(userRepository.findByEmail("test@studio.com")).thenReturn(Optional.empty());

		// Act & Assert
		UsernameNotFoundException exception = assertThrows(
				UsernameNotFoundException.class,
				() -> userDetailsService.loadUserByUsername("test@studio.com")
		);
		assertEquals("User Not Found with email: test@studio.com", exception.getMessage());
	}

	
}