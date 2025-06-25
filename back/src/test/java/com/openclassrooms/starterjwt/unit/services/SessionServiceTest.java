package com.openclassrooms.starterjwt.unit.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SessionService unit tests")
class SessionServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private SessionRepository sessionRepository;

	@InjectMocks
	private SessionService sessionService;

	private Session mockedSession1;

	private Session mockedSession2;

	private User mockedUser1;

	private User mockedUser2;


	@BeforeEach
	void setup() {
		mockedSession1 = new Session().setName("testName1")
				.setId(1L)
				.setDescription("sessionDescription 1")
				.setUsers(new ArrayList<>())
				.setDate(new Date())
				.setTeacher(new Teacher());

		mockedSession2 = new Session()
				.setName("testName2")
				.setId(2L).setDescription("sessionDescription 2")
				.setDate(new Date()).setUsers(List.of())
				.setTeacher(new Teacher());


		mockedUser1 = new User()
				.setId(1L)
				.setFirstName("mockedUserFirstName")
				.setLastName("mockedUserLastNAme")
				.setAdmin(false)
				.setEmail("test@test.com").setPassword("test!1234");
		mockedUser2 = new User()
				.setId(2L)
				.setFirstName("mockedUser2FirstName")
				.setLastName("mockedUser2LastNAme")
				.setAdmin(false)
				.setEmail("test@test.com").setPassword("test!1234");
	}

	@Test
	@DisplayName("it should test to create a new session")
	void shouldCreateSession() {
		//Arrange
		Session newSession = new Session();
		newSession.setName("testName");
		newSession.setId(1L);
		newSession.setDescription("sessionDescription");
		newSession.setDate(new Date());
		newSession.setUsers(new ArrayList<>());
		newSession.setTeacher(new Teacher());
		when(sessionRepository.save(newSession)).thenReturn(newSession);

		// Act
		Session createdSession = sessionService.create(newSession);

		// Assert
		assertThat(createdSession).isEqualTo(newSession);
	}

	@Test
	@DisplayName("it should test to delete a session")
	void shouldDeleteSession() {
		//Act
		sessionService.delete(1L);

		//Assert
		verify(sessionRepository).deleteById(1L);
	}

	@Test
	@DisplayName("it should test to find all sessions")
	void shouldFindAllSessions() {
		//Arrange
		when(sessionRepository.findAll()).thenReturn(List.of(mockedSession1, mockedSession2));

		//Assert
		assertThat(sessionService.findAll()).hasSize(2).containsExactly(mockedSession1, mockedSession2);

	}

	@Test
	@DisplayName("it should test to find a session by his id")
	void shouldGetSessionById() {
		//Arrange
		when(sessionRepository.findById(1L)).thenReturn(Optional.of(mockedSession2));

		//Act
		Session findedSession = sessionService.getById(1L);

		//Assert
		assertThat(findedSession).isEqualTo(mockedSession2);
	}

	@Test
	@DisplayName("it should test to update a sission")
	void shouldUpdateSession() {
		//Arrange
		when(sessionRepository.save(mockedSession1)).thenReturn(mockedSession1);

		//Act
		Session updatedSession = sessionService.update(2L, mockedSession1);

		//Assert
		assertThat(updatedSession.getId()).isEqualTo(2L);
	}

	@Test
	@DisplayName("it should test session participate request")
	void shouldParticipateSession() {
		//Arrange
		when(sessionRepository.findById(1L)).thenReturn(Optional.of(mockedSession1));
		when(userRepository.findById(1L)).thenReturn(Optional.of(new User().setId(1L)));

		//Act
		sessionService.participate(1L, 1L);

		//Assert
		verify(sessionRepository).findById(1L);
		verify(userRepository).findById(1L);
		assertThat(mockedSession1.getUsers()).contains(new User().setId(1L));
	}

	@Test
	@DisplayName("it should test if user is not found during participate request")
	void shouldThrowParticipateSessionNotFoundExceptionWhenUserIsNull() {
		//Arrange
		when(sessionRepository.findById(1L)).thenReturn(Optional.of(mockedSession1));
		when(userRepository.findById(1L)).thenReturn(Optional.empty());
		//Assert
		assertThrows(
				NotFoundException.class,
				() -> sessionService.participate(1L, 1L)
		);
	}

	@Test
	@DisplayName("it should test if session is not found during participate request")
	void shouldThrowParticipateSessionNotFoundExceptionWhenSessionIsNull() {
		//Arrange
		User mockedUser = new User()
				.setId(1L)
				.setFirstName("mockedUserFirstName")
				.setLastName("mockedUserLastNAme")
				.setAdmin(true)
				.setEmail("test@test.com").setPassword("test!1234");

		when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
		when(userRepository.findById(1L)).thenReturn(Optional.of(mockedUser));

		//Assert
		assertThrows(
				NotFoundException.class,
				() -> sessionService.participate(1L, 1L)
		);
	}


	@Test
	@DisplayName("it should test if participate bad request exception is triggered")
	void shouldThrowParticipateSessionBadRequestException() {
		//Arrange
		User mockedUser = new User()
				.setId(1L)
				.setFirstName("mockedUserFirstName")
				.setLastName("mockedUserLastNAme")
				.setAdmin(true)
				.setEmail("test@test.com").setPassword("test!1234");
		mockedSession1.getUsers().add(mockedUser);
		when(sessionRepository.findById(1L)).thenReturn(Optional.of(mockedSession1));
		when(userRepository.findById(1L)).thenReturn(Optional.of(mockedUser));
		//Assert
		assertThrows(
				BadRequestException.class,
				() -> sessionService.participate(1L, 1L),
				"BadRequest"
		);
	}

	@Test
	@DisplayName("it should test session no longer participate request")
	void shouldNotLongerParticipateSession() {

		mockedSession1.getUsers().add(mockedUser1);
		mockedSession1.getUsers().add(mockedUser2);


		when(sessionRepository.findById(1L)).thenReturn(Optional.of(mockedSession1));

		//Act
		sessionService.noLongerParticipate(1L, 1L);

		//Assert
		verify(sessionRepository).findById(1L);
		assertThat(mockedSession1.getUsers()).hasSize(1);
		assertEquals(mockedSession1.getUsers(), List.of(mockedUser2));
	}

	@Test
	@DisplayName("it should test if session is not found during no longer participate request")
	void shouldThrowNotLongerParticipateSessionNotFoundException() {
		//Assert
		assertThrows(
				NotFoundException.class,
				() -> sessionService.noLongerParticipate(null, null)
		);
	}

	@Test
	@DisplayName("it should test if no longer participate bad request exception is triggered")
	void shouldThrowNotLongerParticipateSessionBadRequestException() {
		//Arrange
		when(sessionRepository.findById(1L)).thenReturn(Optional.of(mockedSession1));
		//Assert
		assertThrows(
				BadRequestException.class,
				() -> sessionService.noLongerParticipate(1L, 1L)
		);
	}


}