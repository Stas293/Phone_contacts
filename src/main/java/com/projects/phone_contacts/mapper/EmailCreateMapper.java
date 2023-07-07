package com.projects.phone_contacts.mapper;

import com.projects.phone_contacts.dto.EmailCreateUpdateDto;
import com.projects.phone_contacts.model.Email;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EmailCreateMapper extends com.projects.phone_contacts.mapper.Mapper<EmailCreateUpdateDto, Email> {
    @Mapping(target = "username", source = "emailCreateUpdateDto", qualifiedByName = "mapUsername")
    @Mapping(target = "domain", source = "emailCreateUpdateDto", qualifiedByName = "mapDomain")
    Email map(EmailCreateUpdateDto emailCreateUpdateDto);

    @Named("mapUsername")
    default String mapUsername(EmailCreateUpdateDto email) {
        return email.getEmail().split("@")[0];
    }

    @Named("mapDomain")
    default String mapDomain(EmailCreateUpdateDto email) {
        return email.getEmail().split("@")[1];
    }
}


