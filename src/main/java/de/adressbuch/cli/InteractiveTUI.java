package de.adressbuch.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import de.adressbuch.injection.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.adressbuch.models.Contact;
import de.adressbuch.models.Group;
import de.adressbuch.service.ContactGroupService;
import de.adressbuch.service.ContactService;
import de.adressbuch.service.GroupService;

public class InteractiveTUI {
    private static final Logger logger = LoggerFactory.getLogger(InteractiveTUI.class);
    private final ContactService contactService;
    private final GroupService groupService;
    private static ContactGroupService contactGroupService;
    private boolean running = true;
    private boolean runningContacts = true;
    private boolean runningGroups = true;
    private final Scanner scanner;

    public InteractiveTUI() {
        this(new Scanner(System.in));
    }

    public InteractiveTUI(Scanner scanner) {
        this.scanner = scanner;
        ServiceFactory factory = ServiceFactory.getInstance();
        this.contactService = factory.getContactService();
        this.groupService = factory.getGroupService();
        this.contactGroupService = factory.getContactGroupService();
    }

    public void start() {
        logger.info("[TUI] Adressbuch gestartet");
        System.out.println("Willkommen im Adressbuch!\n" +
            "Bitte wählen Sie eine Option aus dem Hauptmenü, um fortzufahren.");

        while (running) {
            displayMenu();
            System.out.print("> ");
            String choice = scanner.nextLine().trim();
            logger.debug("[TUI] Hauptmenü-Auswahl: {}", choice);
            processChoice(choice);
        }
        logger.info("[TUI] Adressbuch beendet");
    }

    private void displayMenu() {
        System.out.println("=== Hauptmenü ===");
        System.out.println("1. Kontaktverwaltung");
        System.out.println("2. Gruppenverwaltung");
        System.out.println("3. Exit");
    }

    private void processChoice(String choice) {
        switch (choice) {
            case "1" -> contactCommands();
            case "2" -> groupCommands();
            case "3" -> running = false;
            default -> {System.out.println("\"" + choice + "\" ist keine gültige Menüoption.");
                        System.out.println("Bitte wählen Sie eine Zahl zwischen 1 und 3.");}
        }
    }

    private void contactCommands() {
        runningContacts = true;

        while (runningContacts) {
            displayMenuContacts();
            System.out.print("> ");
            String choice = scanner.nextLine().trim();
            processChoiceContacts(choice);
        }
    }

    private void displayMenuContacts() {
        System.out.println("=== Kontaktverwaltung ===");
        System.out.println("1. Kontakte anzeigen");
        System.out.println("2. Kontakt hinzufügen");
        System.out.println("3. Kontakt aktualisieren");
        System.out.println("4. Kontakt suchen");
        System.out.println("5. Kontakt löschen");
        System.out.println("6. Zurück");
    }

    private void processChoiceContacts(String choice) {
        switch (choice) {
            case "1" -> showContacts();
            case "2" -> addContact();
            case "3" -> updateContact();
            case "4" -> searchContacts();
            case "5" -> deleteContact();
            case "6" -> runningContacts = false;
            default -> {System.out.println("\"" + choice + "\" ist keine gültige Menüoption.");
                        System.out.println("Bitte wählen Sie eine Zahl zwischen 1 und 6.");}
        }
    }

