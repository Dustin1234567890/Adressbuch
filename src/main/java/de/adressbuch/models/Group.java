package de.adressbuch.models;

import java.util.Objects;
import java.util.Optional;

public record Group (
    String id,
    String name,
    Optional<String> description
) {
    
    public Group {
        Objects.requireNonNull(name, "Name should never be null!");
    }

    public Group withId(String i) {
        return new Group(i, name, description);
    }

    public Group withName(String n) {
        return new Group(id, n, description);
    }

    public Group withDescription(Optional<String> d) {
        return new Group(id, name, d);
    }
}