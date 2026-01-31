package de.adressbuch.models;

import java.util.Objects;
import java.util.Optional;

//TODO: turn into record weil alles wieder dem selben Pattern folgt - 27/01 JUU
public final class Group {
    private final Long id;
    private final String name;
    private Optional<String> description;

    private Group(Long id, String name, Optional<String> description) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Name nicht null!");
        this.description = Objects.requireNonNull(description, "Beschreibung nicht null!");
    }

    public static Group create(String name, Optional<String> description) {
        return new Group(
            null, 
            name, 
            description);
    }

    public static Group of(Long id, String name, Optional<String> description) {
        return new Group(
            id, 
            name, 
            description);
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public String getName() {
        return name;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public Group withId(Long i) {
        return new Group(i, name, description);
    }

    public Group withName(String n) {
        return new Group(id, n, description);
    }

    public Group withDescription(Optional<String> d) {
        return new Group(id, name, d);
    }
}