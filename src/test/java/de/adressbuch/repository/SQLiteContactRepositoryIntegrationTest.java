package de.adressbuch.repository;

import de.adressbuch.models.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("Integration test voeranst disabled")
public class SQLiteContactRepositoryIntegrationTest {
    private SQLiteContactRepo contactRepository;
    private static final String DB_URL = "jdbc:sqlite:file:memdb1?mode=memory&cache=shared";

    @BeforeEach
    public void setup() throws Exception {
        contactRepository = new SQLiteContactRepo(DB_URL);
    }

    @Test
    public void testSaveAndFindContact() {
        Contact contact = Contact.create(
            "Sample Contact", 
            Optional.of("3325234234"),
            Optional.of("Samplestrasse 5, 66534 Samplebrücken"),
            Optional.of("sample@trashmail.de"));
        
        contactRepository.save(contact);
        
        List<Contact> allContacts = contactRepository.findAll();
        assertFalse(allContacts.isEmpty());
        assertEquals("Sample Contact", allContacts.get(0).getName());
    }

    @Test
    public void testUpdateContact() {
        Contact contact = Contact.create(
            "Alter Name", 
            Optional.of("446452346"),
            Optional.empty(),
            Optional.empty());
        contactRepository.save(contact);
        
        List<Contact> allContacts = contactRepository.findAll();
        Contact saved = allContacts.get(0);
        
        Contact updated = saved.withName("Neuer Name");
        contactRepository.update(updated);
        
        Optional<Contact> retrieved = contactRepository.findById(saved.getId().get());
        assertTrue(retrieved.isPresent());
        assertEquals("Neuer Name", retrieved.get().getName());
    }

    @Test
    public void testDeleteContact() {
        Contact contact = Contact.create(
            "Temp", 
            Optional.empty(),
            Optional.empty(),
            Optional.empty());
        contactRepository.save(contact);
        
        List<Contact> before = contactRepository.findAll();
        int countBefore = before.size();
        
        Long id = before.get(0).getId().get();
        contactRepository.deleteById(id);
        
        List<Contact> after = contactRepository.findAll();
        assertEquals(countBefore - 1, after.size());
    }
}
