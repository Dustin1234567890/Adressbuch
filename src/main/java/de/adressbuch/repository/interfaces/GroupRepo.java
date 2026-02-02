package de.adressbuch.repository.interfaces;

import de.adressbuch.models.Group;
import java.util.Optional;
import java.util.List;

public interface GroupRepo {
    Group save(Group group);
    Group update(Group group);
    Optional<Group> deleteById(String id);
    Optional<Group> findById(String id);
    Optional<Group> findByName(String name);
    List<Group> findAll();
    void initializeDatabase();
}
