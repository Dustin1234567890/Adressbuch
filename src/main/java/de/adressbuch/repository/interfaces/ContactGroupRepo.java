package de.adressbuch.repository.interfaces;

import java.util.List;

public interface ContactGroupRepo {
    void addContactToGroup(Long contactId, Long groupId);
    void removeContactFromGroup(Long contactId, Long groupId);
    List<Long> findContactIdsByGroupId(Long groupId);
    boolean isContactInGroup(Long contactId, Long groupId);
    void initializeDatabase();
}
