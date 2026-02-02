package de.adressbuch.util;

import java.util.Optional;
import java.util.UUID;

public class Utils {
    private Utils() {
    }

    public static Optional<String> convertToOptionalNonBlank(String value) {
        return Optional.ofNullable(value).filter(s -> !s.isBlank());
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
