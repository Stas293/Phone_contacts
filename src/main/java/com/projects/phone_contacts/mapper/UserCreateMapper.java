package com.projects.phone_contacts.mapper;

import com.projects.phone_contacts.dto.UserCreateDTO;
import com.projects.phone_contacts.enums.Roles;
import com.projects.phone_contacts.model.User;
import com.projects.phone_contacts.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements com.projects.phone_contacts.mapper.Mapper<UserCreateDTO, User> {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User map(UserCreateDTO from) {
        return User.builder()
                .username(from.login())
                .password(passwordEncoder.encode(from.password()))
                .role(roleRepository.findByCode(Roles.USER.code()).orElseThrow())
                .build();
    }
}

