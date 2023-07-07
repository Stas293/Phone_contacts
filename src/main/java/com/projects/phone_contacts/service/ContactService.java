package com.projects.phone_contacts.service;

import com.projects.phone_contacts.dto.ContactCreateUpdateDto;
import com.projects.phone_contacts.dto.ContactDeleteDto;
import com.projects.phone_contacts.dto.ContactReadDto;
import com.projects.phone_contacts.exceptions.ContactDeleteException;
import com.projects.phone_contacts.exceptions.ContactNotFoundException;
import com.projects.phone_contacts.mapper.Mapper;
import com.projects.phone_contacts.model.Contact;
import com.projects.phone_contacts.model.User;
import com.projects.phone_contacts.repository.ContactRepository;
import com.projects.phone_contacts.repository.UserRepository;
import com.projects.phone_contacts.utility.ExceptionParser;
import com.projects.phone_contacts.validation.ContactUniqueCreateValidator;
import com.projects.phone_contacts.validation.ContactUniqueUpdateValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContactService {
    private final ContactRepository contactRepository;
    private final Mapper<ContactCreateUpdateDto, Contact> contactCreateMapper;
    private final Mapper<ContactDeleteDto, Contact> contactDeleteMapper;
    private final Mapper<Contact, ContactReadDto> contactReadMapper;
    private final UserRepository userRepository;
    private final ContactUniqueCreateValidator contactUniqueCreateValidator;
    private final ContactUniqueUpdateValidator contactUniqueUpdateValidator;

    @Transactional
    public ContactReadDto create(ContactCreateUpdateDto contactCreateUpdateDto, BindingResult errors) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        contactUniqueCreateValidator.validate(contactCreateUpdateDto, errors);
        if (errors.hasErrors()) {
            ExceptionParser.parseValidation(errors);
        }
        Contact contact = contactCreateMapper.map(contactCreateUpdateDto);
        contact.setUser(userRepository.findByUsername(name).orElseThrow());
        Contact savedContact = contactRepository.save(contact);
        log.info("Contact created: {}", savedContact);
        return contactReadMapper.map(savedContact);
    }

    @Transactional
    public void delete(ContactDeleteDto contactDeleteDto) {
        Contact contact = contactDeleteMapper.map(contactDeleteDto);
        User user = userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow();
        Optional<Contact> optionalContact = getContact(contact, user);
        if (optionalContact.isEmpty()) {
            throw new ContactDeleteException("Contact not found");
        }
        contactRepository.deleteByFirstNameAndMiddleNameAndLastName(contact.getFirstName(), contact.getMiddleName(), contact.getLastName());
        log.info("Contact deleted: {}", optionalContact.get());
    }

    private Optional<Contact> getContact(Contact contact, User user) {
        Optional<Contact> optionalContact;
        if (contact.getMiddleName() == null) {
            optionalContact = contactRepository.findContact(contact.getFirstName(), contact.getLastName(), user.getId());
        } else {
            optionalContact = contactRepository.findContact(contact.getFirstName(), contact.getMiddleName(), contact.getLastName(), user.getId());
        }
        return optionalContact;
    }

    @Transactional
    public ContactReadDto update(ContactCreateUpdateDto contactCreateUpdateDto, BindingResult errors) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow();
        Contact contact = contactCreateMapper.map(contactCreateUpdateDto);
        contact.setUser(userRepository.findByUsername(name).orElseThrow());
        Optional<Contact> optionalContact = getContact(contact, user);
        contactUniqueUpdateValidator.validate(contactCreateUpdateDto, errors);
        if (errors.hasErrors()) {
            ExceptionParser.parseValidation(errors);
        }
        Contact updatedContact = contactCreateMapper.map(contactCreateUpdateDto, optionalContact.get());
        Contact savedContact = contactRepository.save(updatedContact);
        log.info("Contact updated: {}", savedContact);
        return contactReadMapper.map(savedContact);
    }

    public List<ContactReadDto> getAll() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return contactRepository.findAllByUser_Username(name).stream()
                .map(contactReadMapper::map)
                .toList();
    }

    public void deleteById(Long id) {
        User user = userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow();
        Optional<Contact> optionalContact = contactRepository.findByIdAndUser_Id(id, user.getId());
        if (optionalContact.isEmpty()) {
            throw new ContactDeleteException("Contact not found");
        }
        contactRepository.deleteById(id);
    }

    public ContactReadDto getById(Long id) {
        User user = userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow();
        Optional<Contact> optionalContact = contactRepository.findByIdAndUser_Id(id, user.getId());
        if (optionalContact.isEmpty()) {
            throw new ContactNotFoundException("Contact not found");
        }
        return contactReadMapper.map(optionalContact.get());
    }
}