    private void showContacts() {
        var contacts = contactService.findAllContacts();
        logger.debug("[TUI] {} Kontakte gezeigt", contacts.size());
        if (contacts.isEmpty()) {
            System.out.println("Keine Kontakte vorhanden.");
            return;
        }
        System.out.printf(
            "%-36.36s | %-35.35s | %-15.15s | %-35.35s | %-50.50s%n",
            "ID", "Name", "Telefon", "E-Mail", "Adresse"
        );
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Contact c : contacts) {
            System.out.printf(
                "%-36.36s | %-35.35s | %-15.15s | %-35.35s | %-50.50s%n",
                c.id(),
                c.name(),
                c.phoneNumber().orElse("-"),
                c.email().orElse("-"),
                c.address().orElse("-")
            );
        }
    }

    private void addContact() {
        System.out.println("=== Neuen Kontakt hinzufügen ===");

        System.out.print("Name      : ");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            logger.warn("[TUI] Kontakt-Name ist leer");
            System.out.println("Name darf nicht leer sein. Vorgang abgebrochen.");
            return;
        }

        System.out.print("Telefon   : ");
        String phone = scanner.nextLine();

        System.out.print("Adresse   : ");
        String adresse = scanner.nextLine();

        System.out.print("E-Mail    : ");
        String email = scanner.nextLine();

        contactService.addContact(name, phone, adresse, email);
        logger.info("[TUI] Kontakt geaddet: {}", name);

        System.out.println("Kontakt hinzugefügt:");
        System.out.println("Name    : " + name);
        System.out.println("Telefon : " + phone);
        System.out.println("Adresse : " + adresse);
        System.out.println("E-Mail  : " + email);
    }

    private void updateContact() {
        System.out.println("=== Kontakt aktualisieren ===");

        System.out.print("Bitte geben Sie die Kontakt-ID ein: ");
        String id = scanner.nextLine().trim();

        var existingContact = contactService.findContactById(id);
        if (existingContact.isEmpty()) {
            logger.warn("[TUI] Kontakt zum Updaten nicht gefunden: {}", id);
            System.out.println("Kein Kontakt mit der ID " + id + " gefunden. Vorgang abgebrochen.");
            return;
        }
        var existing = existingContact.get();

        System.out.println("Aktueller Name: " + existing.name());
        System.out.print("Neuer Name (leer lassen zum Beibehalten): ");
        String name = scanner.nextLine();
        if (name.isBlank()) {
            name = existing.name();
        }

        System.out.println("Aktuelle Telefonnummer: " + existing.phoneNumber().orElse("-"));
        System.out.print("Neue Telefonnummer (leer lassen zum Beibehalten): ");
        String phone = scanner.nextLine();
        if (phone.isBlank()) {
            phone = existing.phoneNumber().orElse("");
        }

        System.out.println("Aktuelle Adresse: " + existing.address().orElse("-"));
        System.out.print("Neue Adresse (leer lassen zum Beibehalten): ");
        String adresse = scanner.nextLine();
        if (adresse.isBlank()) {
            adresse = existing.address().orElse("");
        }

        System.out.println("Aktuelle E-Mail: " + existing.email().orElse("-"));
        System.out.print("Neue E-Mail (leer lassen zum Beibehalten): ");
        String email = scanner.nextLine();
        if (email.isBlank()) {
            email = existing.email().orElse("");
        }

        contactService.updateContact(id, name, phone, adresse, email);
        logger.info("[TUI] Kontakt geupdatet: {}", id);

        System.out.println("Kontakt mit ID " + id + " wurde erfolgreich aktualisiert.");
    }

    private void searchContacts() {
        System.out.println("Suchauswahl : ");
        System.out.println("1. ID");
        System.out.println("2. Name");
        System.out.println("3. Telefon");
        System.out.println("4. E-Mail");
        System.out.println("5. Adresse");
        String choice = scanner.nextLine();

        System.out.print("Suche nach: ");
        String term = scanner.nextLine();
        Optional<List<Contact>> results;

        switch (choice) {
            case "1" -> {
                results = contactService.findContactById(term).map(List::of);
                logger.debug("[TUI] Kontakt-Suche nach ID: {}", term);
            }
            case "2" -> {
                results = contactService.findContactsByName(term);
                logger.debug("[TUI] Kontakt-Suche nach Name: {}", term);
            }
            case "3" -> {
                results = contactService.findContactsByPhone(term);
                logger.debug("[TUI] Kontakt-Suche nach Telefon: {}", term);
            }
            case "4" -> {
                results = contactService.findContactsByEmail(term);
                logger.debug("[TUI] Kontakt-Suche nach Email: {}", term);
            }
            case "5" -> {
                results = contactService.findContactsByAddresse(term);
                logger.debug("[TUI] Kontakt-Suche nach Adresse: {}", term);
            }
            default -> {
                System.out.println("Ungültige Suchauswahl.");
                return;
            }
        }

        if (results.isEmpty()) {
            System.out.println("Kein Kontakt mit dem Suchbegriff " + term + " gefunden.");
            return;
        }
        System.out.printf(
            "%-36.36s | %-35.35s | %-15.15s | %-35.35s | %-50.50s%n",
            "ID", "Name", "Telefon", "E-Mail", "Adresse"
        );
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Contact c : results.get()) {
            System.out.printf(
                "%-36.36s | %-35.35s | %-15.15s | %-35.35s | %-50.50s%n",
                c.id(),
                c.name(),
                c.phoneNumber().orElse("-"),
                c.email().orElse("-"),
                c.address().orElse("-")
            );
        }
    }

    private void deleteContact() {
        String id;
        String response;

        System.out.println();
        System.out.println("=== Kontakt löschen ===");

        System.out.println("Bitte geben Sie die Kontakt-ID ein: ");
        id = String.valueOf(scanner.nextLine().trim());

        System.out.println("Möchten Sie wirklich den Kontakt von Ihrem Addressbuch löschen? Geben Sie \"Ja\" zum bestätigen ein.");
        response = scanner.nextLine().trim().toLowerCase();

        if ("ja".equals(response)) {
            try {
                contactService.deleteContact(id);
                logger.info("[TUI] Kontakt gelöscht: {}", id);
                System.out.println("Kontakt mit ID " + id + " wurde erfolgreich gelöscht.");
            } catch (IllegalArgumentException e) {
                logger.warn("[TUI] Kontakt zum Löschen nicht gefunden: {}", id);
                System.out.println("Keinen Kontakt mit ID " + id + " gefunden.");
            }
        } else {
            System.out.println("Der Löschvorgang wurde abgebrochen.");
        }
    }

    private void groupCommands() {
        runningGroups = true;

        while (runningGroups) {
            displayMenuGroups();
            System.out.print("> ");
            String choice = scanner.nextLine().trim();
            processChoiceGroups(choice);
        }
    }

    private void displayMenuGroups() {
        System.out.println("=== Gruppenverwaltung ===");
        System.out.println("1. Gruppen anzeigen");
        System.out.println("2. Gruppe hinzufügen");
        System.out.println("3. Gruppe bearbeiten");
        System.out.println("4. Gruppe suchen");
        System.out.println("5. Gruppe löschen");
        System.out.println("6. Kontakt einer Gruppe hinzufügen");
        System.out.println("7. Kontakte einer Gruppe anzeigen");
        System.out.println("8. Ist Kontakt in Gruppe?");
        System.out.println("9. Kontakt einer Gruppe entfernen");
        System.out.println("10. Zurück");
    }

    private void processChoiceGroups(String choice) {
        switch (choice) {
            case "1" -> showGroups();
            case "2" -> createGroup();
            case "3" -> updateGroup();
            case "4" -> searchGroup();
            case "5" -> deleteGroup();
            case "6" -> addContactToGroup();
            case "7" -> showContactsInGroup();
            case "8" -> isContactInGroup();
            case "9" -> removeContactFromGroup();
            case "10" -> runningGroups = false;
            default -> {System.out.println("\"" + choice + "\" ist keine gültige Menüoption.");
                        System.out.println("Bitte wählen Sie eine Zahl zwischen 1 und 10.");}
        }
    }

    private void showGroups() {
        var groups = groupService.findAllGroups();
        logger.debug("[TUI] {} Gruppen gezeigt", groups.size());
        if (groups.isEmpty()) {
            System.out.println("Keine Gruppen vorhanden");
            return;
        }
        System.out.printf("%-36.36s | %-35.35s | %-60.60s%n", "ID", "Name", "Beschreibung");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Group g : groups) {
            System.out.printf(
                "%-36.36s | %-35.35s | %-60.60s%n",
                g.id(),
                g.name(),
                g.description().orElse("-")
            );
        }
    }

    private void createGroup() {
        System.out.println();
        System.out.println("=== Neue Gruppe erstellen ===");

        System.out.print("Gruppenname   : ");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            logger.warn("[TUI] Gruppen-Name ist leer");
            System.out.println("Name darf nicht leer sein. Vorgang abgebrochen.");
            return;
        }

        System.out.print("Beschreibung  : ");
        String desc = scanner.nextLine();

        groupService.addGroup(name, desc);
        logger.info("[TUI] Gruppe geaddet: {}", name);

        System.out.println();
        System.out.println("Gruppe erfolgreich angelegt");
        System.out.println("-----------------------------");
        System.out.println("Name          : " + name);
        System.out.println("Beschreibung  : " + (desc.isBlank() ? "-" : desc));
    }

    public void searchGroup() {
        System.out.println("Suchauswahl : ");
        System.out.println("1. ID");
        System.out.println("2. Name");
        System.out.println("3. Beschreibung");

        String choice = scanner.nextLine();

        System.out.print("Suche nach: ");
        String term = scanner.nextLine();
        Optional<List<Group>> results;

        switch (choice) {
            case "1" -> {
                results = groupService.findGroupById(term).map(List::of);
                logger.debug("[TUI] Gruppen-Suche nach ID: {}", term);
            }
            case "2" -> {
                results = groupService.findGroupByName(term);
                logger.debug("[TUI] Gruppen-Suche nach Name: {}", term);
            }
            default -> {
                System.out.println("Ungültige Suchauswahl.");
                return;
            }
        };

        if (results.isEmpty()) {
            System.out.println("Keine Gruppe mit dem Suchbegriff " + term + " gefunden.");
            return;
        }
        System.out.printf("%-36.36s | %-35.35s | %-60.60s%n", "ID", "Name", "Beschreibung");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Group g : results.get()) {
            System.out.printf(
                "%-36.36s | %-35.35s | %-60.60s%n",
                g.id(),
                g.name(),
                g.description().orElse("-")
            );
        }
    }

    private void deleteGroup() {
        String id;
        String response;

        System.out.println();
        System.out.println("=== Gruppe löschen ===");

        System.out.println("Bitte geben Sie die Gruppen-ID ein: ");
        id = String.valueOf(scanner.nextLine().trim());

        System.out.println(
            "Möchten Sie die Gruppe wirklich aus Ihrem Adressbuch löschen?\n" +
            "Geben Sie \"Ja\" ein, um den Vorgang fortzusetzen. " +
            "Bei jeder anderen Eingabe wird der Vorgang abgebrochen."
        );
        response = scanner.nextLine().trim().toLowerCase();

        if ("ja".equals(response)) {
            try {
                groupService.deleteGroup(id);
                logger.info("[TUI] Gruppe gelöscht: {}", id);
                System.out.println("Gruppe mit ID " + id + " wurde erfolgreich gelöscht.");
            } catch (IllegalArgumentException e) {
                logger.warn("[TUI] Gruppe zum Löschen nicht gefunden: {}", id);
                System.out.println("Keine Gruppe mit ID " + id + " gefunden.");
            }
        }else {
            System.out.println("Der Löschvorgang wurde abgebrochen.");
        }
    }

    private void updateGroup() {
        System.out.println();
        System.out.println("=== Gruppe aktualisieren ===");

        System.out.print("Gruppen-ID eingeben: ");
        String id = scanner.nextLine().trim();

        Optional<Group> existingGroup = groupService.findGroupById(id);
        if (existingGroup.isEmpty()) {
            logger.warn("[TUI] Gruppe zum Updaten nicht gefunden: {}", id);
            System.out.println("Keine Gruppe mit der ID " + id + " gefunden.");
            return;
        }

        Group group = existingGroup.get();

        System.out.println("Aktueller Name: " + group.name());
        System.out.print("Neuer Name (leer lassen, um nicht zu ändern): ");
        String newName = scanner.nextLine().trim();
        if (newName.isEmpty()) {
            newName = group.name();
        }

        System.out.println("Aktuelle Beschreibung: " + group.description().orElse("-"));
        System.out.print("Neue Beschreibung (leer lassen, um nicht zu ändern): ");
        String newDesc = scanner.nextLine().trim();
        if (newDesc.isEmpty()) {
            newDesc = group.description().orElse("");
        }

        groupService.updateGroup(id, newName, newDesc);
        logger.info("[TUI] Gruppe geupdatet: {}", id);
        
        System.out.println("Gruppe erfolgreich aktualisiert.");
        System.out.println("Neue Daten:");
        System.out.println("Name: " + newName);
        System.out.println("Beschreibung: " + (newDesc.isBlank() ? "-" : newDesc));
    }

    private void addContactToGroup() {
        System.out.println("Geben Sie die ID der Gruppe ein, der Sie einen Kontakt hinzufügen möchten: ");
        String groupId = scanner.nextLine().trim();
        Optional<Group> existingGroup = groupService.findGroupById(groupId);
        if (existingGroup.isEmpty()) {
            logger.warn("[TUI] Gruppe nicht gefunden beim Hinzufügen: {}", groupId);
            System.out.println("Keine Gruppe mit der ID " + groupId + " gefunden.");
            return;
        }

        System.out.println("Geben Sie die ID des Kontakts ein, den Sie zur Gruppe hinzufügen möchten: ");
        String contactId = scanner.nextLine().trim();
        Optional<Contact> existingContact = contactService.findContactById(contactId);
        if (existingContact.isEmpty()) {
            logger.warn("[TUI] Kontakt nicht gefunden beim Hinzufügen: {}", contactId);
            System.out.println("Kein Kontakt mit der ID " + contactId + " gefunden.");
            return;
        }

        if (contactGroupService.isContactInGroup(contactId, groupId)) {
            logger.debug("[TUI] Kontakt ist bereits in Gruppe: {}/{}", contactId, groupId);
            System.out.println("Kontakt ist bereits in der Gruppe.");
            return;
        }

        contactGroupService.addContactToGroup(contactId, groupId);
        logger.info("[TUI] Kontakt zu Gruppe geaddet: {}/{}", contactId, groupId);
        System.out.println("Kontakt wurde zur Gruppe hinzugefügt.");
    }

    private void showContactsInGroup() {
        System.out.println("Geben Sie die ID der Gruppe ein, deren Kontakte angezeigt werden sollen: ");
        String groupId = scanner.nextLine().trim();
        Optional<Group> existingGroup = groupService.findGroupById(groupId);
        if (existingGroup.isEmpty()) {
            logger.warn("[TUI] Gruppe nicht gefunden beim Anzeigen: {}", groupId);
            System.out.println("Keine Gruppe mit der ID " + groupId + " gefunden.");
            return;
        }

        List<String> contactIds = contactGroupService.findContactIdsByGroupId(groupId);
        logger.debug("[TUI] {} Kontakte in Gruppe {} gezeigt", contactIds.size(), groupId);
        if (contactIds.isEmpty()) {
            System.out.println("Keine Kontakte in der Gruppe gefunden.");
            return;
        } else {
            List<Contact> results = new ArrayList<>();

            for (String contactId : contactIds) {
                contactService.findContactById(contactId)
                    .ifPresent(results::add);
            }

            if (results.isEmpty()) {
                System.out.println("Keine gültigen Kontakte gefunden.");
                return;
            }
            System.out.printf(
                "%-36.36s | %-35.35s | %-15.15s | %-35.35s | %-50.50s%n",
                "ID", "Name", "Telefon", "E-Mail", "Adresse"
            );
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            for (Contact c : results) {
                System.out.printf(
                    "%-36.36s | %-35.35s | %-15.15s | %-35.35s | %-50.50s%n",
                    c.id(),
                    c.name(),
                    c.phoneNumber().orElse("-"),
                    c.email().orElse("-"),
                    c.address().orElse("-")
                );
            }
        }
    }

    private void isContactInGroup() {
        System.out.println("Geben Sie die ID der Gruppe ein, welche auf den Kontakt überprüft werden soll: ");
        String groupId = scanner.nextLine().trim();

        Optional<Group> existingGroup = groupService.findGroupById(groupId);
        if (existingGroup.isEmpty()) {
            logger.debug("[TUI] Gruppe nicht gefunden beim Überprüfen: {}", groupId);
            System.out.println("Keine Gruppe mit der ID " + groupId + " gefunden.");
            return;
        }

        System.out.println("Geben Sie die ID des Kontakts ein, welcher auf die Gruppe überprüft werden soll: ");
        String contactId = scanner.nextLine().trim();

        Optional<Contact> existingContact = contactService.findContactById(contactId);
        if (existingContact.isEmpty()) {
            logger.debug("[TUI] Kontakt nicht gefunden beim Überprüfen: {}", contactId);
            System.out.println("Kein Kontakt mit der ID " + contactId + " gefunden.");
            return;
        }
        
        if (contactGroupService.isContactInGroup(contactId, groupId)) {
            logger.debug("[TUI] Kontakt ist in Gruppe: {}/{}", contactId, groupId);
            System.out.println("Der Kontakt " + existingContact.get().name() + " ist in der angegebenen Gruppe.");
        } else {
            logger.debug("[TUI] Kontakt ist nicht in Gruppe: {}/{}", contactId, groupId);
            System.out.println("Der Kontakt ist nicht in der Gruppe.");
        }
    }

    private void removeContactFromGroup() {
        System.out.println("Geben Sie die ID der Gruppe ein, aus der der Kontakt entfernt werden soll: ");
        String groupId = scanner.nextLine().trim();

        Optional<Group> existingGroup = groupService.findGroupById(groupId);
        if (existingGroup.isEmpty()) {
            logger.debug("[TUI] Gruppe nicht gefunden beim Entfernen: {}", groupId);
            System.out.println("Keine Gruppe mit der ID " + groupId + " gefunden.");
            return;
        }

        System.out.println("Geben Sie die ID des Kontakts ein, welcher aus der Gruppe entfernt werden soll: ");
        String contactId = scanner.nextLine().trim();

        Optional<Contact> existingContact = contactService.findContactById(contactId);
        if (existingContact.isEmpty()) {
            logger.debug("[TUI] Kontakt nicht gefunden beim Entfernen: {}", contactId);
            System.out.println("Kein Kontakt mit der ID " + contactId + " gefunden.");
            return;
        }

        if (contactGroupService.isContactInGroup(contactId, groupId)) {
            contactGroupService.removeContactFromGroup(contactId, groupId);
            logger.info("[TUI] Kontakt entfernt aus Gruppe: {}/{}", contactId, groupId);
            System.out.println("Kontakt mit ID " + contactId + " wurde aus der Gruppe mit ID " + groupId + " entfernt.");
        } else {
            logger.debug("[TUI] Kontakt war nicht in Gruppe: {}/{}", contactId, groupId);
            System.out.println("Der Kontakt war nicht in der Gruppe oder es gab ein Problem beim Entfernen.");
        }   
    }
}
