package de.adressbuch.models;

import java.util.Objects;
import java.util.Optional;

public record Contact (
    String id,
    String name,
    Optional<String> phoneNumber,
    Optional<String> address,
    Optional<String> email
) {
    public Contact {
        Objects.requireNonNull(name, "Name should never be null!");
    }

    public Contact withId(String newId) {
        return new Contact(newId, name, phoneNumber, address, email);
    }

    public Contact withName(String newName) {
        return new Contact(id, newName, phoneNumber, address, email);
    }

    public Contact withPhoneNumber(Optional<String> newPhoneNumber) {
        return new Contact(id, name, newPhoneNumber, address, email);
    }

    public Contact withAddress(Optional<String> newAddress) {
        return new Contact(id, name, phoneNumber, newAddress, email);
    }

    public Contact withEmail(Optional<String> newEmail) {
        return new Contact(id, name, phoneNumber, address, newEmail);
    }
}
