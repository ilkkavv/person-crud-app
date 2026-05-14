package fi.tuni.tamk.tiko.wahalailkka.repository;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.model.PersonData;
import fi.tuni.tamk.tiko.wahalailkka.util.PersonFilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CSV-based implementation of the PersonRepository interface.
 * <p>
 * This repository stores and retrieves Person data from a CSV file.
 * It supports basic CRUD operations (create, read, update, delete).
 */
public class CsvPersonRepository implements PersonRepository {
    private static final Logger LOGGER = LogManager.getLogger(
            CsvPersonRepository.class);

    private final String pathToFile;

    private final String csvDelimiter = ",";
    private final String headers = String.format("id%sfirstName%slastName%sage",
            csvDelimiter, csvDelimiter, csvDelimiter);

    private MyList<Person> personList = new MyArrayList<>();
    private int nextId = 1;

    private static final int ID_INDEX = 0;
    private static final int FIRST_NAME_INDEX = 1;
    private static final int LAST_NAME_INDEX = 2;
    private static final int AGE_INDEX = 3;

    private static final String INIT_ERR_MSG = "Failed to initialize CSV file.";
    private static final String DATA_WRITE_ERR_MSG = "Failed to write person"
            + " data to CSV file.";
    private static final String DATA_READ_ERR_MSG = "Failed to read from CSV"
            + " file.";
    private static final String LIST_WRITE_ERR_MSG = "Failed to write person"
            + "list to CSV file.";

    /**
     * Constructs a CsvPersonRepository using the given file path.
     * <p>
     * If the file does not exist or is empty, it is initialized with a header
     * row. Otherwise, existing data is loaded and the next available ID is
     * determined.
     *
     * @param pathToFile the path to the CSV file used for persistence
     */
    public CsvPersonRepository(final String pathToFile) {
        this.pathToFile = pathToFile;
        initializeCsv();
    }

    /**
     * Creates a new person and appends it to the CSV file.
     *
     * @param personData the person data used to create a new person
     * @return the created person with an assigned ID
     */
    @Override
    public Person create(final PersonData personData) {
        Person newPerson = new Person(nextId, personData.firstName(),
                personData.lastName(), personData.age());
        writePersonToCsv(newPerson);
        nextId++;
        return newPerson;
    }

    /**
     * Retrieves all persons from the CSV file.
     *
     * @return a list of all persons
     */
    @Override
    public MyList<Person> findAll() {
        readPersonsFromCsv();

        MyList<Person> newPersonList = new MyArrayList<>();
        newPersonList.addAll(personList);
        return newPersonList;
    }

    /**
     * Finds a person by their ID.
     *
     * @param id the ID of the person to find
     * @return an Optional containing the person if found, otherwise empty
     */
    @Override
    public Optional<Person> findById(final int id) {
        readPersonsFromCsv();
        for (int i = 0; i < personList.size(); i++) {
            if (id == personList.get(i).id()) {
                return Optional.of(personList.get(i));
            }
        }
        return Optional.empty();
    }

    /**
     * Searches for persons whose first or last name matches the given input.
     * <p>
     * The data is first read from the CSV file to ensure up-to-date results.
     * The filtering logic is delegated to {@link PersonFilter}.
     *
     * @param searchInput the text used to search for matching persons
     * @return a list of persons matching the given input;
     *         an empty list if no matches are found or input is invalid
     */
    @Override
    public MyList<Person> searchByName(final String searchInput) {
        readPersonsFromCsv();
        return PersonFilter.filterByName(personList, searchInput);
    }

    /**
     * Searches for persons whose age falls within the given range.
     * <p>
     * The data is first read from the CSV file to ensure up-to-date results.
     * The filtering logic is delegated to {@link PersonFilter}.
     *
     * @param min the minimum age (inclusive)
     * @param max the maximum age (inclusive)
     * @return a list of persons whose age is within the given range;
     *         an empty list if no matches are found
     */
    @Override
    public MyList<Person> searchByAge(final int min, final int max) {
        readPersonsFromCsv();
        return PersonFilter.filterByAge(personList, min, max);
    }

