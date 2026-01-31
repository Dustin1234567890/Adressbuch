package de.adressbuch;

import de.adressbuch.cli.AdressbuchCLI;
import de.adressbuch.cli.InteractiveTUI;
import picocli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            CommandLine cmd = new CommandLine(cli);
            int exitCode = cmd.execute(args);
            System.exit(exitCode);
        }
    }
}