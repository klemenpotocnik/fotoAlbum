package fotoAlbum.fotoAlbum.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

/** Literal substring search over the owner's already authorized photographs. */
final class PhotoSearch {
    private PhotoSearch() {}

    static String normalize(String query) {
        String text = query == null ? "" : query.strip();
        if (text.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Iskalni niz lahko vsebuje največ 100 znakov.");
        }
        return text.toLowerCase(Locale.ROOT);
    }

    static boolean matches(String title, String description, String query) {
        return query.isEmpty() || contains(title, query) || contains(description, query);
    }

    private static boolean contains(String text, String query) {
        return text != null && text.toLowerCase(Locale.ROOT).contains(query);
    }
}
