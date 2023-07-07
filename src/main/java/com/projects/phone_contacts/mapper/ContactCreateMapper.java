package com.projects.phone_contacts.mapper;

import com.projects.phone_contacts.dto.ContactCreateUpdateDto;
import com.projects.phone_contacts.model.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContactCreateMapper implements Mapper<ContactCreateUpdateDto, Contact> {
    private final EmailCreateMapper emailCreateMapper;
    private final PhoneNumberMapper.PhoneMapper phoneNumberMapper;

    @Override
    public Contact map(ContactCreateUpdateDto from) {
        String[] name = from.name().split(" ");
        return Contact.builder()
                .firstName(name[0])
                .middleName(name.length > 2 ? name[1] : null)
                .lastName(name.length > 2 ? name[2] : name[1])
                .emails(emailCreateMapper.map(from.emails()))
                .phones(phoneNumberMapper.map(from.phones()))
                .build();
    }

    @Override
    public Contact map(ContactCreateUpdateDto from, Contact to) {
        Contact mapped = map(from);
        List<Field> nonNullFields = Arrays.stream(Contact.class.getDeclaredFields())
                .filter(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(mapped) != null;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                        field.setAccessible(false);
                    }
                    return false;
                })
                .toList();
        nonNullFields.forEach(field -> {
            field.setAccessible(true);
            try {
                field.set(to, field.get(mapped));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                field.setAccessible(false);
            }
        });
        return to;
    }
}