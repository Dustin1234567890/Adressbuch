package de.adressbuch.service;

import de.adressbuch.exception.ContactNotFoundException;
import de.adressbuch.exception.ValidationException;
import de.adressbuch.models.Contact;
import de.adressbuch.repository.interfaces.ContactRepo;
import de.adressbuch.util.Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("ContactService unit")
class ContactServiceTest {
    private ContactRepo contactRepositoryMock;
    private ContactService contactService;

    @BeforeEach
    void setup() throws Exception {
        contactRepositoryMock = mock(ContactRepo.class);
        contactService = new ContactService(contactRepositoryMock);
    }

    @Nested
    @DisplayName("add")
    class AddContactTests {
        
        @Test
        @DisplayName("basic")
        void shouldAddNewContact() throws Exception {
            String name = "Sample Kontakt";
            String phone = "524234243";
            String address = "Testerstrasse 12";
            String email = "sample@test.de";
            Contact contact = new Contact(
                "test-id",
                name, 
                Optional.of(phone), 
                Optional.of(address), 
                Optional.of(email)
            );
            when(contactRepositoryMock.save(any(Contact.class))).thenReturn(contact);
            
            contactService.addContact(name, phone, address, email);
            
            verify(contactRepositoryMock, times(1)).save(any(Contact.class));
        }

        @Test
        @DisplayName("optional fields")
        void shouldAddContactWithOptionalFields() throws Exception {
            String name = "Sample Kontakt";
            String phone = "524234243";
            Contact contact = new Contact(
                "test-id", 
                name, 
                Optional.of(phone), 
                Optional.empty(), 
                Optional.empty()
            );
            when(contactRepositoryMock.save(any(Contact.class))).thenReturn(contact);
            
            contactService.addContact(name, phone, null, null);
            
            verify(contactRepositoryMock, times(1)).save(any(Contact.class));
        }

        @Test
        @DisplayName("throwsException empty name")
        void shouldThrowExceptionForEmptyName() throws Exception {
            String name = "";
            
            assertThrows(ValidationException.class, () ->
                contactService.addContact(name, "123", null, null));
        }

        @Test
        @DisplayName("throwsException null name")
        void shouldThrowExceptionForNullName() throws Exception {
            assertThrows(ValidationException.class, () ->
                contactService.addContact(null, "123", null, null));
        }
    }

    @Nested
    @DisplayName("find")
    class FindContactTests {
        
        @Test
        @DisplayName("findById")
        void shouldFindContactById() throws Exception {
            String id = Utils.generateId();
            Contact contact = new Contact(
                id, 
                "Sample Kontakt",
                Optional.of("524234243"), 
                Optional.empty(), 
                Optional.of("sample@test.de")
            );
            when(contactRepositoryMock.findById(id)).thenReturn(Optional.of(contact));
            
            Optional<Contact> foundContactCheck = contactService.findContactById(id);
            assertTrue(foundContactCheck.isPresent());
            assertEquals("Sample Kontakt", foundContactCheck.get().name());
            verify(contactRepositoryMock, times(1)).findById(id);
        }

        @Test
        @DisplayName("notFound")
        void shouldReturnEmptyIfContactNotFound() throws Exception {

            String id = Utils.generateId();
            when(contactRepositoryMock.findById(id)).thenReturn(Optional.empty());
            

            assertFalse(contactService.findContactById(id).isPresent());
            verify(contactRepositoryMock, times(1)).findById(id);
        }

        @Test
        @DisplayName("findAll")
        void shouldFindAllContacts() throws Exception {

            Contact contact1 = new Contact(
                Utils.generateId(), 
                "Sample Kontakt", 
                Optional.empty(), 
                Optional.empty(), 
                Optional.empty()
            );
            Contact contact2 = new Contact(
                Utils.generateId(), 
                "Sample Kontakt 2", 
                Optional.empty(), 
                Optional.empty(), 
                Optional.empty()
            );
            List<Contact> contacts = Arrays.asList(contact1, contact2);
            when(contactRepositoryMock.findAll()).thenReturn(contacts);
            

            assertEquals(2, contactService.findAllContacts().size());
            verify(contactRepositoryMock, times(1)).findAll();
        }

