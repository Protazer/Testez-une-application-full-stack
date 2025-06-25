package com.openclassrooms.starterjwt.unit.controllers;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SessionController unit tests")
class SessionControllerUnitTest {
	@Mock
	private SessionMapper sessionMapper;
	@Mock
	private SessionService sessionService;
	@InjectMocks
	private SessionController sessionController;


	@Test
	@DisplayName("it should test to find session by his id")
	void shouldFindSessionById() {
		//Arrange
		Session mockedSession = new Session().setId(1L);
		when(sessionService.getById(1L)).thenReturn(mockedSession);
		//Act
		ResponseEntity<?> response = sessionController.findById("1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("it should test if session is not found by his id")
	void shouldNotFindSessionById() {
		//Arrange
		when(sessionService.getById(1L)).thenReturn(null);
		//Act
		ResponseEntity<?> response = sessionController.findById("1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DisplayName("it should test if id is bad during find session by id")
	void shouldReturnBadRequestWhenFindSessionById() {
		//Act
		ResponseEntity<?> response = sessionController.findById("abc");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("it should test to find all sessions")
	void shouldFindAllSessions() {
		//Act
		ResponseEntity<?> response = sessionController.findAll();
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("it should test to create a new session")
	void shouldCreateSession() {
		//Arrange
		Session session = new Session();
		SessionDto sessionDto = new SessionDto();

		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
		when(sessionService.create(session)).thenReturn(session);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);
		//Act
		ResponseEntity<?> response = sessionController.create(sessionDto);
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(sessionDto);
	}

	@Test
	@DisplayName("it should test to update a session")
	void shouldUpdateSession() {
		//Arrange
		SessionDto sessionDto = new SessionDto();
		Session session = new Session().setId(1L);
		when(sessionMapper.toDto(session)).thenReturn(sessionDto);
		when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
		when(sessionService.update(1L, session)).thenReturn(session);
		//Act
		ResponseEntity<?> response = sessionController.update("1", sessionDto);
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(sessionDto);
	}

	@Test
	@DisplayName("it should test if id is bad during update session")
	void shouldReturnBadRequestWhenUpdatingSession() {
		//Arrange
		SessionDto sessionDto = new SessionDto();
		//Act
		ResponseEntity<?> response = sessionController.update("abc", sessionDto);
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("it should test to delete session")
	void shouldSaveSession() {
		//Arrange
		Session session = new Session().setId(1L);
		when(sessionService.getById(1L)).thenReturn(session);
		//Act
		ResponseEntity<?> response = sessionController.save("1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(sessionService).delete(1L);
	}

	@Test
	@DisplayName("it should test if session to delete is found")
	void shouldReturnNotFoundWhenSaveSession() {
		//Arrange
		Session session = new Session().setId(1L);
		when(sessionService.getById(1L)).thenReturn(null);
		//Act
		ResponseEntity<?> response = sessionController.save("1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DisplayName("it should test if bad session id during delete")
	void shouldReturnBadRequestWhenSaveSession() {
		//Act
		ResponseEntity<?> response = sessionController.save("abc");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("it should test session participate")
	void shouldParticipate() {
		//Act
		ResponseEntity<?> response = sessionController.participate("1", "1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(sessionService).participate(1L, 1L);
	}

	@Test
	@DisplayName("it should test if bad session id during participate")
	void shouldReturnBadRequestWhenParticipate() {
		//Act
		ResponseEntity<?> response = sessionController.participate("abc", "1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("it should test to no longer participate")
	void noLongerParticipate() {
		//Act
		ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(sessionService).noLongerParticipate(1L, 1L);
	}

	@Test
	@DisplayName("it should test if bad session id during no longer participate")
	void shouldReturnBadRequestWhenNoLongerParticipate() {
		//Act
		ResponseEntity<?> response = sessionController.noLongerParticipate("abc", "1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
}