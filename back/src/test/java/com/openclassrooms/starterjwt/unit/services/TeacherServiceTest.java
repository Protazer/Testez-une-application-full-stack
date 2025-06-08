package com.openclassrooms.starterjwt.unit.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher mockedTeacher1;

    private Teacher mockedTeacher2;

    @BeforeEach
    public void setup() {
        mockedTeacher1 = new Teacher()
                .setId(1L)
                .setFirstName("teacherFirstName1")
                .setLastName("teacherLastName1");


        mockedTeacher2 = new Teacher()
                .setId(2L)
                .setFirstName("teacherFirstName2")
                .setLastName("teacherLastName2");
    }

    @Test
    void shouldFindAllTeachers() {
        //Arrange
        when(teacherRepository.findAll()).thenReturn(List.of(mockedTeacher1, mockedTeacher2));
        //Act
        teacherService.findAll();
        //Assert
        assertThat(teacherService.findAll()).hasSize(2).containsExactly(mockedTeacher1, mockedTeacher2);
    }

    @Test
    void shouldFindTeacherById() {
        //Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(mockedTeacher1));

        //Act
        Teacher findedTeacher = teacherService.findById(1L);

        //Assert
        assertThat(findedTeacher).isEqualTo(mockedTeacher1);
    }
}