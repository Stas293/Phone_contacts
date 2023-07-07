package com.projects.phone_contacts.integration.repository;


import com.projects.phone_contacts.integration.IntegrationTestBase;
import com.projects.phone_contacts.integration.annotation.IT;
import com.projects.phone_contacts.model.User;
import com.projects.phone_contacts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@IT
@RequiredArgsConstructor
class UserRepositoryTest extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void findByUsername() {
        List<User> users = userRepository.findAll();
        Assertions.assertEquals(2, users.size());
        User user = users.get(0);
        Assertions.assertEquals("john_doe", user.getUsername());
        Assertions.assertEquals("ROLE_ADMIN", user.getRole().getCode());
    }

    @Test
    void findByUsername2() {
        List<User> users = userRepository.findAll();
        Assertions.assertEquals(2, users.size());
        User user = users.get(1);
        Assertions.assertEquals("jane_smith", user.getUsername());
        Assertions.assertEquals("ROLE_USER", user.getRole().getCode());
    }

    @Test
    void finById() {
        User user = userRepository.findById(1L).orElseThrow();
        Assertions.assertEquals("john_doe", user.getUsername());
        Assertions.assertEquals("ROLE_ADMIN", user.getRole().getCode());
    }

    @Test
    void finById2() {
        User user = userRepository.findById(2L).orElseThrow();
        Assertions.assertEquals("jane_smith", user.getUsername());
        Assertions.assertEquals("ROLE_USER", user.getRole().getCode());
    }

    @Test
    void update() {
        User user = userRepository.findById(1L).orElseThrow();
        user.setUsername("john_doe_updated");
        userRepository.save(user);
        User userUpdated = userRepository.findById(1L).orElseThrow();
        Assertions.assertEquals("john_doe_updated", userUpdated.getUsername());
    }

    @Test
    void delete() {
        User user = userRepository.findById(1L).orElseThrow();
        userRepository.delete(user);
        List<User> users = userRepository.findAll();
        Assertions.assertEquals(1, users.size());
    }

    @Test
    void save() {
        User user = User.builder()
                .username("test")
                .password("test")
                .build();
        userRepository.save(user);
        List<User> users = userRepository.findAll();
        Assertions.assertEquals(3, users.size());
    }

    @Test
    void count() {
        long count = userRepository.count();
        Assertions.assertEquals(2, count);
    }

    @Test
    void existsById() {
        Assertions.assertTrue(userRepository.existsById(1L));
    }

    @Test
    void existsById2() {
        Assertions.assertTrue(userRepository.existsById(2L));
    }

    @Test
    void existsById3() {
        Assertions.assertFalse(userRepository.existsById(3L));
    }

    @Test
    void findAll() {
        List<User> users = userRepository.findAll();
        Assertions.assertEquals(2, users.size());
    }

    @Test
    void findAllById() {
        List<User> users = userRepository.findAllById(List.of(1L, 2L));
        Assertions.assertEquals(2, users.size());
    }

    @Test
    void findAllById2() {
        List<User> users = userRepository.findAllById(List.of(1L, 2L, 3L));
        Assertions.assertEquals(2, users.size());
    }
}