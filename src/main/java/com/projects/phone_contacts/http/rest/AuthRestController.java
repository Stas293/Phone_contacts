package com.projects.phone_contacts.http.rest;

import com.projects.phone_contacts.dto.JwtRequestDTO;
import com.projects.phone_contacts.dto.JwtResponseDTO;
import com.projects.phone_contacts.dto.UserCreateDTO;
import com.projects.phone_contacts.dto.UserDTO;
import com.projects.phone_contacts.service.UserService;
import com.projects.phone_contacts.utility.ExceptionParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthRestController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO register(@Validated @RequestBody UserCreateDTO registrationDTO,
                            BindingResult errors) {
        if (errors.hasErrors()) {
            ExceptionParser.parseValidation(errors);
        }
        return userService.register(registrationDTO);
    }

    @PostMapping("/auth")
    public JwtResponseDTO auth(@RequestBody JwtRequestDTO jwtRequestDto) {
        return userService.auth(jwtRequestDto);
    }
}
