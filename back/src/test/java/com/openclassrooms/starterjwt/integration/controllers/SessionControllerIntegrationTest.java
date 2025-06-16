package com.openclassrooms.starterjwt.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
class SessionControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SessionRepository sessionRepository;

	private Long sessionId;

	@BeforeEach
	void setup() {
		sessionRepository.deleteAll();
		Session session = new Session();
		session.setName("Test Session");
		session.setDescription("Session description");
		session.setDate(new Date());
		sessionId = sessionRepository.save(session).getId();
	}

	@Test
	void shouldFindSessionById() throws Exception {
		System.out.println(sessionRepository.findAll());
		mockMvc.perform(get("/api/session/" + sessionId)
						.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Test Session"));

	}

	@Test
	void shouldFindAllSessions() throws Exception {
		mockMvc.perform(get("/api/session/").contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Test Session"));
	}

	@Test
	void shouldCreateSession() throws Exception {
		SessionDto newSession = new SessionDto();
		newSession.setName("created session name");
		newSession.setDescription("Session description");
		newSession.setDate(new Date());
		newSession.setTeacher_id(3L);

		mockMvc.perform(post("/api/session/")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newSession)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("created session name"));
		Session session = sessionRepository.findById(sessionId + 1).orElse(null);
		assertNotNull(session);
		assertEquals("created session name", session.getName());
		assertEquals(sessionId + 1, session.getId());
	}

	@Test
	void shouldUpdateSession() throws Exception {
		SessionDto updatedSession = new SessionDto();
		updatedSession.setName("Session test updated");
		updatedSession.setDescription("Session description");
		updatedSession.setDate(new Date());
		updatedSession.setTeacher_id(3L);
		mockMvc.perform(put("/api/session/" + sessionId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedSession)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Session test updated"));

		Session session = sessionRepository.findById(sessionId).orElse(null);
		assertNotNull(session);
		assertEquals("Session test updated", session.getName());

	}

	@Test
	void shouldDeleteSession() throws Exception {
		mockMvc.perform(delete("/api/session/" + sessionId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		Session session = sessionRepository.findById(3L).orElse(null);
		assertNull(session);
	}

	@Test
	void shouldParticipate() throws Exception {
		mockMvc.perform(post("/api/session/" + sessionId + "/participate/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	void noLongerParticipate() throws Exception {
		Session session = sessionRepository.findById(sessionId).orElse(null);
		assertNotNull(session);
		session.setUsers(List.of(new User().setId(1L)));
		sessionRepository.save(session);

		mockMvc.perform(delete("/api/session/" + sessionId + "/participate/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}