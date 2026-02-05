package de.adressbuch.cli;
import de.adressbuch.service.ContactGroupService;
import de.adressbuch.service.ContactService;
import de.adressbuch.service.GroupService;
import picocli.CommandLine.IFactory;

public class CliFactory implements IFactory {

    private final ContactService contactService;
    private final GroupService groupService;
    private final ContactGroupService contactGroupService;

    public CliFactory(
            ContactService contactService,
            GroupService groupService,
            ContactGroupService contactGroupService
    ) {
        this.contactService = contactService;
        this.groupService = groupService;
        this.contactGroupService = contactGroupService;
    }

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        if (cls == AdressbuchCLI.AddContactCommand.class) {
            return cls.cast(new AdressbuchCLI.AddContactCommand(contactService));
        }
        return cls.getDeclaredConstructor().newInstance();
    }
}