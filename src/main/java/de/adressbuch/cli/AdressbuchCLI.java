package de.adressbuch.cli;

import java.util.List;
import java.util.concurrent.Callable;

import de.adressbuch.config.DatabaseConfig;
import de.adressbuch.models.Contact;
import de.adressbuch.models.Group;
import de.adressbuch.repository.SQLiteContactRepo;
import de.adressbuch.repository.SQLiteGroupRepo;
import de.adressbuch.repository.SQLiteUserRepo;
import de.adressbuch.service.ContactService;
import de.adressbuch.service.GroupService;
import de.adressbuch.service.UserService;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "adressbuch",
    description = "Adressbuch CLI",
    version = "1.0-SNAPSHOT",
    mixinStandardHelpOptions = true,
    subcommands = {
        AdressbuchCLI.ContactCommand.class,
        AdressbuchCLI.GroupCommand.class
    }
)
public class AdressbuchCLI implements Callable<Integer> {
    private static ContactService contactService;
    private static GroupService groupService;
    private static UserService userService;

    static {
        contactService = new ContactService(new SQLiteContactRepo(DatabaseConfig.DB_URL));
        groupService = new GroupService(new SQLiteGroupRepo(DatabaseConfig.DB_URL));
        userService = new UserService(new SQLiteUserRepo(DatabaseConfig.DB_URL));
    }

    @Override
    public Integer call() {
        System.out.println("Adressbuch CLI - Verwenden Sie 'contact' oder 'group' Befehle");
        return 0;
    }

    @Command(name = "contact", description = "Verwalten Sie Kontakte",
            subcommands = {
                AddContactCommand.class,
                ListContactsCommand.class,
                SearchContactCommand.class,
                UpdateContactCommand.class,
                DeleteContactCommand.class
            })
    public static class ContactCommand implements Callable<Integer> {
        @Override
        public Integer call() {
            System.out.println("Kontakt-Befehle: add, list, search, update, delete");
            return 0;
        }
    }

    @Command(name = "add", description = "Neuen Kontakt hinzufügen")
    public static class AddContactCommand implements Callable<Integer> {
        @Option(names = {"-n", "--name"}, description = "Kontaktname", required = true)
        private String name;

        @Option(names = {"-p", "--phone"}, description = "Telefonnummer")
        private String phone = "";

        @Option(names = {"-a", "--address"}, description = "Adresse")
        private String address = "";

        @Option(names = {"-e", "--email"}, description = "Email-Adresse")
        private String email = "";

        @Override
        public Integer call() {
            contactService.addContact(name, phone, address, email);
            System.out.println("Kontakt " + name + " wurde erfolgreich angelegt.");
            return 0;
        }
    }

    @Command(name = "list", description = "Alle Kontakte anzeigen")
    public static class ListContactsCommand implements Callable<Integer> {
        @Override
        public Integer call() {
            List<Contact> contacts = contactService.findAllContacts();
            if (contacts.isEmpty()) {
                System.out.println(
                    "Keine Kontakte vorhanden.\n" +
                    "Beispiel zum Hinzufügen eines Kontakts:\n" +
                    "add --name <Name> --phone <Telefonnummer> --address <Adresse> --email <E-Mail>"
                );
                return 0;
            }
            
            System.out.printf(
                    "%-5s | %-35.35s | %-15.15s | %-35.35s | %-50.50s%n",
                    "ID", "Name", "Telefon", "E-Mail", "Adresse"
            );
            System.out.println("---------------------------------------------------------------------------------------------");

            for (Contact c : contacts) {
                System.out.printf(
                        "%-5s | %-35.35s | %-15.15s | %-35.35s | %-50.50s%n",
                        c.getId().orElse(-1L),
                        c.getName(),
                        c.getPhoneNumber().orElse("-"),
                        c.getEmail().orElse("-"),
                        c.getAddress().orElse("-")
                );
            }
            return 0;
        }
    }

    @Command(name = "search", description = "Kontakte suchen")
    public static class SearchContactCommand implements Callable<Integer> {
        @Parameters(index = "0", description = "Suchbegriff")
        private String searchTerm;

        @Override
        public Integer call() {
            List<Contact> results = contactService.searchContactsByName(searchTerm);
            if (results.isEmpty()) {
                System.out.println("Kein Kontakt mit dem Suchbegriff " + searchTerm + " gefunden.");
                return 0;
            }
            System.out.printf(
                "%-5s | %-35.35s | %-15.15s | %-35.35s | %-50.50s%n",
                "ID", "Name", "Telefon", "E-Mail", "Adresse"
            );
            System.out.println("---------------------------------------------------------------------------------------------");

            for (Contact c : results) {
                System.out.printf(
                        "%-5s | %-35.35s | %-15.15s | %-35.35s | %-50.50s%n",
                        c.getId().orElse(-1L),
                        c.getName(),
                        c.getPhoneNumber().orElse("-"),
                        c.getEmail().orElse("-"),
                        c.getAddress().orElse("-")
                );
            }
            return 0;
        }
    }

