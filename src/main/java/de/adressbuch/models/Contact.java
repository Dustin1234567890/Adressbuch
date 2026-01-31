package de.adressbuch.models;

import java.util.Objects;
import java.util.Optional;

//TODO: refactor to record - JUU 27/01
public final class Contact {
    private final Long id;
    private final String name;
    private final Optional<String> phoneNumber;
    private final Optional<String> address;
    private final Optional<String> email;

    private Contact(Long id, String name, Optional<String> phoneNumber, Optional<String> address, Optional<String> email) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Name nicht null!");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "Phone nicht null!");
        this.address = Objects.requireNonNull(address, "Address nicht null!");
        this.email = Objects.requireNonNull(email, "Email nicht null!");
    }

    public static Contact create(String name, Optional<String> phoneNumber, Optional<String> address, Optional<String> email) {
        return new Contact(
            null, 
            name, 
            phoneNumber,
            address,
            email);
    }

    public static Contact of(Long id, String name, Optional<String> phoneNumber, Optional<String> address, Optional<String> email) {
        return new Contact(
            id, 
            name, 
            phoneNumber,
            address,
            email);
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public String getName() {
        return name;
    }

    public Optional<String> getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<String> getAddress() {
        return address;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Contact withId(Long newId) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id) &&
               Objects.equals(name, contact.name) &&
               Objects.equals(phoneNumber, contact.phoneNumber) &&
               Objects.equals(address, contact.address) &&
               Objects.equals(email, contact.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phoneNumber, address, email);
    }

    @Override
    public String toString() {
        return String.format("Contact{id=%s, name='%s', phone=%s, address=%s, email=%s}",
                id, name, phoneNumber, address, email);
    }
}
