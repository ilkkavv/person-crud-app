package fi.tuni.tamk.tiko.wahalailkka.ui;

import fi.tuni.tamk.tiko.wahalailkka.controller.PersonController;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;

import java.util.Optional;
import java.util.Scanner;

/**
 * Command-line user interface for the Person CRUD App.
 * <p>
 * This class is responsible for interacting with the user through the terminal.
 * It shows the menu, reads user input, calls the controller, and prints
 * results.
 */
public class Cli {
    /** Flag indicating whether the CLI main loop is running. */
    private boolean isRunning = false;
    /** Scanner used for reading user input from standard input. */
    private final Scanner scanner = new Scanner(System.in);
    /** Controller used to handle person-related application logic. */
    private final PersonController controller;

    private static final String CMD_C = "c";
    private static final String CMD_CREATE = "create";
    private static final String CMD_L = "l";
    private static final String CMD_LIST = "list";
    private static final String CMD_F = "f";
    private static final String CMD_FIND = "find";
    private static final String CMD_U = "u";
    private static final String CMD_UPDATE = "update";
    private static final String CMD_D = "d";
    private static final String CMD_DELETE = "delete";
    private static final String CMD_H = "h";
    private static final String CMD_HELP = "help";
    private static final String CMD_E = "e";
    private static final String CMD_EXIT = "exit";

    private static final String HELP_MSG = """
        NAME
            person-crud-app

        DESCRIPTION
            person-crud-app is an application for managing a collection of
            persons. It allows the user to create, view, update, and delete
            person records.

            Each person consists of an ID, first name, last name, and age. IDs
            are assigned automatically by the application.

            The application supports basic CRUD operations through an
            interactive menu.""";

    /**
     * Constructs a new CLI with the given controller.
     *
     * @param controller the controller used by this UI
     */
    public Cli(final PersonController controller) {
        this.controller = controller;
    }

    /**
     * Starts the command-line interface.
     * <p>
     * This method runs the main application loop until the user chooses to
     * exit. It repeatedly shows the menu, reads a command, and handles it.
     */
    public void run() {
        isRunning = true;

        System.out.println("* --- Person CRUD App --- *");

        while (isRunning) {
            try {
                showMenu();
                handleCommand();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void showMenu() {
        System.out.println();
        System.out.println("(C)reate new person");
        System.out.println("(L)ist all persons");
        System.out.println("(F)ind person by ID");
        System.out.println("(U)pdate person data by ID");
        System.out.println("(D)elete person by ID");
        System.out.println("(H)elp");
        System.out.println("(E)xit");
        System.out.println();
        System.out.print("Enter command: ");
    }

    private void handleCommand() {
        String input = scanner.nextLine().toLowerCase().trim();

        switch (input) {
            case CMD_C, CMD_CREATE:
                create();
                break;
            case CMD_L, CMD_LIST:
                list();
                break;
            case CMD_F, CMD_FIND:
                find();
                break;
            case CMD_U, CMD_UPDATE:
                update();
                break;
            case CMD_D, CMD_DELETE:
                delete();
                break;
            case CMD_H, CMD_HELP:
                help();
                break;
            case CMD_E, CMD_EXIT:
                exit();
                break;
            default:
                System.out.println();
                System.out.println("'" + input + "' is not a valid command.");
                waitForEnter();
        }
    }

    private void create() {
        System.out.println();
        System.out.print("Enter person first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter person last name: ");
        String lastName = scanner.nextLine();
        int age = askForAge();

        Person newPerson = controller.createPerson(firstName, lastName, age);

        System.out.println();
        System.out.println("New person created!");
        printPersonData(newPerson);
        waitForEnter();
    }

    private void list() {
        MyList<Person> personList = controller.findAllPersons();

        System.out.println();
        if (personList.size() == 0) {
            System.out.println("Person list is empty.");
        } else {
            for (int i = 0; i < personList.size(); i++) {
                Person person = personList.get(i);
                printPersonData(person);
            }
        }
        waitForEnter();
    }

    private void find() {
        int id = askForId();

        Optional<Person> optionalPerson = controller.findPersonById(id);

        System.out.println();
        optionalPerson.ifPresentOrElse(
                this::printPersonData,
                () -> System.out.println("Person NOT found with ID: " + id));

        waitForEnter();
    }

    private void update() {
        int id = askForId();

        System.out.println();
        System.out.println("Enter new values:");
        System.out.println();

        Optional<Person> optionalPerson = controller.findPersonById(id);

        if (optionalPerson.isPresent()) {
            System.out.print("Enter person first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter person last name: ");
            String lastName = scanner.nextLine();
            int age = askForAge();

            Optional<Person> optionalUpdated = controller.updatePersonById(id,
                    firstName, lastName, age);

            if (optionalUpdated.isPresent()) {
                System.out.println();
                System.out.println("Person data updated!");
                printPersonData(optionalUpdated.get());

                waitForEnter();
            }
        } else {
            System.out.println();
            System.out.println("Person NOT found with ID: " + id);
            waitForEnter();
        }
    }

    private void delete() {
        int id = askForId();

        Optional<Person> optionalPerson = controller.findPersonById(id);

        if (optionalPerson.isPresent()) {
            Optional<Person> optionalDeleted = controller.deletePersonById(id);

            if (optionalDeleted.isPresent()) {
                System.out.println("Person data deleted!");
                System.out.print("DELETED: ");
                printPersonData(optionalDeleted.get());

                waitForEnter();
            }
        } else {
            System.out.println("Person NOT found with ID: " + id);
            waitForEnter();
        }
    }

    private void help() {
        System.out.println();
        System.out.println(HELP_MSG);
        waitForEnter();
    }

    private void exit() {
        System.out.println();
        System.out.println("Exiting Person CLI App...");
        isRunning = false;
    }

    private void printPersonData(final Person person) {
        System.out.printf(
                "ID: %d | %s %s | Age: %d%n",
                person.id(),
                person.firstName(),
                person.lastName(),
                person.age()
        );
    }

    private void waitForEnter() {
        System.out.println();
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    private int askForId() {
        while (true) {
            System.out.println();
            System.out.print("Enter ID: ");
            String stringId = scanner.nextLine();

            try {
                int id = Integer.parseInt(stringId);

                if (id > 0) {
                    return id;
                } else {
                    System.out.println("ID must be a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. ID must be a positive "
                        + "integer.");
            }
        }
    }

    private int askForAge() {
        while (true) {
            System.out.print("Enter person age: ");
            String stringAge = scanner.nextLine();

            try {
                int age = Integer.parseInt(stringAge);

                if (age >= 0) {
                    return age;
                } else {
                    System.out.println("Age must be a non-negative integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Age must be a non-negative "
                        + "integer.");
            }
        }
    }
}
