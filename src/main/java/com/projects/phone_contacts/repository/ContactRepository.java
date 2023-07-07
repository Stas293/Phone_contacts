package com.projects.phone_contacts.repository;

import com.projects.phone_contacts.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    boolean existsByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);

    void deleteByFirstNameAndLastName(String firstName, String lastName);

    void deleteByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);

    Optional<Contact> findByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);
    @Query("SELECT c FROM Contact c WHERE c.firstName = ?1 AND c.middleName = ?2 AND c.lastName = ?3 AND c.user.id = ?4")
    Optional<Contact> findContact(String firstName, String middleName, String lastName, Long user);
    @Query("SELECT c FROM Contact c WHERE c.firstName = ?1 AND c.lastName = ?2 AND c.user.id = ?3")
    Optional<Contact> findContact(String firstName, String lastName, Long id);

    List<Contact> findAllByUser_Username(String name);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    Optional<Contact> findByIdAndUser_Id(Long contactId, Long userId);
}