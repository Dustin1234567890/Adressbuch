package de.adressbuch.service;

import de.adressbuch.models.Contact;
import de.adressbuch.models.Group;
import de.adressbuch.repository.interfaces.ContactGroupRepo;
import de.adressbuch.repository.interfaces.ContactRepo;
import de.adressbuch.repository.interfaces.GroupRepo;
import de.adressbuch.util.Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GroupServiceIntegrationTest {

    private GroupRepo groupRepository;
    private GroupService groupService;
    
@BeforeEach
    public void setup() {
        groupRepository = mock(GroupRepo.class);
        groupService = new GroupService(groupRepository);
    }

    @Test
    public void testCreateGroup() {
        String name1 = "Test Group1";
        Optional<String> description1 = Optional.of("First Group Created");

        groupService.addGroup(name1, description1.orElse(null));

        verify(groupRepository, times(1)).save(any(Group.class));

        String name2 = "Test Group2";
        Optional<String> description2 = Optional.of("First Group Created");

        groupService.addGroup(name2, description2.orElse(null));

        verify(groupRepository, times(1)).save(any(Group.class));

        String id = Utils.generateId();
        Group group = new Group(id, "Deleted Group", Optional.empty());
        when(groupRepository.deleteById(id)).thenReturn(Optional.of(group));

        groupService.deleteGroup(id);

        verify(groupRepository, times(1)).deleteById(id);
    }

}