        @Test
        @DisplayName("findAll empty")
        void shouldReturnEmptyListIfNoContacts() throws Exception {

            when(contactRepositoryMock.findAll()).thenReturn(Collections.emptyList());
            

            assertTrue(contactService.findAllContacts().isEmpty());
        }

        @Test
        @DisplayName("findByName")
        void shouldFindContactsByName() throws Exception {
            Contact contact = new Contact(
                Utils.generateId(), 
                "Sample Kontakt", 
                Optional.empty(), 
                Optional.empty(), 
                Optional.empty()
            );
            when(contactRepositoryMock.findByName("Sample")).thenReturn(
                Optional.of(Arrays.asList(contact))
            );
            
            Optional<List<Contact>> foundContactCheck = contactService.findContactsByName("Sample");
            assertTrue(foundContactCheck.isPresent());
            assertEquals(1, foundContactCheck.get().size());
            verify(contactRepositoryMock, times(1)).findByName("Sample");
        }

        @Test
        @DisplayName("findByPhone")
        void shouldFindContactsByPhone() throws Exception {
            Contact contact = new Contact(
                Utils.generateId(), 
                "Sample Kontakt", 
                Optional.of("524234243"), 
                Optional.empty(), 
                Optional.empty()
            );
            when(contactRepositoryMock.findByPhone("5242")).thenReturn(
                Optional.of(Arrays.asList(contact))
            );
            

            assertTrue(contactService.findContactsByPhone("5242").isPresent());
            assertEquals(1, contactService.findContactsByPhone("5242").get().size());
        }
    }

    @Nested
    @DisplayName("update")
    class UpdateContactTests {
        
        @Test
        @DisplayName("basic")
        void shouldUpdateContact() throws Exception {
            String id = Utils.generateId();
            String newName = "Sample Kontakt 2";
            Contact contact = new Contact(
                id, 
                newName, 
                Optional.of("243242343"), 
                Optional.of("Testerstrasse 23"), 
                Optional.of("sample2@test.de")
            );
            when(contactRepositoryMock.findById(id)).thenReturn(Optional.of(contact));
            
            contactService.updateContact(id, newName, "243242343", "Testerstrasse 23", "sample2@test.de");
            

            verify(contactRepositoryMock, times(1)).update(any(Contact.class));
        }

        @Test
        @DisplayName("throwsException empty")
        void shouldThrowExceptionForEmptyNameOnUpdate() throws Exception {

            String id = Utils.generateId();
            when(contactRepositoryMock.findById(id)).thenReturn(Optional.empty());
            
            assertThrows(ContactNotFoundException.class, () -> 
                contactService.updateContact(id, "", "123", null, null));
        }
    }

    @Nested
    @DisplayName("delete")
    class DeleteContactTests {
        
        @Test
        @DisplayName("basic")
        void shouldDeleteContact() throws Exception {

            String id = Utils.generateId();
            Contact contact = new Contact(
                id, 
                "Sample Kontakt", 
                Optional.empty(), 
                Optional.empty(), 
                Optional.empty()
            );
            when(contactRepositoryMock.deleteById(id)).thenReturn(Optional.of(contact));
            

            contactService.deleteContact(id);
            

            verify(contactRepositoryMock, times(1)).deleteById(id);
        }

        @Test
        @DisplayName("throwsException notFound")
        void shouldThrowExceptionIfContactNotFound() throws Exception {

            String id = Utils.generateId();
            when(contactRepositoryMock.deleteById(id)).thenReturn(Optional.empty());
            
            assertThrows(ContactNotFoundException.class, () -> 
                contactService.deleteContact(id));
        }
    }
}
