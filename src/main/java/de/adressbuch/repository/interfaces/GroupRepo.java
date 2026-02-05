package de.adressbuch.repository.interfaces;

import java.util.List;
import java.util.Optional;

import de.adressbuch.models.Group;

public interface GroupRepo {
    Group save(Group group);
    Group update(Group group);
    Optional<Group> deleteById(String id);
    Optional<Group> findById(String id);
    Optional<List<Group>> findByName(String name);
    List<Group> findAll();
    void initializeDatabase();
}
