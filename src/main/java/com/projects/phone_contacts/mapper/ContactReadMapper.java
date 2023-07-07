package com.projects.phone_contacts.mapper;

import com.projects.phone_contacts.dto.ContactReadDto;
import com.projects.phone_contacts.model.Contact;
import org.mapstruct.*;

@org.mapstruct.Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContactReadMapper extends Mapper<Contact, ContactReadDto> {
    @Mapping(target = "name", source = "contact", qualifiedByName = "mapName")
    @Mapping(target = "emails", expression = "java(contact.getEmails().stream().map(email -> email.getUsername() + \"@\" + email.getDomain()).toList())")
    @Mapping(target = "phones", expression = "java(contact.getPhones().stream().map(phone -> phone.getCountryCode() + phone.getAreaCode() + phone.getNumber()).toList())")
    ContactReadDto map(Contact contact);

    @Named("mapName")
    default String mapName(Contact contact) {
        return contact.getMiddleName() == null ?
                String.format("%s %s", contact.getFirstName(), contact.getLastName()) :
                String.format("%s %s %s", contact.getFirstName(), contact.getMiddleName(), contact.getLastName());
    }

    @AfterMapping
    default void linkEmails(@MappingTarget Contact contact) {
        contact.getEmails().forEach(email -> email.setContact(contact));
    }

    @AfterMapping
    default void linkPhones(@MappingTarget Contact contact) {
        contact.getPhones().forEach(phone -> phone.setContact(contact));
    }
}