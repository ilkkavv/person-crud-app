package fi.tuni.tamk.tiko.wahalailkka;

import fi.tuni.tamk.tiko.wahalailkka.controller.PersonController;
import fi.tuni.tamk.tiko.wahalailkka.repository.CsvPersonRepository;
import fi.tuni.tamk.tiko.wahalailkka.repository.MemPersonRepository;
import fi.tuni.tamk.tiko.wahalailkka.repository.PersonRepository;
import fi.tuni.tamk.tiko.wahalailkka.ui.Cli;

/**
 * Entry point of the Person CRUD application.
 * <p>
 * This class is responsible for initializing the application by:
 * <ul>
 *     <li>Parsing command-line arguments</li>
 *     <li>Configuring the repository implementation</li>
 *     <li>Creating the controller and user interface</li>
 * </ul>
 * <p>
 * By default, the application uses a CSV-based repository. This can be
 * overridden with command-line arguments.
 */
public final class App {
    private static final String PATH_TO_CSV = "data/person.csv";
    private static PersonRepository personRepository;

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
    public static void main(final String[] args) {
        handleArgs(args);
        Cli cli = new Cli(new PersonController(personRepository));
        cli.run();
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
        } else {
            personRepository = new CsvPersonRepository(PATH_TO_CSV);
        }
    }
}
