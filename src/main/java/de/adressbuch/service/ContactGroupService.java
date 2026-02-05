package de.adressbuch.service;

import java.util.List;

import de.adressbuch.repository.interfaces.ContactGroupRepo;

public class ContactGroupService {
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
        } else {
            throw new IllegalArgumentException("Contact or Group does not exist");
        }
    }

    public boolean isContactInGroup(String contactId, String groupId) {
        if (contactExists(contactId) && groupExists(groupId)) {
            return contactGroupRepository.isContactInGroup(contactId, groupId);
        } else {
            throw new IllegalArgumentException("Contact or Group does not exist");
        }
    }

    public List<String> findContactIdsByGroupId(String groupId) {
        if (groupExists(groupId)) {
            return contactGroupRepository.findContactIdsByGroupId(groupId);
        } else {
            throw new IllegalArgumentException("Contact or Group does not exist");
        }
    }

    public void removeContactFromGroup(String contactId, String groupId) {
        if (contactExists(contactId) && groupExists(groupId)) {
            contactGroupRepository.removeContactFromGroup(contactId, groupId);
        } else {
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
