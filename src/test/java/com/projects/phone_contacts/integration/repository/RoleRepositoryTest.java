package com.projects.phone_contacts.integration.repository;

import com.projects.phone_contacts.integration.IntegrationTestBase;
import com.projects.phone_contacts.integration.annotation.IT;
import com.projects.phone_contacts.model.Role;
import com.projects.phone_contacts.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@IT
@RequiredArgsConstructor
class RoleRepositoryTest extends IntegrationTestBase {
    private final RoleRepository roleRepository;

    @Test
    void findAll() {
        List<Role> roles = roleRepository.findAll();
        Assertions.assertEquals(2, roles.size());
    }

    @Test
    void findByCode() {
        Role roleAdmin = roleRepository.findByCode("ROLE_ADMIN").orElseThrow();
        Assertions.assertEquals("Admin", roleAdmin.getName());
        Role roleUser = roleRepository.findByCode("ROLE_USER").orElseThrow();
        Assertions.assertEquals("User", roleUser.getName());
    }

    @Test
    void findByCodeNotFound() {
        Assertions.assertFalse(roleRepository.findByCode("ROLE_NOT_FOUND").isPresent());
    }

    @Test
    void findById() {
        Role roleAdmin = roleRepository.findById(1L).orElseThrow();
        Assertions.assertEquals("Admin", roleAdmin.getName());
        Role roleUser = roleRepository.findById(2L).orElseThrow();
        Assertions.assertEquals("User", roleUser.getName());
    }

    @Test
    void findByIdNotFound() {
        Assertions.assertFalse(roleRepository.findById(3L).isPresent());
    }

    @Test
    void save() {
        Role role = new Role();
        role.setCode("ROLE_TEST");
        role.setName("Test");
        Role savedRole = roleRepository.save(role);
        Assertions.assertEquals("Test", savedRole.getName());
    }

    @Test
    void delete() {
        Role role = new Role();
        role.setCode("ROLE_TEST");
        role.setName("Test");
        Role savedRole = roleRepository.save(role);
        Assertions.assertEquals("Test", savedRole.getName());
        roleRepository.delete(savedRole);
        Assertions.assertFalse(roleRepository.findById(savedRole.getId()).isPresent());
    }

    @Test
    void deleteById() {
        Role role = new Role();
        role.setCode("ROLE_TEST");
        role.setName("Test");
        Role savedRole = roleRepository.save(role);
        Assertions.assertEquals("Test", savedRole.getName());
        roleRepository.deleteById(savedRole.getId());
        Assertions.assertFalse(roleRepository.findById(savedRole.getId()).isPresent());
    }

    @Test
    void deleteAll() {
        Role role = new Role();
        role.setCode("ROLE_TEST");
        role.setName("Test");
        Role savedRole = roleRepository.save(role);
        Assertions.assertEquals("Test", savedRole.getName());
        roleRepository.deleteAll();
        Assertions.assertFalse(roleRepository.findById(savedRole.getId()).isPresent());
    }

    @Test
    void update() {
        Role role = new Role();
        role.setCode("ROLE_TEST");
        role.setName("Test");
        Role savedRole = roleRepository.save(role);
        Assertions.assertEquals("Test", savedRole.getName());
        savedRole.setName("Test2");
        Role updatedRole = roleRepository.save(savedRole);
        Assertions.assertEquals("Test2", updatedRole.getName());
    }

    @Test
    void updateAll() {
        Role role = new Role();
        role.setCode("ROLE_TEST");
        role.setName("Test");
        Role savedRole = roleRepository.save(role);
        Assertions.assertEquals("Test", savedRole.getName());
        savedRole.setName("Test2");
        Role updatedRole = roleRepository.save(savedRole);
        Assertions.assertEquals("Test2", updatedRole.getName());
    }

    @Test
    void count() {
        Role role = new Role();
        role.setCode("ROLE_TEST");
        role.setName("Test");
        Role savedRole = roleRepository.save(role);
        Assertions.assertEquals("Test", savedRole.getName());
        Assertions.assertEquals(3, roleRepository.count());
    }

    @Test
    void existsById() {
        Role role = new Role();
        role.setCode("ROLE_TEST");
        role.setName("Test");
        Role savedRole = roleRepository.save(role);
        Assertions.assertEquals("Test", savedRole.getName());
        Assertions.assertTrue(roleRepository.existsById(savedRole.getId()));
    }
}
