package com.openclassrooms.starterjwt.integration.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
@DisplayName("TeacherController integration tests")
class TeacherControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("it should test to find a teach by his id")
	void shouldFindTeacherById() throws Exception {
		mockMvc.perform(get("/api/teacher/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("John"));
	}

	@Test
	@DisplayName("it should test to find all teachers")
	void shouldFindAllTeachers() throws Exception {
		mockMvc.perform(get("/api/teacher"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName").value("John"));
	}
}