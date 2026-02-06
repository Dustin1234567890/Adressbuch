package de.adressbuch.service;

import java.util.List;
import java.util.Optional;

import de.adressbuch.models.Group;
import de.adressbuch.repository.interfaces.GroupRepo;
import de.adressbuch.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
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
        Group savedGroup = groupRepository.save(group);
        logger.info("Gruppe geaddet: {} ({})", savedGroup.name(), savedGroup.id());
        return savedGroup;
    }

    public Group updateGroup(String id, String name, String description) {
        Group existing = findGroupById(id).orElseThrow();
        validateGroupName(name);
        
        Group updated = new Group(
            id,
            name,
            Utils.convertToOptionalNonBlank(description)
        );
        Group updatedGroup = groupRepository.update(updated);
        logger.info("Gruppe geupdet: {} ({})", name, id);
        return updatedGroup;
    }

    public void deleteGroup(String id) {
        Optional<Group> deleted = groupRepository.deleteById(id);
        if (deleted.isEmpty()) {
            throw new IllegalArgumentException("Group not found with id: " + id);
        }
        logger.info("Gruppe gelöscht: {}", id);
    }

    public Optional<Group> findGroupById(String id) {
        Optional<Group> foundGroup = groupRepository.findById(id);
        if (foundGroup.isEmpty()) {
            logger.debug("Gruppe nicht gefunden: {}", id);
        }
        return foundGroup;
    }

    public Optional<List<Group>> findGroupByName(String name) {
        Optional<List<Group>> foundGroups = groupRepository.findByName(name);
        logger.debug("Suche nach Gruppe '{}': {} Gruppen gefunden", name, foundGroups.map(List::size).orElse(0));
        return foundGroups;
    }

    public List<Group> findAllGroups() {
        List<Group> foundGroups = groupRepository.findAll();
        logger.debug("{} Gruppen geladen", foundGroups.size());
        return foundGroups;
    }

    public void validateGroupName(String name) {
        if (name == null || name.isBlank()) {
            logger.warn("Gruppen-Namen Validierung fehlgeschlagen: name ist null oder empty");
            throw new IllegalArgumentException("Group name cannot be empty");
        }
    }
}
