package de.adressbuch.repository.interfaces;

import java.util.List;
import java.util.Optional;

import de.adressbuch.models.Contact;

public interface ContactRepo {
    Contact save(Contact contact);
    Contact update(Contact contact);
    Optional<Contact> deleteById(String id);
    Optional<Contact> findById(String id);
    List<Contact> findAll();
    Optional<List<Contact>> findByName(String name);
    Optional<List<Contact>> findByPhone(String phone);
    Optional<List<Contact>> findByEmail(String email);
    Optional<List<Contact>> findByAddresse(String address);
    void initializeDatabase();
}
