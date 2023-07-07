package com.projects.phone_contacts.mapper;

import com.projects.phone_contacts.model.User;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserDetailsMapper implements Mapper<User, org.springframework.security.core.userdetails.User> {
    @Override
    public org.springframework.security.core.userdetails.User map(User from) {
        return new org.springframework.security.core.userdetails.User(
                from.getUsername(),
                from.getPassword(),
                Collections.singleton(from.getRole())
        );
    }
}
