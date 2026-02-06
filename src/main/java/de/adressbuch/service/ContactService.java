package de.adressbuch.service;

import java.util.List;
import java.util.Optional;

import de.adressbuch.exception.ContactNotFoundException;
import de.adressbuch.exception.ValidationException;
import de.adressbuch.models.Contact;
import de.adressbuch.repository.interfaces.ContactRepo;
import de.adressbuch.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);
    private final ContactRepo contactRepository;

    public ContactService(ContactRepo contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact addContact(String name, String phoneNumber, String address, String email) throws ValidationException {
        validateContactName(name);
        Contact contact = new Contact(
            Utils.generateId(),
            name,
            Utils.convertToOptionalNonBlank(phoneNumber),
            Utils.convertToOptionalNonBlank(address),
            Utils.convertToOptionalNonBlank(email)
        );
        
        Contact savedContact = contactRepository.save(contact);
        logger.info("Kontakt geaddet: {} ({})", savedContact.name(), savedContact.id());
        return savedContact;
    }

    public Contact updateContact(String id, String name, String phoneNumber, String address, String email) 
            throws ContactNotFoundException, ValidationException {
        
        findContactById(id).orElseThrow(() -> new ContactNotFoundException(id));
        
        validateContactName(name);
        
        Contact updated = new Contact(
            id,
            name,
            Utils.convertToOptionalNonBlank(phoneNumber),
            Utils.convertToOptionalNonBlank(address),
            Utils.convertToOptionalNonBlank(email)
        );
        
        Contact updatedContact = contactRepository.update(updated);
        logger.info("Kontakt geupdatet: {} ({})", name, id);
        return updatedContact;
    }

    public void deleteContact(String id) throws ContactNotFoundException {
        contactRepository.deleteById(id).orElseThrow(() -> new ContactNotFoundException(id));
        
        logger.info("Kontakt gelöscht: {}", id);
    }

    public Optional<Contact> findContactById(String id) {
        return contactRepository.findById(id);
    }

    public List<Contact> findAllContacts() {
        List<Contact> contacts = contactRepository.findAll();
        logger.debug("{} Kontakte geladen", contacts.size());
        return contacts;
    }

    public Optional<List<Contact>> findContactsByName(String searchTerm) {
        Optional<List<Contact>> foundContacts = contactRepository.findByName(searchTerm);
        logger.debug("Suche nach Name '{}': {} Kontakte gefunden", searchTerm, foundContacts.map(List::size).orElse(0));
        return foundContacts;
    }
    
    public Optional<List<Contact>> findContactsByPhone(String searchTerm) {
        Optional<List<Contact>> foundContacts = contactRepository.findByPhone(searchTerm);
        logger.debug("Suche nach Telefon '{}': {} Kontakte gefunden", searchTerm, foundContacts.map(List::size).orElse(0));
        return foundContacts;
    }

    public Optional<List<Contact>> findContactsByEmail(String searchTerm) {
        Optional<List<Contact>> foundContacts = contactRepository.findByEmail(searchTerm);
        logger.debug("Suche nach Email '{}': {} Kontakte gefunden", searchTerm, foundContacts.map(List::size).orElse(0));
        return foundContacts;
    }

    public Optional<List<Contact>> findContactsByAddresse(String searchTerm) {
        Optional<List<Contact>> foundContacts = contactRepository.findByAddresse(searchTerm);
        logger.debug("Suche nach Adresse '{}': {} Kontakte gefunden", searchTerm, foundContacts.map(List::size).orElse(0));
        return foundContacts;
    }

    private void validateContactName(String name) throws ValidationException {
        if (name == null || name.isBlank()) {
            logger.warn("Kontakt-Namen Validierung fehlgeschlagen: name ist null oder empty");
            throw new ValidationException("Kontakt Name darf nicht leer sein");
        }
    }
}