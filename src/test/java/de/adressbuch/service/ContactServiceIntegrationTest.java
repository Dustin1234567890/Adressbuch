package de.adressbuch.service;

import de.adressbuch.models.Contact;
import de.adressbuch.repository.interfaces.ContactGroupRepo;
import de.adressbuch.repository.interfaces.ContactRepo;
import de.adressbuch.util.Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ContactServiceIntegrationTest {

    private ContactRepo contactRepository;
    private ContactService contactService;

    @BeforeEach
    public void setup() {
        contactRepository = mock(ContactRepo.class);
        contactService = new ContactService(contactRepository);
    }
    
@Test
public void contactTest() {
        String name1 = "Erster Kontakt";
        Optional<String> phone1 = Optional.of("+491238976522");
        Optional<String> address1 = Optional.empty();
        Optional<String> email1 = Optional.of("eins@tester.de");

        contactService.addContact(name1, phone1.orElse(null), address1.orElse(null), email1.orElse(null));

        verify(contactRepository, times(1)).save(any(Contact.class));

        String name2 = "Neuer Sample Kontakt";
        Optional<String> phone2 = Optional.of("424235263");
        Optional<String> address2 = Optional.empty();
        Optional<String> email2 = Optional.of("sample@tester.de");

        contactService.addContact(name2, phone2.orElse(null), address2.orElse(null), email2.orElse(null));

        verify(contactRepository, times(2)).save(any(Contact.class));

        String idDelete = Utils.generateId();
        String nameDelete = "Delete This";
        Optional<String> phoneDelete = Optional.of("+499801256309");
        Optional<String> addressDelete = Optional.empty();
        Optional<String> emailDelete = Optional.of("delete@tester.de");
        
        Contact contactDeleted = new Contact(idDelete, nameDelete, phoneDelete, addressDelete, emailDelete);
        when(contactRepository.deleteById(idDelete)).thenReturn(Optional.of(contactDeleted));

        contactService.deleteContact(idDelete);

        verify(contactRepository, times(1)).deleteById(idDelete);
}

}
