package com.openclassrooms.starterjwt.integration.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@studio.com", roles = {"ADMIN"})
class UserControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	private Long userId;


	@BeforeEach
	public void setup() {
		User newUser = new User();
		newUser.setFirstName("userLastName");
		newUser.setLastName("userLastName");
		newUser.setAdmin(true);
		newUser.setEmail("test@studio.com");
		newUser.setPassword("Password");
		userId = userRepository.save(newUser).getId();
	}

	@Test
	void shouldFindUserById() throws Exception {
		mockMvc.perform(get("/api/user/" + userId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("test@studio.com"));
	}

	@Test
	void shouldSaveUser() throws Exception {
		mockMvc.perform(delete("/api/user/" + userId))
				.andExpect(status().isOk());

		User user = userRepository.findById(userId).orElse(null);
		assertNull(user);
	}
}