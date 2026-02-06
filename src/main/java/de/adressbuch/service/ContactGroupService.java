package de.adressbuch.service;

import java.util.List;

import de.adressbuch.exception.ContactNotFoundException;
import de.adressbuch.exception.GroupNotFoundException;
import de.adressbuch.exception.ValidationException;
import de.adressbuch.repository.interfaces.ContactGroupRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactGroupService {
    private static final Logger logger = LoggerFactory.getLogger(ContactGroupService.class);
    private final ContactGroupRepo contactGroupRepository;
    private final GroupService groupService;
    private final ContactService contactService;

    public ContactGroupService(ContactGroupRepo contactGroupRepository, GroupService groupService, ContactService contactService) {
        this.contactGroupRepository = contactGroupRepository;
        this.groupService = groupService;
        this.contactService = contactService;
    }

    public void addContactToGroup(String contactId, String groupId) throws ContactNotFoundException, GroupNotFoundException {
        if (contactService.findContactById(contactId).isEmpty()) {
            throw new ContactNotFoundException("Kontakt mit ID " + contactId + " nicht gefunden");
        }
        
        if (groupService.findGroupById(groupId).isEmpty()) {
            throw new GroupNotFoundException("Gruppe mit ID " + groupId + " nicht gefunden");
        }
        
        contactGroupRepository.addContactToGroup(contactId, groupId);
        logger.info("Kontakt {} zu Gruppe {} geaddet", contactId, groupId);
    }

    public boolean isContactInGroup(String contactId, String groupId) {
        boolean foundContactCheck = contactGroupRepository.isContactInGroup(contactId, groupId);
        logger.debug("Kontakt {} in Gruppe {} gesucht: {}", contactId, groupId, foundContactCheck);
        return foundContactCheck;
    }

    public List<String> findContactIdsByGroupId(String groupId) {
        List<String> contactIds = contactGroupRepository.findContactIdsByGroupId(groupId);
        logger.debug("{} Kontakte in Gruppe {} gefunden", contactIds.size(), groupId);
        return contactIds;
    }

    public void removeContactFromGroup(String contactId, String groupId) throws ValidationException {

        if (!isContactInGroup(contactId, groupId)) {
            throw new ValidationException("Kontakt " + contactId + " ist nicht in Gruppe " + groupId + " vorhanden");
        }
        
        contactGroupRepository.removeContactFromGroup(contactId, groupId);
        logger.info("Kontakt {} von Gruppe {} entfernt", contactId, groupId);
    }
}
