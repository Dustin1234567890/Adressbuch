package de.adressbuch.service;

import de.adressbuch.models.User;
import de.adressbuch.repository.interfaces.UserRepo;
import de.adressbuch.util.Utils;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepo userRepository;

    public UserService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(String username, String displayedName) {
        validateUsername(username);
        User user = new User(
            Utils.generateId(),
            username,
            Utils.convertToOptionalNonBlank(displayedName));
        return userRepository.save(user);
    }

    public User updateUser(String id, String username, String displayedName) {
        validateUsername(username);
        User user = new User(
            id,
            username,
            Utils.convertToOptionalNonBlank(displayedName));
        return userRepository.update(user);
    }

    public boolean deleteUser(String id) {
        Optional<User> deleted = userRepository.deleteById(id);
        if (deleted.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        return true;
    }

    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
    }
}
