package de.adressbuch.service;

import de.adressbuch.exception.ContactNotFoundException;
import de.adressbuch.exception.GroupNotFoundException;
import de.adressbuch.exception.ValidationException;
import de.adressbuch.models.Contact;
import de.adressbuch.models.Group;
import de.adressbuch.repository.SQLiteContactGroupRepo;
import de.adressbuch.repository.SQLiteContactRepo;
import de.adressbuch.repository.SQLiteGroupRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ContactGroupService integration")
public class ContactGroupServiceIntegrationTest {

    @TempDir
    Path tempDir;
    
    private ContactGroupService contactGroupService;
    private SQLiteContactGroupRepo contactGroupRepo;
    private SQLiteContactRepo contactRepo;
    private SQLiteGroupRepo groupRepo;
    private ContactService contactService;
    private GroupService groupService;
    private String dbUrl;

    @BeforeEach
    public void setup() throws Exception {
        dbUrl = "jdbc:sqlite:" + tempDir.resolve("test.db").toString();
        contactRepo = new SQLiteContactRepo(dbUrl);
        groupRepo = new SQLiteGroupRepo(dbUrl);
        contactGroupRepo = new SQLiteContactGroupRepo(dbUrl);
        
        contactService = new ContactService(contactRepo);
        groupService = new GroupService(groupRepo);
        contactGroupService = new ContactGroupService(contactGroupRepo, groupService, contactService);
        contactRepo.initializeDatabase();
        groupRepo.initializeDatabase();
        contactGroupRepo.initializeDatabase();
    }

    @Nested
    @DisplayName("relationships")
    class ContactGroupRelationshipTests {
        
        @Test
        @DisplayName("add")
        public void shouldAddContactToGroup() throws Exception {

            contactService.addContact("Sample Kontakt", "524234243", "Testerstrasse 12", "sample@test.de");
            groupService.addGroup("sample friends", "close friends");
            
            List<Contact> contacts = contactService.findAllContacts();
            List<Group> groups = groupService.findAllGroups();
            String contactId = contacts.get(0).id();
            String groupId = groups.get(0).id();
            

            contactGroupService.addContactToGroup(contactId, groupId);
            

            assertTrue(contactGroupService.isContactInGroup(contactId, groupId));
        }

        @Test
        @DisplayName("remove")
        public void shouldRemoveContactFromGroup() throws Exception {

            contactService.addContact("Sample Kontakt", "524234243", "Testerstrasse 12", "sample@test.de");
            groupService.addGroup("sample friends", "close friends");
            
            List<Contact> contacts = contactService.findAllContacts();
            List<Group> groups = groupService.findAllGroups();
            String contactId = contacts.get(0).id();
            String groupId = groups.get(0).id();
            
            contactGroupService.addContactToGroup(contactId, groupId);
            assertTrue(contactGroupService.isContactInGroup(contactId, groupId));
            

            contactGroupService.removeContactFromGroup(contactId, groupId);
            

            assertFalse(contactGroupService.isContactInGroup(contactId, groupId));
        }

        @Test
        @DisplayName("multiple")
        public void shouldHandleMultipleContactsInGroup() throws Exception {

            contactService.addContact("Sample Kontakt", "524234243", "Testerstrasse 12", "sample@test.de");
            contactService.addContact("Sample Kontakt 2", "243242343", "Testerstrasse 23", "sample2@test.de");
            groupService.addGroup("sample team", "work team");
            
            List<Contact> contacts = contactService.findAllContacts();
            List<Group> groups = groupService.findAllGroups();
            String groupId = groups.get(0).id();
            

            for (int i = 0; i < 2; i++) {
                contactGroupService.addContactToGroup(contacts.get(i).id(), groupId);
            }
            

            assertEquals(2, contactGroupService.findContactIdsByGroupId(groupId).size());
        }
    }

    @Nested
    @DisplayName("search")
    class SearchTests {
        
        @Test
        @DisplayName("findByGroup")
        public void shouldFindContactsByGroup() throws Exception {

            contactService.addContact("Sample Kontakt", "524234243", "Testerstrasse 12", "sample@test.de");
            contactService.addContact("Sample Kontakt 2", "243242343", "Testerstrasse 23", "sample2@test.de");
            groupService.addGroup("sample friends", "close friends");
            groupService.addGroup("sample work", "work colleagues");
            
            List<Contact> contacts = contactService.findAllContacts();
            List<Group> groups = groupService.findAllGroups();
            
            String kontakt1Id = contacts.stream()
                .filter(c -> c.name().equals("Sample Kontakt"))
                .map(Contact::id)
                .findFirst()
                .orElseThrow();
            String kontakt2Id = contacts.stream()
                .filter(c -> c.name().equals("Sample Kontakt 2"))
                .map(Contact::id)
                .findFirst()
                .orElseThrow();
            String freundeId = groups.stream()
                .filter(g -> g.name().equals("sample friends"))
                .map(Group::id)
                .findFirst()
                .orElseThrow();
            

            contactGroupService.addContactToGroup(kontakt1Id, freundeId);
            contactGroupService.addContactToGroup(kontakt2Id, freundeId);
            

            assertEquals(2, contactGroupService.findContactIdsByGroupId(freundeId).size());
        }

    }

    @Nested
    @DisplayName("error handling")
    class ErrorHandlingTests {
        
        @Test
        @DisplayName("invalid contact id")
        public void shouldThrowExceptionForInvalidContactId() throws Exception {
            groupService.addGroup("sample test", "test group");
            List<Group> groups = groupService.findAllGroups();
            

            assertThrows(ContactNotFoundException.class, () -> 
                contactGroupService.addContactToGroup("invalid-id", groups.get(0).id()));
        }

        @Test
        @DisplayName("invalid group id")
        public void shouldThrowExceptionForInvalidGroupId() throws Exception {
            contactService.addContact("Sample Kontakt", "524234243", "Testerstrasse 12", "sample@test.de");
            List<Contact> contacts = contactService.findAllContacts();
            

            assertThrows(GroupNotFoundException.class, () -> 
                contactGroupService.addContactToGroup(contacts.get(0).id(), "invalid-id"));
        }

        @Test
        @DisplayName("remove non-existent relation")
        public void shouldThrowExceptionWhenRemovingNonExistentRelationship() throws Exception {
            contactService.addContact("Sample Kontakt", "524234243", "Testerstrasse 12", "sample@test.de");
            groupService.addGroup("sample friends", "close friends");
            
            List<Contact> contacts = contactService.findAllContacts();
            List<Group> groups = groupService.findAllGroups();
            String contactId = contacts.get(0).id();
            String groupId = groups.get(0).id();
            

            assertThrows(ValidationException.class, () ->
                contactGroupService.removeContactFromGroup(contactId, groupId));
        }
    }
}
