package de.adressbuch.repository.interfaces;

import de.adressbuch.models.Group;
import java.util.Optional;
import java.util.List;

public interface GroupRepo {
    Group save(Group group);
    Group update(Group group);
    Optional<Group> deleteById(Long id);
    Optional<Group> findById(Long id);
    Optional<Group> findByName(String name);
    List<Group> findAll();
    void initializeDatabase();
}
