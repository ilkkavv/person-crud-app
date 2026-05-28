package fi.tuni.tamk.tiko.wahalailkka.ui.cli;

import fi.tuni.tamk.tiko.wahalailkka.controller.PersonController;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonListResult;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonResult;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonUpdateResult;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.repository.CsvRepositoryException;
import fi.tuni.tamk.tiko.wahalailkka.ui.AppUi;
import fi.tuni.tamk.tiko.wahalailkka.util.PersonSorter;
import fi.tuni.tamk.tiko.wahalailkka.validation.ValidationError;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Command-line user interface for the Person CRUD App.
 * <p>
 * This class is responsible for interacting with the user through the terminal.
 * It shows the menu, reads user input, calls the controller, and prints
 * results.
 */
public class Cli implements AppUi {
    private static final Logger LOGGER = LogManager.getLogger(Cli.class);
    /** Flag indicating whether the CLI main loop is running. */
    private boolean isRunning = false;
    /** Scanner used for reading user input from standard input. */
    private final Scanner scanner = new Scanner(System.in);
    /** Controller used to handle person-related application logic. */
    private final PersonController controller;
    /** List of persons used for displaying and sorting results in the CLI. */
    private MyList<Person> currentList;

    private static final String CMD_1 = "1";
    private static final String CMD_C = "c";
    private static final String CMD_CREATE = "create";
    private static final String CMD_2 = "2";
    private static final String CMD_L = "l";
    private static final String CMD_LIST = "list";
    private static final String CMD_3 = "3";
    private static final String CMD_F = "f";
    private static final String CMD_FIND = "find";
    private static final String CMD_4 = "4";
    private static final String CMD_N = "n";
    private static final String CMD_NAME = "name";
    private static final String CMD_5 = "5";
    private static final String CMD_A = "a";
    private static final String CMD_AGE = "age";
    private static final String CMD_6 = "6";
    private static final String CMD_U = "u";
    private static final String CMD_UPDATE = "update";
    private static final String CMD_7 = "7";
    private static final String CMD_D = "d";
    private static final String CMD_DELETE = "delete";
    private static final String CMD_8 = "8";
    private static final String CMD_H = "h";
    private static final String CMD_HELP = "help";
    private static final String CMD_9 = "9";
    private static final String CMD_E = "e";
    private static final String CMD_EXIT = "exit";

    private static final String HELP_MSG = """
    NAME
        person-crud-app

    VERSION
        1.0.0

    DESCRIPTION
        person-crud-app is an application for managing a collection
        of persons. It allows the user to create, view, update,
        and delete person records.

        Each person consists of an ID, first name, last name,
        and age. IDs are assigned automatically by the application.

        The application supports basic CRUD operations through
        an interactive menu.

    COMMANDS
        1, c, create
            Create a new person

        2, l, list
            List all persons

        3, f, find
            Find person by ID

        4, n, name
            Search persons by name

        5, a, age
            Search persons by age range

        6, u, update
            Update person data by ID

        7, d, delete
            Delete person by ID

        8, h, help
            Show this help message

        9, e, exit
            Exit the application
    """;

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
    @Override
    public void run() {
        isRunning = true;

        System.out.println();
        System.out.println("* --- Person CRUD App --- *");

        while (isRunning) {
            try {
                showMenu();
                handleCommand();
            } catch (CsvRepositoryException e) {
                System.out.println();
                System.err.println("ERROR: " + e.getMessage());
            } catch (Exception e) {
                LOGGER.error("Unexpected error", e);
                System.out.println();
                System.err.println("ERROR: Unexpected error.");
            }
        }
    }

    private void showMenu() {
        System.out.println();
        System.out.println("1) Create new person");
        System.out.println("2) List all persons");
        System.out.println("3) Find person by ID");
        System.out.println("4) Search persons by Name");
        System.out.println("5) Search persons by Age range");
        System.out.println("6) Update person data by ID");
        System.out.println("7) Delete person by ID");
        System.out.println("8) Help");
        System.out.println("9) Exit");
        System.out.println();
        System.out.print("Enter command: ");
    }

