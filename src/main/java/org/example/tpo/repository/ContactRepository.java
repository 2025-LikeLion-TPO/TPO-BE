package org.example.tpo.repository;

import org.example.tpo.entity.Contact;
import org.example.tpo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByUser(Users user);

    @Query("""
        select e.contact
        from Event e
        where e.user = :user
        order by e.eventDate desc
    """)
    List<Contact> findRecentContactsByEventDate(Users user, Pageable pageable);
}
