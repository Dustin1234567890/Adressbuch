package de.adressbuch.util;

import java.util.Optional;
import java.util.UUID;

import de.adressbuch.models.Contact;
import de.adressbuch.models.Group;
import org.jooq.Record;

public class Utils {
    private Utils() {
    }

    public static Optional<String> convertToOptionalNonBlank(String value) {
        return Optional.ofNullable(value).filter(s -> !s.isBlank());
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static Contact recordToContact(Record record) {
        return new Contact(
            record.get("id", String.class),
            record.get("name", String.class),
            convertToOptionalNonBlank(record.get("phone", String.class)),
            convertToOptionalNonBlank(record.get("address", String.class)),
            convertToOptionalNonBlank(record.get("email", String.class))
        );
    }

    public static Group recordToGroup(Record record) {
        return new Group(
            record.get("id", String.class),
            record.get("name", String.class),
            convertToOptionalNonBlank(record.get("description", String.class))
        );
    }
}
