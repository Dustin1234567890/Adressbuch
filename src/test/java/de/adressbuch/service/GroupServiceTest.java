package de.adressbuch.service;

import de.adressbuch.models.Group;
import de.adressbuch.repository.interfaces.GroupRepo;
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
        Group group = Group.of(1L, "Test Group", Optional.of("Group for testing"));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        Optional<Group> result = groupService.findGroupById(1L);

        assertEquals("Test Group", result.get().getName());
        verify(groupRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetGroupByIdNotFound() {
        when(groupRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> groupService.findGroupById(999L).orElseThrow(() -> new IllegalArgumentException("Not found")));
    }

    @Test
    public void testDeleteGroup() {
        Group group = Group.of(1L, "Test Group", Optional.empty());
        when(groupRepository.deleteById(1L)).thenReturn(Optional.of(group));

        groupService.deleteGroup(1L);

        verify(groupRepository, times(1)).deleteById(1L);
    }
}
