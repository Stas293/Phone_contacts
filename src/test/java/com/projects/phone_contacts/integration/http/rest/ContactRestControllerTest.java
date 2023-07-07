package com.projects.phone_contacts.integration.http.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.phone_contacts.dto.*;
import com.projects.phone_contacts.enums.Roles;
import com.projects.phone_contacts.integration.IntegrationTestBase;
import com.projects.phone_contacts.integration.annotation.IT;
import com.projects.phone_contacts.repository.RoleRepository;
import com.projects.phone_contacts.security.jwt.JWTUtils;
import com.projects.phone_contacts.service.ContactService;
import com.projects.phone_contacts.validation.PhoneExistingValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@IT
@RequiredArgsConstructor
@AutoConfigureMockMvc
class ContactRestControllerTest extends IntegrationTestBase {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JWTUtils jwtUtils;
    @MockBean
    private ContactService contactService;
    @MockBean
    private PhoneExistingValidator phoneExistingValidator;
    private final RoleRepository roleRepository;
    private List<ContactReadDto> contactReadDtoList;

    @BeforeEach
    public void setup() {
        // Prepare sample data for the tests
        ContactReadDto contactReadDto1 = new ContactReadDto("John Doe", List.of("john@example.com"), List.of("123456789"));
        ContactReadDto contactReadDto2 = new ContactReadDto("Jane Smith", List.of("jane@example.com"), List.of("987654321"));
        contactReadDtoList = Arrays.asList(contactReadDto1, contactReadDto2);
    }


    @Test
    void testCreate_WithValidData_ReturnsCreated() throws Exception {
        // Mock the ContactService behavior
        String jwt = getJwt();
        ContactCreateUpdateDto createDto = new ContactCreateUpdateDto(
                "John Doe",
                Set.of(new EmailCreateUpdateDto("john@example.com")),
                Set.of(new PhoneCreateUpdateDto("123456789"))
        );
        ContactReadDto createdDto = new ContactReadDto("John Doe", List.of("john@example.com"), List.of("123456789"));
        when(contactService.create(ArgumentMatchers.eq(createDto), any())).thenReturn(createdDto);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwt)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(createdDto)));

        // Verify that the ContactService method was called
        verify(contactService).create(ArgumentMatchers.eq(createDto), any());
    }

    @Test
    void testDelete_WithValidData_ReturnsNoContent() throws Exception {
        String jwt = getJwt();

        ContactDeleteDto deleteDto = new ContactDeleteDto("John Doe");
        // Perform the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/contacts")
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteDto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify that the ContactService method was called
        verify(contactService).delete(any(ContactDeleteDto.class));
    }

    @Test
    void testDeleteById_WithValidId_ReturnsNoContent() throws Exception {
        String jwt = getJwt();
        // Perform the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/contacts/{id}", 1L)
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify that the ContactService method was called
        verify(contactService).deleteById(1L);
    }

    @Test
    void testUpdate_WithValidData_ReturnsOk() throws Exception {
        String jwt = getJwt();
        // Mock the ContactService behavior
        ContactCreateUpdateDto updateDto = new ContactCreateUpdateDto("John Doe", Set.of(new EmailCreateUpdateDto("john@example.com")),
                Set.of(new PhoneCreateUpdateDto("+380989145791")));
        ContactReadDto updatedDto = new ContactReadDto("John Doe", List.of("john@example.com"), List.of("+380989145791"));
        when(contactService.update(eq(updateDto), any())).thenReturn(updatedDto);

        // Perform the PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwt)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(updatedDto)));

        // Verify that the ContactService method was called
        verify(contactService).update(eq(updateDto), any());
    }

    private String getJwt() {
        return jwtUtils.generateToken(
                "user",
                Collections.singletonList(roleRepository.findByCode(Roles.USER.code()).orElseThrow())
        );
    }

    @Test
    void testGetById_WithValidId_ReturnsOk() throws Exception {
        // Mock the ContactService behavior
        String jwt = getJwt();
        ContactReadDto contactReadDto = new ContactReadDto( "John Doe", Collections.singletonList("john@example.com"), Collections.singletonList("123456789"));
        when(contactService.getById(1L)).thenReturn(contactReadDto);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/contacts/{id}", 1L)
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(contactReadDto)));

        // Verify that the ContactService method was called
        verify(contactService).getById(1L);
    }

    @Test
    void testGetAll_ReturnsOk() throws Exception {
        String jwt = getJwt();
        // Mock the ContactService behavior
        when(contactService.getAll()).thenReturn(contactReadDtoList);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/contacts")
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(contactReadDtoList)));

        // Verify that the ContactService method was called
        verify(contactService).getAll();
    }

}
