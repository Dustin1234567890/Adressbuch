package de.adressbuch.service;

import de.adressbuch.models.Contact;
import de.adressbuch.repository.interfaces.ContactGroupRepo;

import java.util.List;

public class ContactFilterService {
    private final ContactService contactService;
    private final ContactGroupRepo contactGroupRepository;

    public ContactFilterService(ContactService contactService, ContactGroupRepo contactGroupRepository) {
        this.contactService = contactService;
        this.contactGroupRepository = contactGroupRepository;
    }

    public List<Contact> filterContactsByGroup(String groupId) {
        List<String> contactIds = contactGroupRepository.findContactIdsByGroupId(groupId);
        return contactIds.stream()
                .flatMap(id -> contactService.findContactById(id).stream())
                .toList();
    }

    public List<Contact> filterContactsByPhoneExists() {
        return contactService.findAllContacts().stream()
                .filter(c -> c.phoneNumber().isPresent())
                .toList();
    }

    public List<Contact> filterContactsByEmailExists() {
        return contactService.findAllContacts().stream()
                .filter(c -> c.email().isPresent())
                .toList();
    }

    public List<Contact> filterContactsByAddressExists() {
        return contactService.findAllContacts().stream()
                .filter(c -> c.address().isPresent())
                .toList();
    }
}
