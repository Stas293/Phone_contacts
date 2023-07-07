package com.projects.phone_contacts.repository;

import com.projects.phone_contacts.model.Phone;
import com.projects.phone_contacts.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    @Query("""
            select (count(p) > 0) from Phone p
            where p.areaCode = ?1 and p.countryCode = ?2 and p.number = ?3 and p.contact.user.username = ?4""")
    boolean existsByNumberAndUser(String areaCode, String countryCode, String number, String username);

    List<Phone> findAllByContact_User(User user);
}