package de.adressbuch.service;

import de.adressbuch.models.Contact;
import de.adressbuch.repository.interfaces.ContactRepo;
import de.adressbuch.util.Utils;

import java.util.List;
import java.util.Optional;

public class ContactService {
    private final ContactRepo contactRepository;

    public ContactService(ContactRepo contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact addContact(String name, String phoneNumber, String address, String email) {
        validateContactName(name);
        Contact contact = Contact.create(
            name,
            Utils.convertToOptionalNonBlank(phoneNumber),
            Utils.convertToOptionalNonBlank(address),
            Utils.convertToOptionalNonBlank(email)
        );
        return contactRepository.save(contact);
    }

    public Contact updateContact(Long id, String name, String phoneNumber, String address, String email) {
        Contact existing = findContactById(id).orElseThrow();
        validateContactName(name);
        
        Contact updated = Contact.of(
            id,
            name,
            Utils.convertToOptionalNonBlank(phoneNumber),
            Utils.convertToOptionalNonBlank(address),
            Utils.convertToOptionalNonBlank(email)
        );
        return contactRepository.update(updated);
    }

    public boolean deleteContact(Long id) {
        Optional<Contact> deleted = contactRepository.deleteById(id);
        if (deleted.isEmpty()) {
            throw new IllegalArgumentException("Contact not found with id: " + id);
        }
        return true;
    }

    public Optional<Contact> findContactById(Long id) {
        return contactRepository.findById(id);
    }

    public List<Contact> findAllContacts() {
        return contactRepository.findAll();
    }

    public void validateContactName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Contact name cannot be empty");
        }
    }
}