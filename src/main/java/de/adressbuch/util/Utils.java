package de.adressbuch.util;

import java.util.Optional;

public class Utils {
    private Utils() {
    }

    public static Optional<String> convertToOptionalNonBlank(String value) {
        return Optional.ofNullable(value).filter(s -> !s.isBlank());
    }
}
