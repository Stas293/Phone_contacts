package com.projects.phone_contacts.unit.mapper;

import com.projects.phone_contacts.dto.ContactCreateUpdateDto;
import com.projects.phone_contacts.dto.EmailCreateUpdateDto;
import com.projects.phone_contacts.dto.PhoneCreateUpdateDto;
import com.projects.phone_contacts.mapper.ContactCreateMapper;
import com.projects.phone_contacts.mapper.EmailCreateMapper;
import com.projects.phone_contacts.mapper.PhoneNumberMapper;
import com.projects.phone_contacts.model.Contact;
import com.projects.phone_contacts.model.Email;
import com.projects.phone_contacts.model.Phone;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ContactCreateMapperTest {
    @Mock
    private EmailCreateMapper emailCreateMapper;
    @Mock
    private PhoneNumberMapper.PhoneMapper phoneNumberMapper;
    @InjectMocks
    private ContactCreateMapper contactCreateMapper;

    private static Contact getContact() {
        return Contact.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .middleName("MiddleName")
                .phones(List.of(Phone.builder()
                        .countryCode("+380")
                        .areaCode("66")
                        .number("1234567")
                        .build()))
                .emails(List.of(Email.builder()
                                .username("username")
                                .domain("domain")
                                .build(),
                        Email.builder()
                                .username("username2")
                                .domain("domain2")
                                .build()))
                .build();
    }

    private static ContactCreateUpdateDto getContactCreateUpdateDto() {
        return ContactCreateUpdateDto.builder()
                .name("FirstName MiddleName LastName")
                .phones(Set.of(PhoneCreateUpdateDto.builder()
                        .number("+380661234567")
                        .build()))
                .emails(Set.of(EmailCreateUpdateDto.builder()
                                .email("username@domain")
                                .build(),
                        EmailCreateUpdateDto.builder()
                                .email("username2@domain2")
                                .build()
                ))
                .build();
    }

    @Test
    void map() {
        doReturn(List.of(Email.builder()
                        .username("username")
                        .domain("domain")
                        .build(),
                Email.builder()
                        .id(2L)
                        .username("username2")
                        .domain("domain2")
                        .build()))
                .when(emailCreateMapper).map(Set.of(EmailCreateUpdateDto.builder()
                                .email("username@domain")
                                .build(),
                        EmailCreateUpdateDto.builder()
                                .email("username2@domain2")
                                .build()));
        doReturn(List.of(Phone.builder()
                .countryCode("+380")
                .areaCode("66")
                .number("1234567")
                .build()))
                .when(phoneNumberMapper).map(Set.of(PhoneCreateUpdateDto.builder()
                        .number("+380661234567")
                        .build()));
        ContactCreateUpdateDto contactCreateUpdateDto = getContactCreateUpdateDto();

        Contact contact = getContact();

        Contact mappedContact = contactCreateMapper.map(contactCreateUpdateDto);
        Assertions.assertEquals(contact.getFirstName(), mappedContact.getFirstName());
        Assertions.assertEquals(contact.getMiddleName(), mappedContact.getMiddleName());
        Assertions.assertEquals(contact.getLastName(), mappedContact.getLastName());
        IntStream.range(0, contact.getPhones().size()).forEach(i -> {
            Assertions.assertEquals(contact.getPhones().get(i).getCountryCode(), mappedContact.getPhones().get(i).getCountryCode());
            Assertions.assertEquals(contact.getPhones().get(i).getAreaCode(), mappedContact.getPhones().get(i).getAreaCode());
            Assertions.assertEquals(contact.getPhones().get(i).getNumber(), mappedContact.getPhones().get(i).getNumber());
        });
        IntStream.range(0, contact.getEmails().size()).forEach(i -> {
            Assertions.assertEquals(contact.getEmails().get(i).getUsername(), mappedContact.getEmails().get(i).getUsername());
            Assertions.assertEquals(contact.getEmails().get(i).getDomain(), mappedContact.getEmails().get(i).getDomain());
        });

    }

    @Test
    void testMap() {
        Contact contact = getContact();
        getContactCreateUpdateDto();
        ContactCreateUpdateDto contactCreateUpdateDto;
        contactCreateUpdateDto = ContactCreateUpdateDto.builder()
                .name("FirstName1 MiddleName1 LastName1")
                .phones(Set.of(PhoneCreateUpdateDto.builder()
                        .number("+380661234568")
                        .build()))
                .emails(Set.of(EmailCreateUpdateDto.builder()
                                .email("username1@domain1")
                                .build(),
                        EmailCreateUpdateDto.builder()
                                .email("username2@domain2")
                                .build()
                ))
                .build();

        doReturn(List.of(Email.builder()
                        .username("username1")
                        .domain("domain1")
                        .build(),
                Email.builder()
                        .username("username2")
                        .domain("domain2")
                        .build()))
                .when(emailCreateMapper).map(Set.of(EmailCreateUpdateDto.builder()
                                .email("username1@domain1")
                                .build(),
                        EmailCreateUpdateDto.builder()
                                .email("username2@domain2")
                                .build()));
        doReturn(List.of(Phone.builder()
                .countryCode("+380")
                .areaCode("66")
                .number("1234568")
                .build()))
                .when(phoneNumberMapper).map(Set.of(PhoneCreateUpdateDto.builder()
                        .number("+380661234568")
                        .build()));

        contact = contactCreateMapper.map(contactCreateUpdateDto, contact);
        Assertions.assertEquals(contact.getFirstName(), "FirstName1");
        Assertions.assertEquals(contact.getMiddleName(), "MiddleName1");
        Assertions.assertEquals(contact.getLastName(), "LastName1");

        Assertions.assertEquals(contact.getPhones().get(0).getCountryCode(), "+380");
        Assertions.assertEquals(contact.getPhones().get(0).getAreaCode(), "66");
        Assertions.assertEquals(contact.getPhones().get(0).getNumber(), "1234568");

        Assertions.assertEquals(contact.getEmails().get(0).getUsername(), "username1");
        Assertions.assertEquals(contact.getEmails().get(0).getDomain(), "domain1");

        Assertions.assertEquals(contact.getEmails().get(1).getUsername(), "username2");
        Assertions.assertEquals(contact.getEmails().get(1).getDomain(), "domain2");

    }

    @Test
    void testMap1() {
        List<ContactCreateUpdateDto> contactCreateUpdateDtos = List.of(getContactCreateUpdateDto());
        List<Contact> mappedContacts = contactCreateMapper.map(contactCreateUpdateDtos);
        Assertions.assertEquals(mappedContacts.size(), 1);
    }

    @Test
    void testMap2() {
        Set<ContactCreateUpdateDto> contactCreateUpdateDtos = Set.of(getContactCreateUpdateDto());
        List<Contact> mappedContacts = contactCreateMapper.map(contactCreateUpdateDtos);
        Assertions.assertEquals(mappedContacts.size(), 1);
    }
}