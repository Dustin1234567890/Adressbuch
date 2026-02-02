package de.adressbuch.models;

import java.util.Objects;
import java.util.Optional;

public record User (
    String id,
    String username,
    Optional<String> displayedName) {
    
    public User {
        Objects.requireNonNull(id, "ID is not allowed to be null");
        Objects.requireNonNull(username, "Name is not allowed to be null");
        Objects.requireNonNull(displayedName, "displayedName is not allowed to be null");
    }

    public User withId(String i) {
        return new User(i, username, displayedName);
    }

    public User withName(String n) {
        return new User(id, n, displayedName);
    }

    public User withDisplayedName(Optional<String> d) {
        return new User(id, username, d);
    }
}