    /**
     * Updates a person with the given ID.
     * <p>
     * If a matching person is found, their data is replaced and the CSV file
     * is rewritten.
     *
     * @param id the ID of the person to update
     * @param personData the new person data
     * @return an Optional containing the updated person if the given ID is
     * found, otherwise empty
     */
    @Override
    public Optional<Person> updateById(final int id,
                                       final PersonData personData) {
        readPersonsFromCsv();

        boolean personFound = false;
        MyList<Person> newPersonList = new MyArrayList<>();
        Person updatedPerson = new Person(id, personData.firstName(),
                personData.lastName(), personData.age());

        for (int i = 0; i < personList.size(); i++) {
            if (id == personList.get(i).id()) {
                newPersonList.add(updatedPerson);
                personFound = true;
            } else {
                newPersonList.add(personList.get(i));
            }
        }

        if (personFound) {
            personList = newPersonList;
            writePersonListToCsv();
            return Optional.of(updatedPerson);
        }
        return Optional.empty();
    }

    /**
     * Deletes a person with the given ID.
     * <p>
     * If a matching person is found, it is removed and the CSV file is
     * rewritten.
     *
     * @param id the ID of the person to delete
     * @return an Optional containing the deleted person if the given ID is
     * found, otherwise empty
     */
    @Override
    public Optional<Person> deleteById(final int id) {
        readPersonsFromCsv();

        boolean personFound = false;
        MyList<Person> newPersonList = new MyArrayList<>();
        Person deletedPerson = null;

        for (int i = 0; i < personList.size(); i++) {
            if (id == personList.get(i).id()) {
                deletedPerson = personList.get(i);
                personFound = true;
            } else {
                newPersonList.add(personList.get(i));
            }
        }

        if (personFound) {
            personList = newPersonList;
            writePersonListToCsv();
            return Optional.of(deletedPerson);
        }
        return Optional.empty();
    }

    private void initializeCsv() {
        Path path = Path.of(pathToFile);

        try {
            Path parent = path.getParent();
            if (parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }

            if (Files.notExists(path) || Files.size(path) == 0) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                        pathToFile))) {
                    writer.write(headers);
                    writer.newLine();
                }
            } else {
                readPersonsFromCsv();
            }
        } catch (IOException e) {
            LOGGER.error(INIT_ERR_MSG, e);
            throw new CsvRepositoryException(INIT_ERR_MSG, e);
        }
    }

    private void writePersonToCsv(final Person person) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                pathToFile, true))) {
            writer.write(String.format("%d%s%s%s%s%s%d",
                    person.id(), csvDelimiter,
                    person.firstName(), csvDelimiter,
                    person.lastName(), csvDelimiter,
                    person.age()));
            writer.newLine();
        } catch (IOException e) {
            LOGGER.error(DATA_WRITE_ERR_MSG, e);
            throw new CsvRepositoryException(DATA_WRITE_ERR_MSG, e);
        }
    }

    private void readPersonsFromCsv() {
        personList.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(
                pathToFile))) {
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    String[] data = line.split(csvDelimiter);
                    int id = Integer.parseInt(data[ID_INDEX]);
                    int age = Integer.parseInt(data[AGE_INDEX]);
                    if (nextId <= id) {
                        nextId = id + 1;
                    }
                    personList.add(new Person(id, data[FIRST_NAME_INDEX],
                            data[LAST_NAME_INDEX], age));
                }
            }
        } catch (IOException | NumberFormatException
                 | ArrayIndexOutOfBoundsException e){
            LOGGER.error(DATA_READ_ERR_MSG, e);
            throw new CsvRepositoryException(DATA_READ_ERR_MSG, e);
        }
    }

    private void writePersonListToCsv() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                pathToFile))) {
            writer.write(headers);
            writer.newLine();
            for (int i = 0; i < personList.size(); i++) {
                Person person = personList.get(i);
                writer.write(String.format("%d%s%s%s%s%s%d",
                        person.id(), csvDelimiter,
                        person.firstName(), csvDelimiter,
                        person.lastName(), csvDelimiter,
                        person.age()));
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.error(LIST_WRITE_ERR_MSG, e);
            throw new CsvRepositoryException(LIST_WRITE_ERR_MSG, e);
        }
    }
}
