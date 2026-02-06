package de.adressbuch.service;

import java.util.List;

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

    public void addContactToGroup(String contactId, String groupId) {
        if (contactExists(contactId) && groupExists(groupId)) {
            contactGroupRepository.addContactToGroup(contactId, groupId);
            logger.info("Kontakt {} zu Gruppe {} geaddet", contactId, groupId);
        } else {
            throw new IllegalArgumentException("Contact or Group does not exist");
        }
    }

    public boolean isContactInGroup(String contactId, String groupId) {
        if (contactExists(contactId) && groupExists(groupId)) {
            boolean foundContactCheck = contactGroupRepository.isContactInGroup(contactId, groupId);
            logger.debug("Kontakt {} in Gruppe {} gesucht: {}", contactId, groupId, foundContactCheck);
            return foundContactCheck;
        } else {
            logger.warn("Kontakt oder Gruppe existiert nicht: {} / {}", contactId, groupId);
            throw new IllegalArgumentException("Contact or Group does not exist");
        }
    }

    public List<String> findContactIdsByGroupId(String groupId) {
        if (groupExists(groupId)) {
            List<String> contactIds = contactGroupRepository.findContactIdsByGroupId(groupId);
            logger.debug("{} Kontakte in Gruppe {} gefunden", contactIds.size(), groupId);
            return contactIds;
        } else {
            logger.warn("Gruppe existiert nicht: {}", groupId);
            throw new IllegalArgumentException("Contact or Group does not exist");
        }
    }

    public void removeContactFromGroup(String contactId, String groupId) {
        if (contactExists(contactId) && groupExists(groupId)) {
            contactGroupRepository.removeContactFromGroup(contactId, groupId);
            logger.info("Kontakt {} von Gruppe {} entfernt", contactId, groupId);
        } else {
            logger.warn("Kontakt oder Gruppe existiert nicht: {} / {}", contactId, groupId);
            throw new IllegalArgumentException("Contact or Group does not exist");
        }
    }

    public boolean groupExists(String id) {
        return groupService.findGroupById(id).isPresent();
    }

    public boolean contactExists(String id) {
        return contactService.findContactById(id).isPresent();
    }

}
