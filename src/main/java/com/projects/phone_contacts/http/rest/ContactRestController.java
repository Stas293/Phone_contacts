package com.projects.phone_contacts.http.rest;

import com.projects.phone_contacts.dto.ContactCreateUpdateDto;
import com.projects.phone_contacts.dto.ContactDeleteDto;
import com.projects.phone_contacts.dto.ContactReadDto;
import com.projects.phone_contacts.service.ContactService;
import com.projects.phone_contacts.validation.CreateContactValidationGroup;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
@Slf4j
public class ContactRestController {
    private final ContactService contactService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactReadDto create(
            @RequestBody @Validated({Default.class, CreateContactValidationGroup.class}) ContactCreateUpdateDto contactCreateUpdateDto,
            BindingResult errors) {
        return contactService.create(contactCreateUpdateDto, errors);
    }

    @DeleteMapping("/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@RequestBody ContactDeleteDto contactDeleteDto) {
        contactService.deleteImage(contactDeleteDto);
    }

    @GetMapping(value = "/image", produces = APPLICATION_OCTET_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ByteArrayResource getImage(String name) {
        return contactService.getImage(name);
    }

    @PutMapping(value = "/image", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImage(@RequestPart String name,
                            @RequestPart MultipartFile file) {
        contactService.saveImage(name, file);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody ContactDeleteDto contactDeleteDto) {
        contactService.delete(contactDeleteDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        contactService.deleteById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ContactReadDto update(@Validated @RequestBody ContactCreateUpdateDto contactCreateUpdateDto,
                                 BindingResult errors) {
        return contactService.update(contactCreateUpdateDto, errors);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContactReadDto getById(@PathVariable Long id) {
        return contactService.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ContactReadDto> getAll() {
        return contactService.getAll();
    }
}