    @Command(name = "update", description = "Kontakt aktualisieren")
    public static class UpdateContactCommand implements Callable<Integer> {
        @Option(names = {"-id", "--id"}, description = "Kontakt-ID", required = true)
        private Long id;

        @Option(names = {"-n", "--name"}, description = "Neuer Name")
        private String name;

        @Option(names = {"-p", "--phone"}, description = "Neue Telefonnummer")
        private String phone;

        @Option(names = {"-a", "--address"}, description = "Neue Adresse")
        private String address;

        @Option(names = {"-e", "--email"}, description = "Neue Email")
        private String email;

        @Override
        public Integer call() {
            contactService.updateContact(id, name, phone, address, email);
            System.out.println(
                "Kontakt erfolgreich aktualisiert.\n" +
                (name != null ? "Name: " + name + "\n" : "") +
                (phone != null ? "Telefon: " + phone + "\n" : "") +
                (email != null ? "E-Mail: " + email + "\n" : "") +
                (address != null ? "Adresse: " + address + "\n" : "")
            );
            return 0;
        }
    }

    @Command(name = "delete", description = "Kontakt löschen")
    public static class DeleteContactCommand implements Callable<Integer> {
        @Option(names = {"-id", "--id"}, description = "Kontakt-ID", required = true)
        private Long id;

        @Override
        public Integer call() {
            boolean deleted = contactService.deleteContact(id);
            if (deleted) {
                System.out.println("Kontakt mit ID " + id + " wurde erfolgreich gelöscht.");
            } else {
                System.out.println("Kein Kontakt mit ID " + id + " gefunden.");
            }
            return 0;
        }
    }

    @Command(name = "group", description = "Verwalten Sie Gruppen",
            subcommands = {
                AddGroupCommand.class,
                ListGroupsCommand.class,
                DeleteGroupCommand.class
            })
    public static class GroupCommand implements Callable<Integer> {
        @Override
        public Integer call() {
            System.out.println("Gruppen-Befehle: add, list, delete");
            return 0;
        }
    }

    @Command(name = "add", description = "Neue Gruppe hinzufügen")
    public static class AddGroupCommand implements Callable<Integer> {
        @Option(names = {"-n", "--name"}, description = "Gruppenname", required = true)
        private String name;

        @Option(names = {"-d", "--description"}, description = "Beschreibung")
        private String description = "";

        @Override
        public Integer call() {
            groupService.addGroup(name, description);
            System.out.println(
                "Gruppe erfolgreich angelegt:\n" +
                "Name: " + name + "\n" +
                "Beschreibung: " + (description.isBlank() ? "-" : description)
            );
            return 0;
        }
    }

    @Command(name = "list", description = "Alle Gruppen anzeigen")
    public static class ListGroupsCommand implements Callable<Integer> {
        @Override
        public Integer call() {
            List<Group> groups = groupService.findAllGroups();
            if (groups.isEmpty()) {
                System.out.println(
                    "Keine Gruppen vorhanden.\n" +
                    "Du kannst eine neue Gruppe hinzufügen mit:\n" +
                    "add --name <Gruppenname> --description <Beschreibung>"
                );
                return 0;
            }

            System.out.printf("%-5s | %-35.35s | %-60.60s%n", "ID", "Name", "Beschreibung");
            System.out.println("---------------------------------------------------------------------");

            for (Group g : groups) {
                System.out.printf(
                    "%-5s | %-35.35s | %-60.60s%n",
                    g.getId().orElse(-1L),
                    g.getName(),
                    g.getDescription().orElse("-")
                );
            }
            return 0;
        }
    }

    @Command(name = "delete", description = "Gruppe löschen")
    public static class DeleteGroupCommand implements Callable<Integer> {
        @Option(names = {"-id", "--id"}, description = "Gruppen-ID", required = true)
        private Long id;

        @Override
        public Integer call() {
            boolean deleted = groupService.deleteGroup(id);
            if (deleted) {
                System.out.println("Gruppe mit ID " + id + " wurde erfolgreich gelöscht.");
            } else {
                System.out.println("Keine Gruppe mit ID " + id + " gefunden.");
            }
            return 0;
        }
    }
}
