package de.adressbuch.service;

import de.adressbuch.models.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ContactImportExport unit")
class ContactImportExportServiceTest {
    private ContactImportExportService service;
    private Path tempDir;

    @BeforeEach
    void setup() throws IOException {
        service = new ContactImportExportService();
        tempDir = Files.createTempDirectory("test-contacts");
    }

    @Nested
    @DisplayName("export")
    class ExportTests {
        
        @Disabled("Disabled until Import and Export works")
        @Test
        @DisplayName("basic")
        void shouldExportContactsToCSV() throws IOException {

            Contact contact1 = new Contact(
                "id1", "Sample Kontakt",
                Optional.of("524234243"),
                Optional.of("Testerstrasse 12"),
                Optional.of("sample@test.de")
            );
            Contact contact2 = new Contact(
                "id2", "Sample Kontakt 2",
                Optional.empty(),
                Optional.empty(),
                Optional.of("sample2@test.de")
            );
            List<Contact> contacts = Arrays.asList(contact1, contact2);
            Path csvFile = tempDir.resolve("contacts.csv");
            

            service.exportToCSV(contacts, csvFile.toString());
            

            assertTrue(Files.exists(csvFile), "CSV-Datei sollte existieren");
            assertEquals(3, Files.readAllLines(csvFile).size(), "Header + 2 Kontakte");
            assertTrue(Files.readAllLines(csvFile).get(0).contains("id,name,phone,address,email"), "Header sollte korrekt sein");
            assertTrue(Files.readAllLines(csvFile).get(1).contains("Sample Kontakt"), "Erster Kontakt sollte in Zeile 2 sein");
            assertTrue(Files.readAllLines(csvFile).get(2).contains("Sample Kontakt 2"), "Zweiter Kontakt sollte in Zeile 3 sein");
        }

        @Disabled("Disabled until Import and Export works")
        @Test
        @DisplayName("empty")
        void shouldExportEmptyContactList() throws IOException {

            Path csvFile = tempDir.resolve("empty.csv");
            

            service.exportToCSV(Arrays.asList(), csvFile.toString());
            

            assertTrue(Files.exists(csvFile), "CSV-Datei sollte existieren");
            assertEquals(1, Files.readAllLines(csvFile).size(), "Nur Header");
            assertEquals("id,name,phone,address,email", Files.readAllLines(csvFile).get(0));
        }

        @Disabled("Disabled until Import and Export works")
        @Test
        @DisplayName("escapeSpecial")
        void shouldEscapeSpecialCharacters() throws IOException {

            Contact contact = new Contact(
                "id1", "Name, mit Komma",
                Optional.of("123"),
                Optional.of("Strasse \"Hauptstrasse\" 5"),
                Optional.of("test@example.com")
            );
            Path csvFile = tempDir.resolve("special.csv");
            

            service.exportToCSV(Arrays.asList(contact), csvFile.toString());
            

            assertTrue(Files.readAllLines(csvFile).get(1).contains("\"Name, mit Komma\""), "Komma sollte gequotet sein");
            assertTrue(Files.readAllLines(csvFile).get(1).contains("\"Strasse"), "Anführungszeichen sollte escapt sein");
        }
    }

    @Nested
    @DisplayName("import")
    class ImportTests {
        
        @Disabled("Disabled until Import and Export works")
        @Test
        @DisplayName("basic")
        void shouldImportContactsFromCSV() throws IOException {

            Path csvFile = tempDir.resolve("import.csv");
            String csvContent = "id,name,phone,address,email\n" +
                    "id1,Sample Kontakt,524234243,Testerstrasse 12,sample@test.de\n" +
                    "id2,Sample Kontakt 2,,,,sample2@test.de\n";
            Files.writeString(csvFile, csvContent);
            

            List<Contact> contacts = service.importFromCSV(csvFile.toString());
            

            assertEquals(2, contacts.size(), "Sollte 2 Kontakte importieren");
            assertEquals("Sample Kontakt", contacts.get(0).name());
            assertEquals("524234243", contacts.get(0).phoneNumber().orElse(null));
            assertEquals("Sample Kontakt 2", contacts.get(1).name());
            assertFalse(contacts.get(1).phoneNumber().isPresent());
        }

        @Disabled("Disabled until Import and Export works")
        @Test
        @DisplayName("empty")
        void shouldImportEmptyCSV() throws IOException {

            Path csvFile = tempDir.resolve("empty.csv");
            Files.writeString(csvFile, "id,name,phone,address,email\n");
            

            assertTrue(service.importFromCSV(csvFile.toString()).isEmpty(), "Sollte leere Liste sein");
        }

        @Disabled("Disabled until Import and Export works")
        @Test
        @DisplayName("throwsException notFound")
        void shouldThrowIOExceptionForNonExistentFile() {

            String nonExistentPath = tempDir.resolve("nonexistent.csv").toString();
            

            assertThrows(IOException.class, () -> service.importFromCSV(nonExistentPath));
        }

        @Disabled("Disabled until Import and Export works")
        @Test
        @DisplayName("unescapeSpecial")
        void shouldUnescapeSpecialCharacters() throws IOException {

            Path csvFile = tempDir.resolve("special.csv");
            String csvContent = "id,name,phone,address,email\n" +
                    "id1,\"Name, mit Komma\",123,\"Strasse \"\"Hauptstrasse\"\" 5\",test@example.com\n";
            Files.writeString(csvFile, csvContent);
            

            List<Contact> contacts = service.importFromCSV(csvFile.toString());
            

            assertEquals(1, contacts.size());
            assertEquals("Name, mit Komma", contacts.get(0).name());
            assertEquals("Strasse \"Hauptstrasse\" 5", contacts.get(0).address().orElse(null));
        }
    }

    @Nested
    @DisplayName("roundtrip")
    class RoundTripTests {
        
        @Disabled("Disabled until Import and Export works")
        @Test
        @DisplayName("consistent")
        void shouldBeConsistentWithRoundTrip() throws IOException {

            Contact contact1 = new Contact(
                "id1", "Sample Kontakt",
                Optional.of("524234243"),
                Optional.of("Testerstrasse 12"),
                Optional.of("sample@test.de")
            );
            Contact contact2 = new Contact(
                "id2", "Sample Kontakt 2",
                Optional.empty(),
                Optional.of("Testerstrasse 123"),
                Optional.empty()
            );
            List<Contact> original = Arrays.asList(contact1, contact2);
            Path csvFile = tempDir.resolve("roundtrip.csv");
            

            service.exportToCSV(original, csvFile.toString());
            
            List<Contact> imported = service.importFromCSV(csvFile.toString());

            assertEquals(2, imported.size());
            assertEquals(original.get(0).name(), imported.get(0).name());
            assertEquals(original.get(0).phoneNumber(), imported.get(0).phoneNumber());
            assertEquals(original.get(1).name(), imported.get(1).name());
            assertEquals(original.get(1).address(), imported.get(1).address());
        }
    }
}
