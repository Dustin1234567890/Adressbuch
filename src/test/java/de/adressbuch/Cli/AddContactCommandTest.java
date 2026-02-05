package de.adressbuch.cli;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.adressbuch.cli.AdressbuchCLI.AddContactCommand;
import de.adressbuch.service.ContactService;

class AddContactCommandTest {

    @Test
    void addContact_callsServiceWithCorrectArguments() throws Exception {
        ContactService contactService = mock(ContactService.class);

        AddContactCommand command = new AddContactCommand(contactService);

        setField(command, "name", "Martin");
        setField(command, "phone", "12345");
        setField(command, "address", "Berlin");
        setField(command, "email", "martin@test.de");

        int exitCode = command.call();

        verify(contactService, times(1))
            .addContact("Martin", "12345", "Berlin", "martin@test.de");

        assert exitCode == 0;
    }

    private static void setField(Object target, String fieldName, Object value)
            throws Exception {

        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
