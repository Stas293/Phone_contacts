package com.projects.phone_contacts.repository;

import com.projects.phone_contacts.model.Email;
import com.projects.phone_contacts.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmailRepository extends JpaRepository<Email, Long> {
    @Query("select (count(e) > 0) from Email e where e.username = ?1 and e.domain = ?2 and e.contact.user.username = ?3")
    boolean existsByMailAndUser(String username, String domain, String contactUsername);

    List<Email> findAllByContact_User(User user);
}