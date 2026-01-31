package de.adressbuch.repository.interfaces;

import de.adressbuch.models.User;
import java.util.List;
import java.util.Optional;

public interface UserRepo {
    User save(User user);
    User update(User user);
    Optional<User> deleteById(Long id);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void initializeDatabase();
}
