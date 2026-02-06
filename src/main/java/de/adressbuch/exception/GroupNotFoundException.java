package de.adressbuch.exception;

public class GroupNotFoundException extends EntityNotFoundException {
    public GroupNotFoundException(String id) {
        super("Gruppe mit ID " + id + " nicht gefunden");
    }
}
