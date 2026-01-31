package de.adressbuch.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/*
@Disabled("Integration test vorerst disabled")
public class SQLiteContactGroupRepoIntegrationTest {
    private SQLiteContactGroupRepo contactGroupRepo;
    private static final String DB_URL = "jdbc:sqlite:file:memdb2?mode=memory&cache=shared";

    @BeforeEach
    public void setup() throws Exception {
        contactGroupRepo = new SQLiteContactGroupRepo(DB_URL);
    }

    @Test
    public void testAddContactToGroup() {
        Long contactId = 1L;
        Long groupId = 1L;

        contactGroupRepo.addContactToGroup(contactId, groupId);

        assertTrue(contactGroupRepo.isContactInGroup(contactId, groupId));
    }

    @Test
    public void testIsContactInGroup() {
        Long contactId = 1L;
        Long groupId = 1L;

        contactGroupRepo.addContactToGroup(contactId, groupId);

        boolean result = contactGroupRepo.isContactInGroup(contactId, groupId);

        assertTrue(result);
    }

    @Test
    public void testIsContactNotInGroup() {
        Long contactId = 1L;
        Long groupId = 1L;

        boolean result = contactGroupRepo.isContactInGroup(contactId, groupId);

        assertFalse(result);
    }

    @Test
    public void testFindContactIdsByGroupId() {
        Long groupId = 1L;

        contactGroupRepo.addContactToGroup(1L, groupId);
        contactGroupRepo.addContactToGroup(2L, groupId);
        contactGroupRepo.addContactToGroup(3L, groupId);

        List<Long> result = contactGroupRepo.findContactIdsByGroupId(groupId);

        assertEquals(3, result.size());
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));
        assertTrue(result.contains(3L));
    }

    @Test
    public void testFindContactIdsByGroupIdEmpty() {
        Long groupId = 999L;

        List<Long> result = contactGroupRepo.findContactIdsByGroupId(groupId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testRemoveContactFromGroup() {
        Long contactId = 1L;
        Long groupId = 1L;

        contactGroupRepo.addContactToGroup(contactId, groupId);
        assertTrue(contactGroupRepo.isContactInGroup(contactId, groupId));

        contactGroupRepo.removeContactFromGroup(contactId, groupId);

        assertFalse(contactGroupRepo.isContactInGroup(contactId, groupId));
    }

    @Test
    public void testMultipleContactsInMultipleGroups() {
        contactGroupRepo.addContactToGroup(1L, 1L);
        contactGroupRepo.addContactToGroup(1L, 2L);
        contactGroupRepo.addContactToGroup(2L, 1L);

        List<Long> group1Contacts = contactGroupRepo.findContactIdsByGroupId(1L);
        List<Long> group2Contacts = contactGroupRepo.findContactIdsByGroupId(2L);

        assertEquals(2, group1Contacts.size());
        assertEquals(1, group2Contacts.size());
        assertTrue(group1Contacts.contains(1L));
        assertTrue(group1Contacts.contains(2L));
        assertTrue(group2Contacts.contains(1L));
    }
}
*/