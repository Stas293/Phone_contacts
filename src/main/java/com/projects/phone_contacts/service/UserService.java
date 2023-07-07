package com.projects.phone_contacts.service;

import com.projects.phone_contacts.dto.JwtRequestDTO;
import com.projects.phone_contacts.dto.JwtResponseDTO;
import com.projects.phone_contacts.dto.UserCreateDTO;
import com.projects.phone_contacts.dto.UserDTO;
import com.projects.phone_contacts.mapper.UserCreateMapper;
import com.projects.phone_contacts.mapper.UserReadMapper;
import com.projects.phone_contacts.repository.UserRepository;
import com.projects.phone_contacts.security.jwt.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserRepository userRepository;
    private final UserCreateMapper userCreateEditMapper;
    private final UserReadMapper userReadMapper;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Transactional
    public UserDTO register(UserCreateDTO registrationDTO) {
        return Optional.of(registrationDTO)
                .map(userCreateEditMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    public JwtResponseDTO auth(JwtRequestDTO jwtRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                jwtRequestDto.login(), jwtRequestDto.password());
        authenticationManager.authenticate(authenticationToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequestDto.login());
        String jwt = jwtUtils.generateToken(
                jwtRequestDto.login(),
                userDetails.getAuthorities());
        return new JwtResponseDTO(jwt);
    }
}
