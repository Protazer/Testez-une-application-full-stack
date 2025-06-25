package com.openclassrooms.starterjwt.unit.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("SessionRepository unit tests")
class SessionRepositoryTest {

	@Autowired
	private SessionRepository sessionRepository;

	@Test
	@DisplayName("it should test to get all sessions")
	void shouldGetAllSessions() {
		// Act
		List<Session> sessions = sessionRepository.findAll();
		//Assert
		assertEquals(2, sessions.size());
	}

	@Test
	@DisplayName("it should test to get a session by his id")
	void shouldGetSessionById() {
		// Act
		Session session = sessionRepository.findById(1L).orElse(null);
		//Assert
		assertNotNull(session);
		assertEquals("TEST1", session.getName());
	}

	@Test
	@DisplayName("it should test to create a session")
	void shouldSaveSession() {
		//Arrange
		Teacher teacher = new Teacher();
		teacher.setId(1L);
		teacher.setFirstName("john");
		teacher.setLastName("Doe");

		Session newSession = new Session();
		newSession.setId(1L);
		newSession.setName("TEST3");
		newSession.setDescription("Description of session TEST3");
		newSession.setDate(new Date());
		newSession.setTeacher(teacher);

		// Act
		Session savedSession = sessionRepository.save(newSession);

		//Assert
		assertNotNull(savedSession.getId());
		assertEquals(newSession.getName(), savedSession.getName());
	}

	@Test
	@DisplayName("it should test to update a session")
	void shouldUpdateSession() {
		// Act
		Session foundedSession = sessionRepository.findById(1L).orElse(null).setName("test");
		Session updatedSession = sessionRepository.save(foundedSession);
		//Assert
		assertNotNull(updatedSession);
		assertEquals("test", updatedSession.getName());

	}

	@Test
	@DisplayName("it should test to delete a session")
	void shouldDeleteSession() {
		// Act
		sessionRepository.deleteById(1L);
		Optional<Session> session = sessionRepository.findById(1L);
		//Assert
		assertFalse(session.isPresent());

	}
}