package de.adressbuch.models;

import java.util.Objects;
import java.util.Optional;

//TODO: refactor to record JUU 28/01
public final class User {
    private final Long id;
    private final String username;
    private final Optional<String> displayedName;

    private User(Long id, String username, Optional<String> displayedName) {
        this.id = id;
        this.username = Objects.requireNonNull(username, "Username nicht null!");
        this.displayedName = displayedName;
    }

    public static User create(String username, String displayedName) {
        return new User(
            null, 
            username, 
            Optional.ofNullable(displayedName));
    }

    public static User of(Long id, String username, String displayedName) {
        return new User(
            id, 
            username, 
            Optional.ofNullable(displayedName));
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public String getUserName() {
        return username;
    }

    public Optional<String> getDisplayedName() {
        return displayedName;
    }

    public String getActiveName() {
        return displayedName.orElse(username);
    }

    public User withId(Long newid) {
        return new User(newid, username, displayedName);
    }

    public User withUsername(String newUsername) {
        return new User(id, newUsername, displayedName);
    }

    public User withDisplayedName(String newDisplayedName) {
        return new User(id, username, Optional.ofNullable(newDisplayedName));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
               Objects.equals(username, user.username) &&
               Objects.equals(displayedName, user.displayedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, displayedName);
    }

    @Override
    public String toString() {
        return String.format("User{id=%s, username='%s', displayedName=%s}", 
                id, username, displayedName);
    }
}
