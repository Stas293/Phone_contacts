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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContactService {
    private final ContactRepository contactRepository;
    private final Mapper<ContactCreateUpdateDto, Contact> contactCreateMapper;
    private final Mapper<ContactDeleteDto, Contact> contactDeleteMapper;
    private final Mapper<Contact, ContactReadDto> contactReadMapper;
    private final Mapper<String, Contact> contactMapper;
    private final UserRepository userRepository;
    private final ContactUniqueCreateValidator contactUniqueCreateValidator;
    private final ContactUniqueUpdateValidator contactUniqueUpdateValidator;
    private final ImageService imageService;

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
        imageService.deleteImage(optionalContact.get().getImagePath());
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

    private void uploadImage(MultipartFile image, Contact contact, String username) {
        Optional.ofNullable(image)
                .filter(file -> !file.isEmpty())
                .ifPresent(file -> saveImage(file, contact, username));
    }

    @SneakyThrows
    private void saveImage(MultipartFile file, Contact contact, String username) {
        imageService.upload(contact, username, file.getOriginalFilename(), file.getInputStream());
    }

    @Transactional
    public void deleteImage(ContactDeleteDto contactDeleteDto) {
        Contact contact = contactDeleteMapper.map(contactDeleteDto);
        User user = userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow();
        Optional<Contact> optionalContact = getContact(contact, user);
        if (optionalContact.isEmpty()) {
            throw new ContactDeleteException("Contact not found");
        }
        imageService.deleteImage(optionalContact.get().getImagePath());
        optionalContact.get().setImagePath(null);
        contactRepository.save(optionalContact.get());
    }

    @Transactional
    public void saveImage(String contactName, MultipartFile file) {
        Pattern pattern = Pattern.compile("^[a-zA-Zа-яА-Я]+(\\s[a-zA-Zа-яА-Я]+){1,2}$");
        if (!pattern.matcher(contactName).matches()) {
            throw new ContactNotFoundException("Contact name is not valid");
        }
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow();
        Contact contact = contactMapper.map(contactName);
        Optional<Contact> optionalContact = getContact(contact, user);
        if (optionalContact.isEmpty()) {
            throw new ContactNotFoundException("Contact not found");
        }
        uploadImage(file, optionalContact.get(), name);
        String imagePath = String.format("%s/%s/%s", name, contact.getName(), file.getOriginalFilename());
        Optional.ofNullable(file)
                .filter(Predicate.not(MultipartFile::isEmpty))
                .ifPresent(image -> optionalContact.get().setImagePath(imagePath));
        contactRepository.save(optionalContact.get());
    }

    public ByteArrayResource getImage(String contactName) {
        Pattern pattern = Pattern.compile("^[a-zA-Zа-яА-Я]+(\\s[a-zA-Zа-яА-Я]+){1,2}$");
        if (!pattern.matcher(contactName).matches()) {
            throw new ContactNotFoundException("Contact name is not valid");
        }
        Contact contact = contactMapper.map(contactName);
        User user = userRepository.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow();
        Optional<Contact> optionalContact = getContact(contact, user);
        if (optionalContact.isEmpty()) {
            throw new ContactNotFoundException("Contact not found");
        }
        if (optionalContact.get().getImagePath() == null) {
            throw new ContactNotFoundException("Image not found");
        }
        Optional<byte[]> image = imageService.getImage(optionalContact.get().getImagePath());
        return new ByteArrayResource(image.get());
    }
}
