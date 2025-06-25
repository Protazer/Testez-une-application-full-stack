package com.openclassrooms.starterjwt.unit.repository;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("TeacherRepository unit tests")
class TeacherRepositoryTest {

	@Autowired
	private TeacherRepository teacherRepository;

	@Test
	@DisplayName("it should test to get all teachers")
	void shouldGetAllTeachers() {
		// Act
		List<Teacher> teachers = teacherRepository.findAll();
		//Assert
		assertEquals(2, teachers.size());
	}

	@Test
	@DisplayName("it should test to find a teacher by his id")
	void shouldGetTeacherById() {
		// Act
		Teacher teacher = teacherRepository.findById(1L).orElse(null);
		//Assert
		assertNotNull(teacher);
		assertEquals("John", teacher.getFirstName());
	}

	@Test
	@DisplayName("it should tests to create a teacher")
	void shouldSaveTeacher() {
		//Arrange
		Teacher newTeacher = new Teacher();
		newTeacher.setId(1L);
		newTeacher.setFirstName("john");
		newTeacher.setLastName("Doe");

		// Act
		Teacher savedTeacher = teacherRepository.save(newTeacher);

		//Assert
		assertNotNull(savedTeacher.getId());
		assertEquals(newTeacher.getFirstName(), savedTeacher.getFirstName());
	}

	@Test
	@DisplayName("it should test to update a teacher")
	void shouldUpdateTeacher() {
		// Act
		Optional<Teacher> foundedTeacher = teacherRepository.findById(1L);
		if (foundedTeacher.isPresent()) {
			Teacher updatedTeacher = foundedTeacher.get().setFirstName("teacher1");
			teacherRepository.save(updatedTeacher);
			//Assert
			assertEquals("teacher1", updatedTeacher.getFirstName());
		}
	}

	@Test
	@DisplayName("it should tests to delete a teacher")
	void shouldDeleteTeacher() {
		// Act
		teacherRepository.deleteById(1L);
		Optional<Teacher> teacher = teacherRepository.findById(1L);
		//Assert
		assertFalse(teacher.isPresent());

	}
}