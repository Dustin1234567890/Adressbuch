package de.adressbuch.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import de.adressbuch.config.DatabaseConfig;
import de.adressbuch.models.Contact;
import de.adressbuch.models.Group;
import de.adressbuch.repository.SQLiteContactGroupRepo;
import de.adressbuch.repository.SQLiteContactRepo;
import de.adressbuch.repository.SQLiteGroupRepo;
import de.adressbuch.service.ContactGroupService;
import de.adressbuch.service.ContactService;
import de.adressbuch.service.GroupService;
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
    private static ContactGroupService contactGroupService;

    static {
        contactService = new ContactService(new SQLiteContactRepo(DatabaseConfig.DB_URL));
        groupService = new GroupService(new SQLiteGroupRepo(DatabaseConfig.DB_URL));
        contactGroupService = new ContactGroupService(new SQLiteContactGroupRepo(DatabaseConfig.DB_URL), groupService, contactService);
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
        private final ContactService contactService;

        public AddContactCommand(ContactService contactService) {
            this.contactService = contactService;
        }

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
            return 0;
        }
    }

    @Command(name = "search", description = "Kontakte suchen")
    public static class SearchContactCommand implements Callable<Integer> {
        public enum ContactSearchField {
            name,
            phone,
            email,
            address,
            id
        }

        @Parameters(index = "0", description = "Suchfeld (name, phone, email, address, id)")
        private ContactSearchField field;

        @Parameters(index = "1", description = "Suchbegriff")
        private String search;

        @Override
        public Integer call() {
            Optional<List<Contact>> results = switch (field) {
                case name -> contactService.findContactsByName(search);
                case phone -> contactService.findContactsByPhone(search);
                case email -> contactService.findContactsByEmail(search);
                case address -> contactService.findContactsByAddresse(search);
                case id -> contactService.findContactById(search).map(List::of);
            };
            if (results.isEmpty()) {
                System.out.println("Kein Kontakt mit dem Suchbegriff " + search + " gefunden.");
                return 0;
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
            return 0;
        }
    }

    @Command(name = "update", description = "Kontakt aktualisieren")
    public static class UpdateContactCommand implements Callable<Integer> {
        @Option(names = {"-id", "--id"}, description = "Kontakt-ID", required = true)
        private String id;

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
            Optional<Contact> existingContact = contactService.findContactById(id);

            if (existingContact.isEmpty()) {
                System.out.println("Kein Kontakt mit der ID " + id + " gefunden. Aktualisierung abgebrochen.");
                return 0;
            }

            Contact existing = existingContact.get();

            contactService.updateContact(
                id,
                name != null ? name : existing.name(),
                phone != null ? phone : existing.phoneNumber().orElse(null),
                address != null ? address : existing.address().orElse(null),
                email != null ? email : existing.email().orElse(null)
            );

            System.out.println("Kontakt erfolgreich aktualisiert:");
            if (name != null) {
            System.out.println("Name: " + existing.name() + " → " + name);
            }
            if (phone != null) {
                System.out.println("Telefon: " + existing.phoneNumber().orElse("-") + " → " + phone);
            }
            if (email != null) {
                System.out.println("E-Mail: " + existing.email().orElse("-") + " → " + email);
            }
            if (address != null) {
                System.out.println("Adresse: " + existing.address().orElse("-") + " → " + address);
            }
            return 0;
        }
    }

    @Command(name = "delete", description = "Kontakt löschen")
    public static class DeleteContactCommand implements Callable<Integer> {
        @Option(names = {"-id", "--id"}, description = "Kontakt-ID", required = true)
        private String id;
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
                DeleteGroupCommand.class,
                UpdateGroupCommand.class,
                SearchGroupCommand.class,
                AddContactToGroupCommand.class,
                IsContactInGroupCommand.class,
                ListContactsInGroupCommand.class,
                RemoveContactFromGroupCommand.class
            })
    public static class GroupCommand implements Callable<Integer> {
        @Override
        public Integer call() {
            System.out.println("Gruppen-Befehle: add, list, delete, update, search");
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
            return 0;
        }
    }

    @Command(name = "delete", description = "Gruppe löschen")
    public static class DeleteGroupCommand implements Callable<Integer> {
        @Option(names = {"-id", "--id"}, description = "Gruppen-ID", required = true)
        private String id;
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

    @Command(name = "update", description = "Gruppe aktualisieren")
    public static class UpdateGroupCommand implements Callable<Integer> {
        @Option(names = {"-id", "--id"}, description = "Gruppen-ID", required = true)
        private String id;

        @Option(names = {"-n", "--name"}, description = "Neuer Gruppenname")
        private String name;

        @Option(names = {"-d", "--description"}, description = "Neue Beschreibung")
        private String description;
        
        @Override
        public Integer call() {
            Optional<Group> existingContact = groupService.findGroupById(id);

            if (existingContact.isEmpty()) {
                System.out.println("Keine Gruppe mit der ID " + id + " gefunden. Aktualisierung abgebrochen.");
                return 0;
            }

            Group existing = existingContact.get();

            groupService.updateGroup(
                id,
                name != null ? name : existing.name(),
                description != null ? description : existing.description().orElse(null)
            );

            System.out.println("Gruppe erfolgreich aktualisiert:");
            if (name != null) {
            System.out.println("Name: " + existing.name() + " → " + name);
            }
            if (description != null) {
                System.out.println("Beschreibung: " + existing.description().orElse("-") + " → " + description);
            }
            return 0;
        }
    }

    @Command(name = "search", description = "Gruppe suchen")
    public static class SearchGroupCommand implements Callable<Integer> {
        public enum GroupSearchField {
            name,
            id
        }

        @Parameters(index = "0", description = "Suchfeld (name, description, id)")
        private GroupSearchField field;

        @Parameters(index = "1", description = "Suchbegriff")
        private String search;
        
        @Override
        public Integer call() {
            Optional<List<Group>> results = switch (field) {
                case name -> groupService.findGroupByName(search);
                case id -> groupService.findGroupById(search).map(List::of);
            };
            if (results.isEmpty()) {
                System.out.println("Keine Gruppe mit dem Suchbegriff " + search + " gefunden.");
                return 0;
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
            return 0;
        }
    }

    @Command(name = "add-contact-to-group", description = "Kontakt zu Gruppe hinzufügen")
    public static class AddContactToGroupCommand implements Callable<Integer> {
        @Option(names = {"-idGroup", "--idGroup"}, description = "Gruppen-ID", required = true)
        private String groupId;

        @Option(names = {"-idContact", "--idContact"}, description = "Kontakt-ID", required = true)
        private String contactId;
        
        @Override
        public Integer call() {
            Optional<Group> existingGroup = groupService.findGroupById(groupId);
            Optional<Contact> existingContact = contactService.findContactById(contactId);

            if (existingGroup.isEmpty()) {
                System.out.println("Keine Gruppe mit der ID " + groupId + " gefunden.");
                return 0;
            }

            if (existingContact.isEmpty()) {
                System.out.println("Kein Kontakt mit der ID " + contactId + " gefunden.");
                return 0;
            }
            
            if (contactGroupService.isContactInGroup(contactId, groupId)) {
                System.out.println("Kontakt ist bereits in der Gruppe.");
                return 0;
            }
            
            contactGroupService.addContactToGroup(contactId, groupId);
            System.out.println("Kontakt erfolgreich zur Gruppe hinzugefügt.");
            return 0;
        }
    }

    @Command(name = "is-contact-in-group", description = "Prüft, ob ein Kontakt in einer Gruppe ist")
    public static class IsContactInGroupCommand implements Callable<Integer> {
        @Option(names = {"-idGroup", "--idGroup"}, description = "Gruppen-ID", required = true)
        private String groupId;

        @Option(names = {"-idContact", "--idContact"}, description = "Kontakt-ID", required = true)
        private String contactId;
        
        @Override
        public Integer call() {
            Optional<Group> existingGroup = groupService.findGroupById(groupId);
            Optional<Contact> existingContact = contactService.findContactById(contactId);

            if (existingGroup.isEmpty()) {
                System.out.println("Keine Gruppe mit der ID " + groupId + " gefunden.");
                return 0;
            }
            if (existingContact.isEmpty()) {
                System.out.println("Kein Kontakt mit der ID " + contactId + " gefunden.");
                return 0;
            }
            
            if (contactGroupService.isContactInGroup(contactId, groupId)) {
                System.out.println("Der Kontakt " + existingContact.get().name() + " ist in der angegebenen Gruppe.");
            } else {
                System.out.println("Der Kontakt ist nicht in der Gruppe.\n" +
                "Um einen Kontakt zu einer Gruppe hinzuzufügen, verwenden Sie den Befehl 'add-contact-to-group'.");
            }
            return 0;
        }
    }

    @Command(name = "show-contacts-in-group", description = "Findet alle Kontakte in einer Gruppe")
    public static class ListContactsInGroupCommand implements Callable<Integer> {
        @Option(names = {"-idGroup", "--idGroup"}, description = "Gruppen-ID", required = true)
        private String groupId;
        
        @Override
        public Integer call() {
            List<String> contactIds = contactGroupService.findContactIdsByGroupId(groupId);
            if (contactIds.isEmpty()) {
                System.out.println("Keine Kontakte in der Gruppe gefunden.");
                return 0;
            } else {
                Optional<Group> existingGroup = groupService.findGroupById(groupId);

                if (existingGroup.isEmpty()) {
                    System.out.println("Keine Gruppe mit der ID " + groupId + " gefunden.");
                    return 0;
                }

                List<Contact> results = new ArrayList<>();

                for (String contactId : contactIds) {
                    contactService.findContactById(contactId)
                        .ifPresent(results::add);
                }

                if (results.isEmpty()) {
                    System.out.println("Keine gültigen Kontakte gefunden.");
                    return 0;
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
            return 0;
        }
    }

    @Command(name = "remove-contact-from-group", description = "Kontakt von einer Gruppe entfernen")
    public static class RemoveContactFromGroupCommand implements Callable<Integer> {
        @Option(names = {"-idGroup", "--idGroup"}, description = "Gruppen-ID", required = true)
        private String groupId;

        @Option(names = {"-idContact", "--idContact"}, description = "Kontakt-ID", required = true)
        private String contactId;
        
        @Override
        public Integer call() {
            Optional<Group> existingGroup = groupService.findGroupById(groupId);
            Optional<Contact> existingContact = contactService.findContactById(contactId);

            if (existingGroup.isEmpty()) {
                System.out.println("Keine Gruppe mit der ID " + groupId + " gefunden.");
                return 0;
            }
            if (existingContact.isEmpty()) {
                System.out.println("Kein Kontakt mit der ID " + contactId + " gefunden.");
                return 0;
            }
            contactGroupService.removeContactFromGroup(contactId, groupId);
            System.out.println("Kontakt mit ID " + contactId + " wurde aus der Gruppe mit ID " + groupId + " entfernt.");
            return 0;
        }
    }
}
