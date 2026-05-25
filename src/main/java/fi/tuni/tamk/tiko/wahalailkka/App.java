package fi.tuni.tamk.tiko.wahalailkka;

import fi.tuni.tamk.tiko.wahalailkka.controller.PersonController;
import fi.tuni.tamk.tiko.wahalailkka.repository.CsvPersonRepository;
import fi.tuni.tamk.tiko.wahalailkka.repository.CsvRepositoryException;
import fi.tuni.tamk.tiko.wahalailkka.repository.MemPersonRepository;
import fi.tuni.tamk.tiko.wahalailkka.repository.PersonRepository;
import fi.tuni.tamk.tiko.wahalailkka.ui.AppUi;
import fi.tuni.tamk.tiko.wahalailkka.ui.cli.Cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import fi.tuni.tamk.tiko.wahalailkka.ui.gui.SwingGui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point of the Person CRUD application.
 * <p>
 * This class is responsible for initializing the application by:
 * <ul>
 *     <li>Initializing application logging</li>
 *     <li>Parsing command-line arguments</li>
 *     <li>Configuring the repository implementation</li>
 *     <li>Selecting the user interface implementation</li>
 *     <li>Creating the controller and launching the application UI</li>
 * </ul>
 * <p>
 * By default, the application uses a CSV-based repository and the Swing GUI.
 * Both the repository and UI implementation can be changed with command-line
 * arguments.
 */
public final class App {
    private static final Path LOG_FOLDER = Path.of("logs");
    private static final String PATH_TO_CSV = "data/person.csv";
    private static AppUi appUi;
    private static final Logger LOGGER = LogManager.getLogger(App.class);

    /** Usage instructions displayed when invalid arguments are provided. */
    private static final String USAGE_MSG = """
        Usage:
            person-crud-app [options]
    
        Options:
            --repo=mem        Use in-memory repository
            --ui=cli          Use command-line interface
    
        Examples:
            ./bin/person-crud-app
            ./bin/person-crud-app --repo=mem
            ./bin/person-crud-app --ui=cli
            ./bin/person-crud-app --repo=mem --ui=cli
        """;

    private App() { }

    /**
     * Starts the application.
     * <p>
     * The method processes command-line arguments, initializes the selected
     * repository and user interface implementation, and launches the
     * application.
     *
     * @param args command-line arguments used to configure the application
     */
    static void main(final String[] args) {
        try {
            Files.createDirectories(LOG_FOLDER);
            LOGGER.info("Person CRUD App started");
            handleArgs(args);
            appUi.run();
            LOGGER.info("Person CRUD App stopped");
        } catch (IOException e) {
            LOGGER.error("Failed to initialize application files.", e);
            System.err.println("Failed to initialize application files.");
        } catch (CsvRepositoryException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Unexpected error", e);
            System.err.println("Unexpected error.");
        }
    }

    private static void handleArgs(final String[] args) {
        boolean useMemRepo = false;
        boolean useCliUi = false;

        for (String arg : args) {
            if (arg.equals("--repo=mem")) {
                useMemRepo = true;
            } else if (arg.equals("--ui=cli")) {
                useCliUi = true;
            } else {
                System.err.println("Invalid command line argument given.");
                System.out.println(USAGE_MSG);
                System.exit(1);
            }
        }

        PersonRepository personRepository;

        if (useMemRepo) {
            personRepository = new MemPersonRepository();
            LOGGER.info("Memory repository selected");
        } else {
            personRepository = new CsvPersonRepository(PATH_TO_CSV);
            LOGGER.info("CSV repository selected");
        }

        if (useCliUi) {
            appUi = new Cli(new PersonController(personRepository));
            LOGGER.info("CLI selected");
        } else {
            appUi = new SwingGui(new PersonController(personRepository));
            LOGGER.info("GUI selected");
        }
    }
}