    private void handleCommand() {
        String input = scanner.nextLine().toLowerCase().trim();

        switch (input) {
            case CMD_1, CMD_C, CMD_CREATE:
                create();
                break;
            case CMD_2, CMD_L, CMD_LIST:
                list();
                break;
            case CMD_3, CMD_F, CMD_FIND:
                find();
                break;
            case CMD_4, CMD_N, CMD_NAME:
                searchByName();
                break;
            case CMD_5, CMD_A, CMD_AGE:
                searchByAge();
                break;
            case CMD_6, CMD_U, CMD_UPDATE:
                update();
                break;
            case CMD_7, CMD_D, CMD_DELETE:
                delete();
                break;
            case CMD_8, CMD_H, CMD_HELP:
                help();
                break;
            case CMD_9, CMD_E, CMD_EXIT:
                exit();
                break;
            default:
                System.out.println();
                System.out.println("'" + input + "' is not a valid command.");
                waitForEnter();
        }
    }

    private void create() {
        boolean success = false;

        while (!success) {
            System.out.println();
            System.out.println("Enter values (or type 'cancel' in any field"
                    + " to return):");
            System.out.println();
            System.out.print("Enter person first name: ");
            String firstName = scanner.nextLine().trim();
            if (isCancelCommand(firstName)) {
                return;
            }

            System.out.print("Enter person last name: ");
            String lastName = scanner.nextLine().trim();
            if (isCancelCommand(lastName)) {
                return;
            }

            int age;

            while (true) {
                System.out.print("Enter person age: ");
                String stringAge = scanner.nextLine().trim();
                if (isCancelCommand(stringAge)) {
                    return;
                }

                try {
                    age = Integer.parseInt(stringAge);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println();
                    System.out.println("Invalid input. Age must be a valid"
                            + " integer.");
                    System.out.println();
                }
            }

            PersonResult result = controller.createPerson(firstName, lastName,
                    age);
            printPersonResult(result, "New person created:");
            success = result.isSuccess();

            waitForEnter();
        }
    }

    private boolean isCancelCommand(final String input) {
        return input.equalsIgnoreCase("cancel");
    }

    private void list() {
        PersonListResult result = controller.findAllPersons();

        printPersonListResult(result);

        if (result.isSuccess() && result.personList().size() > 1) {
            currentList = result.personList();
            askForSorting();
        } else {
            waitForEnter();
        }
    }

    private void find() {
        int id = askForId();

        PersonResult result = controller.findPersonById(id);
        printPersonResult(result, null);
        waitForEnter();
    }

    private void searchByName() {
        PersonListResult result;

        do {
            System.out.println();
            System.out.print("Search name: ");
            String searchInput = scanner.nextLine().trim();

            result = controller.searchPersonsByName(searchInput);

            printPersonListResult(result);

        } while (!result.isSuccess());

        if (result.personList().size() > 1) {
            currentList = result.personList();
            askForSorting();
        } else {
            waitForEnter();
        }
    }

    private void searchByAge() {
        PersonListResult result = null;

        do {
            System.out.println();
            System.out.print("Enter minimum age: ");
            String stringMin = scanner.nextLine().trim();
            System.out.print("Enter maximum age: ");
            String stringMax = scanner.nextLine().trim();

            try {
                int min = Integer.parseInt(stringMin);
                int max = Integer.parseInt(stringMax);

                result = controller.searchPersonsByAge(min, max);

                printPersonListResult(result);
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println("Invalid input. Both values must be"
                        + " integers.");
            }
        } while (result == null || !result.isSuccess());

        if (result.personList().size() > 1) {
            currentList = result.personList();
            askForSorting();
        } else {
            waitForEnter();
        }
    }

    private void askForSorting() {
        boolean sort = showChoices("Sort?", "Yes", "No (return to main menu)");

        if (sort) {
            handleSorting();
        }
    }

    private void handleSorting() {
        boolean sortByAge;
        boolean sortByFirstName = false;
        boolean sortByAscendingOrder;

        sortByAge = showChoices("Sort options:", "Sort by age",
                "Sort by name");

        if (!sortByAge) {
            sortByFirstName = showChoices("Sort options:", "Sort by first name",
                    "Sort by last name");
        }

        sortByAscendingOrder = showChoices("Sort options:",
                "Sort by ascending order", "Sort by descending order");

        PersonListResult result;

        if (sortByAge) {
            result = controller.sortPersonsByAge(currentList,
                    sortByAscendingOrder);
        } else {
            if (sortByFirstName) {
                result = controller.sortPersonsByName(currentList,
                        PersonSorter.NameField.FIRST_NAME,
                        sortByAscendingOrder);
            } else {
                result = controller.sortPersonsByName(currentList,
                        PersonSorter.NameField.LAST_NAME,
                        sortByAscendingOrder);
            }
        }

        printPersonListResult(result);
        currentList = result.personList();
        askForSorting();
    }

