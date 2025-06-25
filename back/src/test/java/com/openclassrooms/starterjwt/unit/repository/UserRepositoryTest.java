package com.openclassrooms.starterjwt.unit.repository;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("UserRepository unit tests")
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;

	@Test
	@DisplayName("it should test to get all users")
	void shouldGetAllUsers() {
		// Act
		List<User> users = userRepository.findAll();
		//Assert
		assertEquals(1, users.size());
	}

	@Test
	@DisplayName("it should test to get user by his id")
	void shouldGetUserById() {
		// Act
		User user = userRepository.findById(1L).orElse(null);
		//Assert
		assertNotNull(user);
		assertEquals("Admin", user.getFirstName());
	}

	@Test
	@DisplayName("it should test to create a new user")
	void shouldSaveUser() {
		//Arrange
		User newUser = new User();
		newUser.setId(1L);
		newUser.setAdmin(true);
		newUser.setEmail("email@email.test");
		newUser.setFirstName("john");
		newUser.setLastName("Doe");
		newUser.setPassword("<PASSWORD>");

		// Act
		User savedUser = userRepository.save(newUser);

		//Assert
		assertNotNull(savedUser.getId());
		assertEquals(newUser.getFirstName(), savedUser.getFirstName());
	}

	@Test
	@DisplayName("it should test to update a user")
	void shouldUpdateUser() {
		// Act
		User foundedUser = Objects.requireNonNull(userRepository.findById(1L).orElse(null)).setFirstName("user1");
		User updatedUser = userRepository.save(foundedUser);
		//Assert
		assertEquals("user1", updatedUser.getFirstName());

	}

	@Test
	@DisplayName("it should test to delete a user")
	void shouldDeleteUser() {
		// Act
		userRepository.deleteById(1L);
		Optional<User> user = userRepository.findById(1L);
		//Assert
		assertFalse(user.isPresent());

	}

}