package com.projects.phone_contacts.mapper;

import com.projects.phone_contacts.dto.ContactDeleteDto;
import com.projects.phone_contacts.model.Contact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContactDeleteMapper implements Mapper<ContactDeleteDto, Contact> {
    @Override
    public Contact map(ContactDeleteDto from) {
        String[] name = from.name().split(" ");
        return Contact.builder()
                .firstName(name[0])
                .middleName(name.length > 2 ? name[1] : null)
                .lastName(name.length > 2 ? name[2] : name[1])
                .build();
    }
}
