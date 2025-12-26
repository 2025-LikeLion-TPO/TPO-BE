package org.example.tpo.repository;

import org.example.tpo.entity.Contact;
import org.example.tpo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByUser(Users user);
}
