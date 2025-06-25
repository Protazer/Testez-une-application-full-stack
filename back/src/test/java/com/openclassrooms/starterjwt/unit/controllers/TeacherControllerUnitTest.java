package com.openclassrooms.starterjwt.unit.controllers;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeacherController unit test")
class TeacherControllerUnitTest {

	@Mock
	private TeacherMapper teacherMapper;
	@Mock
	private TeacherService teacherService;

	@InjectMocks
	private TeacherController teacherController;

	@Test
	@DisplayName("it should test to find a teacher by his id")
	void shouldFindTeacherById() {
		//Arrange
		TeacherDto teacherDto = new TeacherDto();
		Teacher teacher = new Teacher().setId(1L);
		when(teacherService.findById(1L)).thenReturn(teacher);
		when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);
		//Act
		ResponseEntity<?> response = teacherController.findById("1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(teacherDto);
	}

	@Test
	@DisplayName("it should test if teacher is not found")
	void shouldReturnNotFoundWhenFindTeacherById() {
		//Arrange
		when(teacherService.findById(1L)).thenReturn(null);
		//Act
		ResponseEntity<?> response = teacherController.findById("1");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DisplayName("it should test if id is bad during find teacher by id")
	void shouldReturnBadRequestWhenFindTeacherById() {
		//Act
		ResponseEntity<?> response = teacherController.findById("abc");
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	@DisplayName("it should test to find all teachers")
	void shouldFindAllTeachers() {
		//Arrange
		List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());
		List<TeacherDto> teachersDtos = Arrays.asList(new TeacherDto(), new TeacherDto());
		when(teacherService.findAll()).thenReturn(teachers);
		when(teacherMapper.toDto(teachers)).thenReturn(teachersDtos);
		//Act
		ResponseEntity<?> response = teacherController.findAll();
		//Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(teachersDtos);
	}
}