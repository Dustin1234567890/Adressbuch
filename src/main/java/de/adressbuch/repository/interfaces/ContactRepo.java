package de.adressbuch.repository.interfaces;

import de.adressbuch.models.Contact;
import java.util.List;
import java.util.Optional;

public interface ContactRepo {
    Contact save(Contact contact);
    Contact update(Contact contact);
    Optional<Contact> deleteById(Long id);
    Optional<Contact> findById(Long id);
    List<Contact> findAll();
    void initializeDatabase();
}
