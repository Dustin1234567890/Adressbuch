package de.adressbuch.service;

import java.util.List;
import java.util.Optional;

import de.adressbuch.models.Contact;
import de.adressbuch.repository.interfaces.ContactRepo;
import de.adressbuch.util.Utils;

public class ContactService {
    private final ContactRepo contactRepository;

    public ContactService(ContactRepo contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact addContact(String name, String phoneNumber, String address, String email) {
        validateContactName(name);
        Contact contact = new Contact(
            Utils.generateId(),
            name,
            Utils.convertToOptionalNonBlank(phoneNumber),
            Utils.convertToOptionalNonBlank(address),
            Utils.convertToOptionalNonBlank(email)
        );
        return contactRepository.save(contact);
    }

    public Contact updateContact(String id, String name, String phoneNumber, String address, String email) {
        Optional<Contact> existing = findContactById(id);
        if(existing.isEmpty()){
            throw new IllegalArgumentException("Contact does not exist" + id);
        }
        validateContactName(name);
        
        Contact updated = new Contact(
            id,
            name,
            Utils.convertToOptionalNonBlank(phoneNumber),
            Utils.convertToOptionalNonBlank(address),
            Utils.convertToOptionalNonBlank(email)
        );
        return contactRepository.update(updated);
    }

    public boolean deleteContact(String id) {
        Optional<Contact> deleted = contactRepository.deleteById(id);
        if (deleted.isEmpty()) {
            throw new IllegalArgumentException("Contact not found with id: " + id);
        }
        return true;
    }

    public Optional<Contact> findContactById(String id) {
        return contactRepository.findById(id);
    }

    public List<Contact> findAllContacts() {
        return contactRepository.findAll();
    }

    public Optional<List<Contact>> findContactsByName(String searchTerm) {
        return contactRepository.findByName(searchTerm);
    }
    
    public Optional<List<Contact>> findContactsByPhone(String searchTerm) {
        return contactRepository.findByPhone(searchTerm);
    }

    public Optional<List<Contact>> findContactsByEmail(String searchTerm) {
        return contactRepository.findByEmail(searchTerm);
    }

    public Optional<List<Contact>> findContactsByAddresse(String searchTerm) {
        return contactRepository.findByAddresse(searchTerm);
    }

    public void validateContactName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Contact name cannot be empty");
        }
    }
}