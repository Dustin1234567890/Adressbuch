package de.adressbuch.service;

import de.adressbuch.models.Contact;
import de.adressbuch.repository.interfaces.ContactRepo;
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
        Contact contact = Contact.of(1L, "Neuer Sample Kontakt", Optional.of("424235263"), Optional.empty(), Optional.of("sample@tester.de"));
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        Optional<Contact> result = contactService.findContactById(1L);

        assertEquals("Neuer Sample Kontakt", result.get().getName());
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetContactByIdNotFound() {
        when(contactRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> contactService.findContactById(999L).orElseThrow(() -> new IllegalArgumentException("not found")));
    }

    @Test
    public void testDeleteContact() {
        Contact contact = Contact.of(1L, "Neuer Sample Kontakt", Optional.of("424235263"), Optional.empty(), Optional.of("sample@tester.de"));
        when(contactRepository.deleteById(1L)).thenReturn(Optional.of(contact));

        contactService.deleteContact(1L);

        verify(contactRepository, times(1)).deleteById(1L);
    }
}
