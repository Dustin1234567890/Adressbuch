package de.adressbuch.service;

import de.adressbuch.models.Group;
import de.adressbuch.repository.interfaces.GroupRepo;
import de.adressbuch.util.Utils;

import java.util.List;
import java.util.Optional;

public class GroupService {
    private final GroupRepo groupRepository;

    public GroupService(GroupRepo groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group addGroup(String name, String description) {
        validateGroupName(name);
        Group group = new Group(
            Utils.generateId(),
            name, 
            Utils.convertToOptionalNonBlank(description)
        );
        return groupRepository.save(group);
    }

    public Group updateGroup(String id, String name, String description) {
        Group existing = findGroupById(id).orElseThrow();
        validateGroupName(name);
        
        Group updated = new Group(
            id,
            name,
            Utils.convertToOptionalNonBlank(description)
        );
        return groupRepository.update(updated);
    }

    public boolean deleteGroup(String id) {
        Optional<Group> deleted = groupRepository.deleteById(id);
        if (deleted.isEmpty()) {
            throw new IllegalArgumentException("Group not found with id: " + id);
        }
        return true;
    }

    public Optional<Group> findGroupById(String id) {
        return groupRepository.findById(id);
    }

    public Optional<Group> findGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public void validateGroupName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Group name cannot be empty");
        }
    }
}
