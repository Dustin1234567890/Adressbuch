package de.adressbuch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.adressbuch.cli.AdressbuchCLI;
import de.adressbuch.cli.CliFactory;
import de.adressbuch.cli.InteractiveTUI;
import de.adressbuch.config.DatabaseConfig;
import de.adressbuch.repository.SQLiteContactGroupRepo;
import de.adressbuch.repository.SQLiteContactRepo;
import de.adressbuch.repository.SQLiteGroupRepo;
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
            AdressbuchCLI cli = new AdressbuchCLI();
            ContactService contactService = new ContactService(new SQLiteContactRepo(DatabaseConfig.DB_URL));

            GroupService groupService = new GroupService(new SQLiteGroupRepo(DatabaseConfig.DB_URL));

            ContactGroupService contactGroupService = new ContactGroupService(new SQLiteContactGroupRepo(DatabaseConfig.DB_URL), groupService, contactService);

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