package de.adressbuch.cli;

import de.adressbuch.config.DatabaseConfig;
import de.adressbuch.repository.*;
import de.adressbuch.repository.interfaces.*;
import de.adressbuch.service.*;

import java.util.Scanner;

public class InteractiveTUI {
    private final ContactService contactService;
    private final GroupService groupService;
    private boolean running = true;

    public InteractiveTUI() {
        this.contactService = new ContactService(new SQLiteContactRepo(DatabaseConfig.DB_URL));
        this.groupService = new GroupService(new SQLiteGroupRepo(DatabaseConfig.DB_URL));
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Adressbuch");

        while (running) {
            displayMenu();
            System.out.print("> ");
            String choice = scanner.nextLine().trim();
            processChoice(choice);
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n1. Kontakte anzeigen");
        System.out.println("2. Kontakt hinzufuegen");
        System.out.println("3. Kontakt suchen");
        System.out.println("4. Gruppen anzeigen");
        System.out.println("5. Gruppe erstellen");
        System.out.println("6. exit");
    }

    private void processChoice(String choice) {
        switch (choice) {
            case "1" -> showContacts();
            case "2" -> addContact();
            case "3" -> searchContacts();
            case "4" -> showGroups();
            case "5" -> createGroup();
            case "6" -> running = false;
            default -> System.out.println("unknown");
        }
    }

    private void showContacts() {
        var contacts = contactService.findAllContacts();
        if (contacts.isEmpty()) {
            System.out.println("Keine Kontakte vorhanden");
            return;
        }
        for (var c : contacts) {
            System.out.println(c.getId().orElse(-1L) + " " + c.getName() + " " + c.getPhoneNumber().orElse("-") + " " + c.getEmail().orElse("-"));
        }
    }

    private void addContact() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Telefon: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        contactService.addContact(name, phone, "", email);
        System.out.println("Kontakt hinzugefügt");
    }

    private void searchContacts() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Suche: ");
        String term = scanner.nextLine();
        var results = contactService.findAllContacts().stream()
                .filter(c -> c.getName().toLowerCase().contains(term.toLowerCase()))
                .toList();
        if (results.isEmpty()) {
            System.out.println("Keine");
            return;
        }
        for (var c : results) {
            System.out.println(c.getId().orElse(-1L) + " " + c.getName());
        }
    }

    private void showGroups() {
        var groups = groupService.findAllGroups();
        if (groups.isEmpty()) {
            System.out.println("Keine Gruppen vorhanden");
            return;
        }
        for (var g : groups) {
            System.out.println(g.getId().orElse(-1L) + " " + g.getName());
        }
    }

    private void createGroup() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Beschreibung: ");
        String desc = scanner.nextLine();

        groupService.addGroup(name, desc);
        System.out.println("Gruppe erstellt");
    }
}
