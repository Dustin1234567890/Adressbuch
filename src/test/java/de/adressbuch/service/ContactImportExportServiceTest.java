package de.adressbuch.service;

import de.adressbuch.models.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
/*
@Disabled("Tests für Import/Export Feature")
public class ContactImportExportServiceTest {
    private ContactImportExportService importExportService;
    private Path tmp;

    @BeforeEach
    public void setup() throws IOException {
        importExportService = new ContactImportExportService();
        tmp = Files.createTempFile("contacts", ".csv");
    }

    @Test
    public void testExportContactsToCSV() throws IOException {
        Contact contact1 = Contact.create(
            "ToExport Sample", 
            Optional.of("524234243"),
            Optional.of("Testerstrasse 12"),
            Optional.of("sample@test.de"));
        Contact contact2 = Contact.create("ToExport Sample2", 
            Optional.of("243242343"),
            Optional.empty(),
            Optional.of("toexport2@test.de"));
        
        List<Contact> contacts = Arrays.asList(contact1, contact2);

        importExportService.exportToCSV(contacts, tmp.toString());
        assertTrue(Files.exists(tmp));
        String content = Files.readString(tmp);
        assertTrue(content.contains("ToExport Sample"));
        assertTrue(content.contains("ToExport Sample2"));
    }

    @Test
    public void testExportEmptyContactList() throws IOException {
        List<Contact> contacts = Arrays.asList();

        importExportService.exportToCSV(contacts, tempFile.toString());

        assertTrue(Files.exists(tempFile));
    }

    @Test
    public void testImportContactsFromCSV() throws IOException {
        String csvContent = "name,phone,address,email\n" +
            "Toimport Sample,524234243,Testerstrasse 12,sample@test.de\n" +
            "Toimport Sample2,243242343,,toexport2@test.de\n";
        
        Files.writeString(tmp, csvContent);

        List<Contact> result = importExportService.importFromCSV(tmp.toString());

        assertEquals(2, result.size());
        assertEquals("Toimport Sample", result.get(0).name());
        assertEquals("Toimport Sample2", result.get(1).name());
        assertEquals("524234243", result.get(0).phoneNumber().orElse(null));
    }

    @Test
    public void testImportContactsWithMissingFields() throws IOException {
        String csvContent = "name,phone,address,email\n" + "Sample Name,23423423,,\n";
        
        Files.writeString(tmp, csvContent);

        List<Contact> result = importExportService.importFromCSV(tmp.toString());
        assertEquals(1, result.size());
        assertEquals("Sample Name", result.get(0).name());
        assertTrue(result.get(0).phoneNumber().isPresent());
        assertFalse(result.get(0).address().isPresent());
    }

    @Test
    public void testImportifNonExistent() {
        assertThrows(IOException.class, () -> 
            importExportService.importFromCSV("/invalidpath/path/contacts.csv"));
    }

    @Test
    public void testExportImportDependency() throws IOException {
        Contact contact1 = Contact.create(
            "Sample Kontakt", 
            Optional.of("425245234"),
            Optional.of("Testerstrasse 123"),
            Optional.of("tester@test.de"));
        Contact contact2 = Contact.create(
            "Sample Kontakt 2", 
            Optional.of("423423433"),
            Optional.empty(),
            Optional.of("tester2@test.de"));
        
        List<Contact> originalContacts = Arrays.asList(contact1, contact2);

        importExportService.exportToCSV(originalContacts, tmpfile.toString());
        List<Contact> importedContacts = importExportService.importFromCSV(tmpfile.toString());

        assertEquals(2, importedContacts.size());
        assertEquals(originalContacts.get(0).name(), importedContacts.get(0).name());
        assertEquals(originalContacts.get(1).name(), importedContacts.get(1).name());
    }

    @Test
    public void testExportContactsWithSpecialChars() throws IOException {
        Contact contact = Contact.create(
            "%§, TEst!@#", 
            Optional.of("354353453"),
            Optional.of("Testerstrasse 23"),
            Optional.of("test@test.de"));
        
        List<Contact> contacts = Arrays.asList(contact);

        importExportService.exportToCSV(contacts, tempFile.toString());

        List<Contact> importedContacts = importExportService.importFromCSV(tempFile.toString());

        assertEquals("%§, TEst!@#", importedContacts.get(0).name());
    }
}
*/