    private boolean showChoices(final String question,
                                final String firstChoice,
                                final String secondChoice) {
        while (true) {
            System.out.println();
            System.out.println(question);
            System.out.println();
            System.out.println("1) " + firstChoice);
            System.out.println("2) " + secondChoice);
            System.out.println();
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            if (!choice.equals(CMD_1) && !choice.equals(CMD_2)) {
                System.out.println("'" + choice + "' is not a valid choice.");
                waitForEnter();
            } else {
                return choice.equals(CMD_1);
            }
        }
    }

    private void update() {
        int id = askForId();

        PersonResult result = controller.findPersonById(id);

        if (!result.isSuccess()) {
            printPersonResult(result, null);
        } else {
            boolean success = false;
            Person person = result.person();

            while (!success) {
                System.out.println();
                System.out.println("Leave fields empty to keep current"
                        + " values.");
                System.out.println();
                System.out.print("Enter new first name ["
                        + person.firstName() + "]: ");
                String firstName = scanner.nextLine().trim();
                System.out.print("Enter new last name ["
                        + person.lastName() + "]: ");
                String lastName = scanner.nextLine().trim();
                System.out.print("Enter new age [" + person.age() + "]: ");
                String stringAge = scanner.nextLine().trim();

                if (firstName.isEmpty()) {
                    firstName = person.firstName();
                }

                if (lastName.isEmpty()) {
                    lastName = person.lastName();
                }

                if (stringAge.isEmpty()) {
                    stringAge = String.valueOf(person.age());
                }

                int age;

                try {
                    age = Integer.parseInt(stringAge);

                    PersonUpdateResult updateResult =
                            controller.updatePersonById(id, firstName, lastName,
                                    age);
                    printPersonUpdateResult(updateResult);
                    success = updateResult.isSuccess();
                } catch (NumberFormatException e) {
                    System.out.println();
                    System.out.println("Invalid input. Age must be a valid"
                            + " integer.");
                }
            }
        }

        waitForEnter();
    }

    private void delete() {
        int id = askForId();

        PersonResult result = controller.findPersonById(id);

        if (result.isSuccess()) {
            Person person = result.person();
            String question = String.format("Are you sure you want to delete:"
                    + " [ID: %d | %s %s | Age: %d]?",
                    person.id(),
                    person.firstName(),
                    person.lastName(),
                    person.age()
            );

            if (showChoices(question, "Yes", "No")) {
                PersonResult deleteResult = controller.deletePersonById(id);
                printPersonResult(deleteResult, "Person data deleted:");
            }
        } else {
            printPersonResult(result, null);
        }

        waitForEnter();
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

    private void printPersonResult(final PersonResult result,
                                   final String operationMsg) {
        System.out.println();
        if (result.isSuccess()) {
            if (operationMsg != null) {
                System.out.println(operationMsg);
            }
            printPersonData(result.person());
        } else {
            if (result.repositoryError() != null) {
                System.out.println(result.repositoryError());
            } else {
                printValidationErrors(result.validationErrors());
            }
        }
    }

    private void printPersonListResult(final PersonListResult result) {
        System.out.println();
        if (result.isSuccess()) {
            MyList<Person> personList = result.personList();

            if (personList.isEmpty()) {
                System.out.println("No persons found.");
            } else {
                for (int i = 0; i < personList.size(); i++) {
                    printPersonData(personList.get(i));
                }
            }
        } else {
            printValidationErrors(result.validationErrors());
        }
    }

    private void printPersonUpdateResult(final PersonUpdateResult result) {
        System.out.println();
        if (result.isSuccess()) {
            if (result.changedFields().isEmpty()) {
                System.out.println("No fields were changed.");
            } else {
                System.out.println("Person data updated.");
                System.out.println();
                System.out.println("Updated fields:");

                for (int i = 0; i < result.changedFields().size(); i++) {
                    System.out.println(result.changedFields().get(i));
                }
            }
        } else {
            if (result.repositoryError() != null) {
                System.out.println(result.repositoryError());
            } else {
                printValidationErrors(result.validationErrors());
            }
        }
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

    private void printValidationErrors(
            final MyList<ValidationError> errorList) {
        for (int i = 0; i < errorList.size(); i++) {
            System.out.println(errorList.get(i).getMessage());
        }
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
            String stringId = scanner.nextLine().trim();

            try {
                return Integer.parseInt(stringId);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. ID must be a number.");
            }
        }
    }
}
