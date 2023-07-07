package com.projects.phone_contacts.integration.http.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.phone_contacts.dto.JwtRequestDTO;
import com.projects.phone_contacts.dto.UserCreateDTO;
import com.projects.phone_contacts.exceptions.ValidationException;
import com.projects.phone_contacts.integration.IntegrationTestBase;
import com.projects.phone_contacts.integration.annotation.IT;
import com.projects.phone_contacts.mapper.UserCreateMapper;
import com.projects.phone_contacts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.assertTrue;

@IT
@RequiredArgsConstructor
@AutoConfigureMockMvc
class AuthRestControllerTest extends IntegrationTestBase {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserCreateMapper userMapper;

    @Test
    void testRegister_Success() throws Exception {
        // Prepare test data
        UserCreateDTO userCreateDTO = new UserCreateDTO("Testuser", "password123");


        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(userCreateDTO.login()));
    }

    @Test
    void testRegister_ValidationErrors() throws Exception {
        // Prepare test data with validation errors
        UserCreateDTO userCreateDTO = new UserCreateDTO("", "password123");

        // Perform the request and expect a ConstraintViolationException
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException));
    }

    @Test
    void testAuth_Success() throws Exception {
        // Prepare test data
        JwtRequestDTO jwtRequestDTO = new JwtRequestDTO("Testuser", "password123");
        UserCreateDTO userCreateDTO = new UserCreateDTO("Testuser", "password123");

        userRepository.save(userMapper.map(userCreateDTO));

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());
    }
}
