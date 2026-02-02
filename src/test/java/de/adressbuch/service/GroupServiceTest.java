package de.adressbuch.service;

import de.adressbuch.models.Group;
import de.adressbuch.repository.interfaces.GroupRepo;
import de.adressbuch.util.Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GroupServiceTest {
    private GroupRepo groupRepository;
    private GroupService groupService;

    @BeforeEach
    public void setup() {
        groupRepository = mock(GroupRepo.class);
        groupService = new GroupService(groupRepository);
    }

    @Test
    public void testCreateGroup() {
        String name = "Test Group";
        Optional<String> description = Optional.of("Group for testing");

        groupService.addGroup(name, description.orElse(null));

        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    public void testGetGroupById() {
        String id = Utils.generateId();
        Group group = new Group(id, "Test Group", Optional.of("Group for testing"));
        when(groupRepository.findById(id)).thenReturn(Optional.of(group));

        Optional<Group> result = groupService.findGroupById(id);

        assertEquals("Test Group", result.get().name());
        verify(groupRepository, times(1)).findById(id);
    }

    @Test
    public void testGetGroupByIdNotFound() {
        when(groupRepository.findById(Utils.generateId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> groupService.findGroupById(Utils.generateId()).orElseThrow(() -> new IllegalArgumentException("Not found")));
    }

    @Test
    public void testDeleteGroup() {
        String id = Utils.generateId();
        Group group = new Group(id, "Test Group", Optional.empty());
        when(groupRepository.deleteById(id)).thenReturn(Optional.of(group));

        groupService.deleteGroup(id);

        verify(groupRepository, times(1)).deleteById(id);
    }
}
