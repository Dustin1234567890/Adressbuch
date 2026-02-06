package de.adressbuch.exception;

public class ContactNotFoundException extends EntityNotFoundException {
    public ContactNotFoundException(String id) {
        super("Kontakt mit ID " + id + " nicht gefunden");
    }
}
