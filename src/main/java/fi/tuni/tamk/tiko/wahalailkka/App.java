package fi.tuni.tamk.tiko.wahalailkka;

import fi.tuni.tamk.tiko.wahalailkka.controller.PersonController;
import fi.tuni.tamk.tiko.wahalailkka.repository.CsvPersonRepository;
import fi.tuni.tamk.tiko.wahalailkka.repository.MemPersonRepository;
import fi.tuni.tamk.tiko.wahalailkka.repository.PersonRepository;
import fi.tuni.tamk.tiko.wahalailkka.ui.AppUi;
import fi.tuni.tamk.tiko.wahalailkka.ui.cli.Cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
 *     <li>Creating the controller and user interface</li>
 * </ul>
 * <p>
 * By default, the application uses a CSV-based repository. This can be
 * overridden with command-line arguments.
 */
public final class App {
    private static final Path LOG_FOLDER = Path.of("logs");
    private static final String PATH_TO_CSV = "data/person.csv";
    private static PersonRepository personRepository;
    private static final Logger LOGGER = LogManager.getLogger(App.class);

    /** Usage instructions displayed when invalid arguments are provided. */
    private static final String USAGE_MSG = """
        Usage:
            java -jar person-crud-app.jar [options]

        Options:
            --repo=mem        Use in-memory repository

        Examples:
            java -jar person-crud-app.jar
            java -jar person-crud-app.jar --repo=mem
        """;

    private App() { }

    /**
     * Starts the application.
     * <p>
     * The method processes command-line arguments, initializes the appropriate
     * repository, and launches the CLI user interface.
     *
     * @param args command-line arguments used to configure the application
     */
    static void main(final String[] args) {
        try {
            Files.createDirectories(LOG_FOLDER);
        } catch (IOException e) {
            LOGGER.error("Failed to create log directory!", e);
            System.out.println("Failed to create log directory!");
        }

        LOGGER.info("Person CRUD App started");
        handleArgs(args);
        AppUi appUi = new Cli(new PersonController(personRepository));
        appUi.run();
        LOGGER.info("Person CRUD App stopped");
    }

    private static void handleArgs(final String[] args) {
        boolean useMemRepo = false;

        for (String arg : args) {
            if (arg.equals("--repo=mem")) {
                useMemRepo = true;
            } else {
                System.err.println("Invalid command line argument given.");
                System.out.println(USAGE_MSG);
                System.exit(1);
            }
        }

        if (useMemRepo) {
            personRepository = new MemPersonRepository();
            LOGGER.info("Memory repository selected");
        } else {
            personRepository = new CsvPersonRepository(PATH_TO_CSV);
            LOGGER.info("CSV repository selected");
        }
    }
}
