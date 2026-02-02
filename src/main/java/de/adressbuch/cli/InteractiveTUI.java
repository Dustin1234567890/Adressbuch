package de.adressbuch.cli;

import java.util.Scanner;

import de.adressbuch.config.DatabaseConfig;
import de.adressbuch.models.Contact;
import de.adressbuch.models.Group;
import de.adressbuch.repository.SQLiteContactRepo;
import de.adressbuch.repository.SQLiteGroupRepo;
import de.adressbuch.service.ContactService;
import de.adressbuch.service.GroupService;

public class InteractiveTUI {
    private final ContactService contactService;
    private final GroupService groupService;
    private boolean running = true;
    private boolean runningContacts = true;
    private boolean runningGroups = true;
    private final Scanner scanner = new Scanner(System.in);

    public InteractiveTUI() {
        this.contactService = new ContactService(new SQLiteContactRepo(DatabaseConfig.DB_URL));
        this.groupService = new GroupService(new SQLiteGroupRepo(DatabaseConfig.DB_URL));
    }

    public void start() {
        System.out.println("Willkommen im Adressbuch!\n" +
            "Bitte wählen Sie eine Option aus dem Hauptmenü, um fortzufahren.");

        while (running) {
            displayMenu();
            System.out.print("> ");
            String choice = scanner.nextLine().trim();
            processChoice(choice);
        }
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
        System.out.println("3. Kontakt suchen");
        System.out.println("4. Kontakt löschen");
        System.out.println("5. Zurück");
    }

    private void processChoiceContacts(String choice) {
        switch (choice) {
            case "1" -> showContacts();
            case "2" -> addContact();
            case "3" -> searchContacts();
            case "4" -> deleteContact();
            case "5" -> runningContacts = false;
            default -> {System.out.println("\"" + choice + "\" ist keine gültige Menüoption.");
                        System.out.println("Bitte wählen Sie eine Zahl zwischen 1 und 5.");}
        }
    }

    private void showContacts() {
        var contacts = contactService.findAllContacts();
        if (contacts.isEmpty()) {
            System.out.println("Keine Kontakte vorhanden.");
            return;
        }
        System.out.printf(
            "%-5s | %-20s | %-15s | %-25s | %-30s%n",
            "ID", "Name", "Telefon", "E-Mail", "Adresse"
        );
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Contact c : contacts) {
            System.out.printf(
                //TODO check formatting with UUIDs
                    "%-5d | %-20s | %-15s | %-25s | %-30s%n",
                    c.id(),
                    c.name(),
                    c.phoneNumber().orElse("-"),
                    c.email().orElse("-"),
                    c.address().orElse("-")
            );
        }
    }

    private void addContact() {
        System.out.println();
        System.out.println("=== Neuen Kontakt hinzufügen ===");

        System.out.print("Name      : ");
        String name = scanner.nextLine();

        System.out.print("Telefon   : ");
        String phone = scanner.nextLine();

        System.out.print("Adresse   : ");
        String adresse = scanner.nextLine();

        System.out.print("E-Mail    : ");
        String email = scanner.nextLine();

        contactService.addContact(name, phone, adresse, email);

        System.out.println("Kontakt hinzugefügt:");
        System.out.println("Name    : " + name);
        System.out.println("Telefon : " + phone);
        System.out.println("Adresse : " + adresse);
        System.out.println("E-Mail  : " + email);
    }

    private void searchContacts() {
        System.out.print("Suche    : ");
        String term = scanner.nextLine();
        var results = contactService.searchContactsByName(term);
        if (results.isEmpty()) {
            System.out.println("Kein Kontakt mit dem Suchbegriff " + term + " gefunden.");
            return;
        }
        System.out.printf(
            "%-5s | %-20s | %-15s | %-25s | %-30s%n",
            "ID", "Name", "Telefon", "E-Mail", "Adresse"
        );
        System.out.println("---------------------------------------------------------------------------------------------");

        for (Contact c : results) {
            System.out.printf(
                    "%-5d | %-20s | %-15s | %-25s | %-30s%n",
                    //TODO check formatting with UUIDs
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
            boolean deleted = contactService.deleteContact(id);
            if (deleted) {
                System.out.println("Kontakt mit ID " + id + " wurde erfolgreich gelöscht.");
            } else {
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
        System.out.println("3. Gruppe löschen");
        System.out.println("4. Zurück");
    }

    private void processChoiceGroups(String choice) {
        switch (choice) {
            case "1" -> showGroups();
            case "2" -> createGroup();
            case "3" -> deleteGroup();
            case "4" -> runningGroups = false;
            default -> {System.out.println("\"" + choice + "\" ist keine gültige Menüoption.");
                        System.out.println("Bitte wählen Sie eine Zahl zwischen 1 und 4.");}
        }
    }

    private void showGroups() {
        var groups = groupService.findAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Keine Gruppen vorhanden");
            return;
        }
        System.out.printf("%-5s | %-20s | %-40s%n", "ID", "Name", "Beschreibung");
        System.out.println("---------------------------------------------------------------------");

        for (Group g : groups) {
            System.out.printf(
                //TODO: formatting string uuid
                "%-5d | %-20s | %-40s%n",
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

        System.out.print("Beschreibung  : ");
        String desc = scanner.nextLine();

        groupService.addGroup(name, desc);

        System.out.println();
        System.out.println("Gruppe erfolgreich angelegt");
        System.out.println("-----------------------------");
        System.out.println("Name          : " + name);
        System.out.println("Beschreibung  : " + (desc.isBlank() ? "-" : desc));
    }

    private void deleteGroup() {
        String id;
        String response;

        System.out.println();
        System.out.println("=== Gruppe löschen ===");

        System.out.println("Bitte geben Sie die Gruppen-ID ein: ");
        id = String.valueOf(scanner.nextLine().trim());

        System.out.println("Möchten Sie wirklich die Gruppe von Ihrem Addressbuch löschen? Geben Sie \"Ja\" zum bestätigen ein.");
        response = scanner.nextLine().trim().toLowerCase();

        if ("ja".equals(response)) {
            boolean deleted = groupService.deleteGroup(id);
            if (deleted) {
                System.out.println("Gruppe mit ID " + id + " wurde erfolgreich gelöscht.");
            } else {
                System.out.println("Keine Gruppe mit ID " + id + " gefunden.");
            } 
        }else {
            System.out.println("Der Löschvorgang wurde abgebrochen.");
        }
    }
}
