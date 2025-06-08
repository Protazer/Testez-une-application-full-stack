package com.openclassrooms.starterjwt.unit.repository;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void shouldGetAllUsers() {
        // Act
        List<User> users = userRepository.findAll();
        //Assert
        assertEquals(1, users.size());
    }

    @Test
    void shouldGetUserById() {
        // Act
        User user = userRepository.findById(1L).orElse(null);
        //Assert
        assertNotNull(user);
        assertEquals("Admin", user.getFirstName());
    }

    @Test
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
    void shouldUpdateUser() {
        // Act
        User foundedUser = userRepository.findById(1L).orElse(null).setFirstName("user1");
        User updatedUser = userRepository.save(foundedUser);
        //Assert
        assertEquals("user1", updatedUser.getFirstName());

    }

    @Test
    void shouldDeleteUser() {
        // Act
        userRepository.deleteById(1L);
        Optional<User> user = userRepository.findById(1L);
        //Assert
        assertFalse(user.isPresent());

    }

}