package de.adressbuch.service;

import de.adressbuch.models.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ContactImportExportService {
    private static final Logger logger = LoggerFactory.getLogger(ContactImportExportService.class);

    public void exportToCSV(List<Contact> contacts, String filePath) throws IOException {
        logger.debug("TODO: Exportiere {} Kontakte nach: {}", contacts.size(), filePath);
        throw new UnsupportedOperationException("TODO: Implement CSV export");
    }

    public List<Contact> importFromCSV(String filePath) throws IOException {
        logger.debug("TODO: Importiere Kontakte aus: {}", filePath);
        throw new UnsupportedOperationException("TODO: Implement CSV import");
    }
}
