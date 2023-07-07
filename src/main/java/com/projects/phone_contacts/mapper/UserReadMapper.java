package com.projects.phone_contacts.mapper;

import com.projects.phone_contacts.dto.UserDTO;
import com.projects.phone_contacts.model.User;
import org.mapstruct.Mapping;

@org.mapstruct.Mapper(componentModel = "spring")
public interface UserReadMapper extends Mapper<User, UserDTO> {
    @Mapping(target = "login", source = "username")
    UserDTO map(User user);
}
