package com.projects.phone_contacts.mapper;

import com.projects.phone_contacts.model.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactNameMapper implements Mapper<String, Contact> {
    @Override
    public Contact map(String from) {
        String[] names = from.split(" ");
        String firstName = names[0];
        String middleName = names.length > 2 ? names[1] : null;
        String lastName = names.length > 2 ? names[2] : names[1];
        return Contact.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .build();
    }
}
