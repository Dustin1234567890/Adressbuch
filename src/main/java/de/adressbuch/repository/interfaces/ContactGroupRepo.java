package de.adressbuch.repository.interfaces;

import java.util.List;

public interface ContactGroupRepo {
    void addContactToGroup(String contactId, String groupId);
    void removeContactFromGroup(String contactId, String groupId);
    List<String> findContactIdsByGroupId(String groupId);
    boolean isContactInGroup(String contactId, String groupId);
    void initializeDatabase();
}
