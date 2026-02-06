package de.adressbuch.service;

import de.adressbuch.exception.ContactNotFoundException;
import de.adressbuch.exception.ValidationException;
import de.adressbuch.models.Contact;
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

@DisplayName("ContactService - Integration Tests")
public class ContactServiceIntegrationTest {
    
    @TempDir
    Path tempDir;
    
    private ContactService contactService;
    private SQLiteContactRepo contactRepository;
    private SQLiteGroupRepo groupRepository;
    private String dbUrl;

    @BeforeEach
    public void setup() throws Exception {
        dbUrl = "jdbc:sqlite:" + tempDir.resolve("test.db").toString();
        contactRepository = new SQLiteContactRepo(dbUrl);
        groupRepository = new SQLiteGroupRepo(dbUrl);
        contactService = new ContactService(contactRepository);
        
        contactRepository.initializeDatabase();
        groupRepository.initializeDatabase();
    }
    
    @Nested
    @DisplayName("crud")
    class CrudTests {
        
        @Test
        @DisplayName("full cycle")
        public void shouldPerformFullCrudCycle() throws Exception {

            assertEquals(0, contactService.findAllContacts().size());
            

            contactService.addContact("Sample Kontakt", "524234243", "Testerstrasse 12", "sample@test.de");
            

            assertEquals(1, contactService.findAllContacts().size());
            String contactId = contactService.findAllContacts().get(0).id();
            

            assertTrue(contactService.findContactById(contactId).isPresent());
            assertEquals("Sample Kontakt", contactService.findContactById(contactId).get().name());
            assertEquals("524234243", contactService.findContactById(contactId).get().phoneNumber().orElse(null));
            assertEquals("Testerstrasse 12", contactService.findContactById(contactId).get().address().orElse(null));
            assertEquals("sample@test.de", contactService.findContactById(contactId).get().email().orElse(null));
            

            contactService.updateContact(contactId, "Sample Kontakt Updated", "243242343", "Testerstrasse 23", "sample.updated@test.de");
            

            assertTrue(contactService.findContactById(contactId).isPresent());
            assertEquals("Sample Kontakt Updated", contactService.findContactById(contactId).get().name());
            assertEquals("243242343", contactService.findContactById(contactId).get().phoneNumber().orElse(null));
            

            contactService.deleteContact(contactId);
            

            List<Contact> remainingContacts = contactService.findAllContacts();
            assertTrue(remainingContacts.isEmpty());
        }

        @Test
        @DisplayName("multiple")
        public void shouldHandleMultipleContacts() throws Exception {

            contactService.addContact("Sample Kontakt", "524234243", "Testerstrasse 12", "sample@test.de");
            contactService.addContact("Sample Kontakt 2", "243242343", "Testerstrasse 23", "sample2@test.de");
            contactService.addContact("Sample Kontakt 3", "354353453", "Testerstrasse 1", "sample3@test.de");
            

            assertEquals(3, contactService.findAllContacts().size());
            List<Contact> allContacts = contactService.findAllContacts();
            String kontakt3Id = allContacts.stream()
                .filter(c -> c.name().equals("Sample Kontakt 3"))
                .map(Contact::id)
                .findFirst()
                .orElseThrow();
            contactService.deleteContact(kontakt3Id);
            

            assertEquals(2, contactService.findAllContacts().size());
        }
    }

    @Nested
    @DisplayName("search")
    class SearchAndFilterTests {
        
        @Test
        @DisplayName("byName")
        public void shouldSearchByName() throws Exception {

            contactService.addContact("Sample Kontakt", "524234243", "Testerstrasse 12", "sample@test.de");
            contactService.addContact("Sample Kontakt 2", "243242343", "Testerstrasse 23", "sample2@test.de");
            contactService.addContact("Sample Test", "354353453", "Teststrasse 1", "sample.test@test.de");
            

            Optional<List<Contact>> results = contactService.findContactsByName("Sample");
            

            assertTrue(results.isPresent());
            assertEquals(3, results.get().size());
            assertTrue(results.get().stream().anyMatch(c -> c.name().contains("Sample Kontakt")));
            assertTrue(results.get().stream().anyMatch(c -> c.name().contains("Sample Test")));
        }

        @Test
        @DisplayName("byPhone")
        public void shouldSearchByPhone() throws Exception {
            contactService.addContact("Sample Kontakt", "0123456789", null, null);
            contactService.addContact("Sample Kontakt 2", "0987654321", null, null);
            

            assertTrue(contactService.findContactsByPhone("0123").isPresent());
            assertEquals(1, contactService.findContactsByPhone("0123").get().size());
            assertEquals("Sample Kontakt", contactService.findContactsByPhone("0123").get().get(0).name());
        }

        @Test
        @DisplayName("byEmail")
        public void shouldSearchByEmail() throws Exception {

            contactService.addContact("Sample Kontakt", "524234243", "Testerstrasse 12", "sample@test.de");
            contactService.addContact("Sample Kontakt 2", "243242343", "Testerstrasse 23", "sample2@test.de");
            contactService.addContact("Sample Kontakt 3", "354353453", "Testerstrasse 1", "sample3@test.de");
            

            assertTrue(contactService.findContactsByEmail("test").isPresent());
            assertEquals(2, contactService.findContactsByEmail("test").get().size());
        }

        @Test
        @DisplayName("byAddress")
        public void shouldSearchByAddress() throws Exception {

            contactService.addContact("Sample Kontakt", "524234243", "Testerstrasse 12", "sample@test.de");
            contactService.addContact("Sample Kontakt 2", "243242343", "Testerstrasse 23", "sample2@test.de");
            contactService.addContact("Sample Kontakt 3", "354353453", "Teststrasse 1", "sample3@test.de");
            

            assertTrue(contactService.findContactsByAddresse("Testerstrasse").isPresent());
            assertEquals(2, contactService.findContactsByAddresse("Testerstrasse").get().size());
        }
    }

    @Nested
    @DisplayName("errors")
    class ErrorHandlingTests {
        
        @Test
        @DisplayName("delete notFound")
        public void shouldThrowExceptionWhenDeletingNonExistent() throws Exception {
            assertThrows(ContactNotFoundException.class, () -> 
                contactService.deleteContact("non-existent-id"));
        }

        @Test
        @DisplayName("validation empty")
        public void shouldThrowExceptionOnEmptyName() throws Exception {
            assertThrows(ValidationException.class, () -> 
                contactService.addContact("", "123", null, null));
        }

        @Test
        @DisplayName("validation null")
        public void shouldThrowExceptionOnNullName() throws Exception {
            assertThrows(ValidationException.class, () -> 
                contactService.addContact(null, "123", null, null));
        }
    }
}
