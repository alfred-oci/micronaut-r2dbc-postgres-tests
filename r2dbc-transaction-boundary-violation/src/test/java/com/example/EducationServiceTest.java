package com.example;

import com.example.education.EducationService;
import com.example.education.Student;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
class EducationServiceTest {
    @Container
    private static final PostgreSQLContainer DB_CONTAINER = new PostgreSQLContainer("postgres:12");

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    EducationService educationService;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void testPlacement() {
        List<String> studentNames =
                List.of("Larry", "Moe", "Curly");

        List<Student> students = educationService.placeStudentsAtSchool(
                "Stooges Academy", studentNames).collectList().block();

        assertNotNull(students);
        assertEquals(3, students.size());
        assertEquals(studentNames.get(0), students.get(0));
        assertEquals(studentNames.get(1), students.get(1));
        assertEquals(studentNames.get(2), students.get(2));

    }
}
