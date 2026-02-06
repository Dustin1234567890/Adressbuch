package de.adressbuch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.adressbuch.cli.AdressbuchCLI;
import de.adressbuch.cli.CliFactory;
import de.adressbuch.cli.InteractiveTUI;
import de.adressbuch.injection.ServiceFactory;
import de.adressbuch.service.ContactGroupService;
import de.adressbuch.service.ContactService;
import de.adressbuch.service.GroupService;
import picocli.CommandLine;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Adressbuch starting");
        
        if (args.length == 0) {
            // Interactive mode
            logger.info("Starting interactive mode");
            InteractiveTUI tui = new InteractiveTUI();
            tui.start();
        } else {
            // CLI mode with picocli
            logger.info("Starting CLI mode");
            ServiceFactory factory = ServiceFactory.getInstance();
            AdressbuchCLI cli = new AdressbuchCLI();
            ContactService contactService = factory.getContactService();
            GroupService groupService = factory.getGroupService();
            ContactGroupService contactGroupService = factory.getContactGroupService();

            CommandLine cmd;
            cmd = new CommandLine(
                    cli,
                    new CliFactory(
                            contactService,
                            groupService,
                            contactGroupService
                    )
            );
            int exitCode = cmd.execute(args);
            System.exit(exitCode);
        }
    }
}