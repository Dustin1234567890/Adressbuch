package de.adressbuch.repository.interfaces;

import java.util.List;
import java.util.Optional;

import de.adressbuch.models.Contact;

public interface ContactRepo {
    Contact save(Contact contact);
    Contact update(Contact contact);
    Optional<Contact> deleteById(Long id);
    Optional<Contact> findById(Long id);
    List<Contact> findAll();
    List<Contact> searchByName(String searchTerm);
    void initializeDatabase();
}
