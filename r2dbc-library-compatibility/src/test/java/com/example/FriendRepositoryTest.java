package com.example;

import com.example.friends.Friend;
import com.example.friends.FriendRepository;
import io.micronaut.data.r2dbc.operations.EnableR2dbcV1Compatibility;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@Testcontainers
class FriendRepositoryTest {
    @Container
    private static final PostgreSQLContainer DB_CONTAINER = new PostgreSQLContainer("postgres:12");

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    FriendRepository repository;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

    @Test
    void testSaveOne() {
        Friend result =
                repository.save(new Friend("Shemp")).block();

        assertNotNull(result);
        assertEquals("Shemp", result.getName());
    }

    @Test
    void testSaveAll() {
        List<Friend> result = repository.saveAll(
                List.of(new Friend("Larry"),
                        new Friend("Moe"),
                        new Friend("Curly"))).collectList().block();

        assert result != null;
        assertEquals(3, result.size());
        assertEquals("Larry", result.get(0).getName());
        assertEquals("Moe", result.get(1).getName());
        assertEquals("Curly", result.get(2).getName());
    }

    @Test
    void testSaveThenDeleteOne_WithOutOverRide() {
        EnableR2dbcV1Compatibility.ENABLED = false;

        Friend result =
                repository.save(new Friend("Shemp")).block();

        assertNotNull(result);
        assertEquals("Shemp", result.getName());

        try {
            repository.deleteById(result.getId()).block();
            fail("No ClassCastException Called!");
        } catch(ClassCastException expected) {
            assertEquals(
                    "class java.lang.Long cannot be cast to class java.lang.Integer " +
                             "(java.lang.Long and java.lang.Integer are in module java.base of loader 'bootstrap')",
                    expected.getMessage());
        }
    }

    @Test
    void testSaveThenDeleteOne_WithOverRide() {
        EnableR2dbcV1Compatibility.ENABLED = true;

        Friend result =
                repository.save(new Friend("Shemp")).block();

        assertNotNull(result);
        assertEquals("Shemp", result.getName());

        Long result2 = repository.deleteById(result.getId()).block();

        assertNotNull(result2);
        assertEquals(1L, result2);
    }
}