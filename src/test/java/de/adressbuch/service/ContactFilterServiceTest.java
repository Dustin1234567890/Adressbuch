package de.adressbuch.service;

import de.adressbuch.models.Contact;
import de.adressbuch.repository.interfaces.ContactGroupRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/*
@Disabled("Tests für Filter Feature")
public class ContactFilterServiceTest {
    private ContactGroupRepo contactGroupRepo;
    private ContactFilterService filterService;

    @BeforeEach
    public void setup() {
        contactGroupRepo = mock(ContactGroupRepo.class);
        filterService = new ContactFilterService(contactGroupRepo);
    }

    @Test
    public void testFindContactsByGroupId() {
        String groupId = 1L;
        List<String> contactIds = Arrays.asList(1L, 2L, 3L);
        
        when(contactGroupRepo.findContactIdsByGroupId(groupId)).thenReturn(contactIds);

        List<String> result = filterService.findContactsByGroupId(groupId);

        assertEquals(3, result.size());
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));
        assertTrue(result.contains(3L));
        verify(contactGroupRepo, times(1)).findContactIdsByGroupId(groupId);
    }

    @Test
    public void testFindContactsByGroupIdEmpty() {
        String groupId = 999L;
        
        when(contactGroupRepo.findContactIdsByGroupId(groupId)).thenReturn(Arrays.asList());

        List<String> result = filterService.findContactsByGroupId(groupId);

        assertTrue(result.isEmpty());
        verify(contactGroupRepo, times(1)).findContactIdsByGroupId(groupId);
    }

    @Test
    public void testIsContactInGroup() {
        String contactId = 1L;
        String groupId = 1L;
        
        when(contactGroupRepo.isContactInGroup(contactId, groupId)).thenReturn(true);

        boolean result = filterService.isContactInGroup(contactId, groupId);

        assertTrue(result);
        verify(contactGroupRepo, times(1)).isContactInGroup(contactId, groupId);
    }

    @Test
    public void testIsContactNotInGroup() {
        String contactId = 1L;
        String groupId = 2L;
        
        when(contactGroupRepo.isContactInGroup(contactId, groupId)).thenReturn(false);

        boolean result = filterService.isContactInGroup(contactId, groupId);

        assertFalse(result);
        verify(contactGroupRepo, times(1)).isContactInGroup(contactId, groupId);
    }

    @Test
    public void testAddContactToGroup() {
        String contactId = 1L;
        String groupId = 1L;
        
        filterService.addContactToGroup(contactId, groupId);
        
        verify(contactGroupRepo, times(1)).addContactToGroup(contactId, groupId);
    }

    @Test
    public void testRemoveContactFromGroup() {
        String contactId = 1L;
        String groupId = 1L;
        
        filterService.removeContactFromGroup(contactId, groupId);
        
        verify(contactGroupRepo, times(1)).removeContactFromGroup(contactId, groupId);
    }
}
*/