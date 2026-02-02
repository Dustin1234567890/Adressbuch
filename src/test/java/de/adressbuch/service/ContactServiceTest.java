package de.adressbuch.service;

import de.adressbuch.models.Contact;
import de.adressbuch.repository.interfaces.ContactRepo;
import de.adressbuch.util.Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContactServiceTest {
    private ContactRepo contactRepository;
    private ContactService contactService;

    @BeforeEach
    public void setup() {
        contactRepository = mock(ContactRepo.class);
        contactService = new ContactService(contactRepository);
    }

    @Test
    public void testCreateContact() {
        String name = "Neuer Sample Kontakt";
        Optional<String> phone = Optional.of("424235263");
        Optional<String> address = Optional.empty();
        Optional<String> email = Optional.of("sample@tester.de");

        contactService.addContact(name, phone.orElse(null), address.orElse(null), email.orElse(null));

        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    public void testGetContactById() {
        String id = Utils.generateId();
        Contact contact = new Contact(id, "Neuer Sample Kontakt", Optional.of("424235263"), Optional.empty(), Optional.of("sample@tester.de"));
        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));

        Optional<Contact> result = contactService.findContactById(id);

        assertEquals("Neuer Sample Kontakt", result.get().name());
        verify(contactRepository, times(1)).findById(id);
    }

    @Test
    public void testGetContactByIdNotFound() {
        String id = Utils.generateId();
        when(contactRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> contactService.findContactById(id).orElseThrow(() -> new IllegalArgumentException("not found")));
    }

    @Test
    public void testDeleteContact() {
        String id = Utils.generateId();
        Contact contact = new Contact(id, "Neuer Sample Kontakt", Optional.of("424235263"), Optional.empty(), Optional.of("sample@tester.de"));
        when(contactRepository.deleteById(id)).thenReturn(Optional.of(contact));

        contactService.deleteContact(id);

        verify(contactRepository, times(1)).deleteById(id);
    }
}
