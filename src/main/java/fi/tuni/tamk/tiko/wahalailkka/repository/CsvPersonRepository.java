package fi.tuni.tamk.tiko.wahalailkka.repository;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * CSV-based implementation of the PersonRepository interface.
 * <p>
 * This repository stores and retrieves Person data from a CSV file.
 * It supports basic CRUD operations (create, read, update, delete).
 */
public class CsvPersonRepository implements PersonRepository {
    private final String pathToFile;

    private final String csvDelimiter = ",";
    private final String headers = String.format("id%sfirstName%slastName%sage",
            csvDelimiter, csvDelimiter, csvDelimiter);

    private MyList<Person> personList = new MyArrayList<>();
    private int nextId = 1;

    private final int idIndex = 0;
    private final int firstNameIndex = 1;
    private final int lastNameIndex = 2;
    private final int ageIndex = 3;

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
     * @param person the person data
     * @return the created person with an assigned ID
     */
    @Override
    public Person create(final Person person) {
        Person newPerson = new Person(nextId, person.firstName(),
                person.lastName(), person.age());
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
        return personList;
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
     * Updates a person with the given ID.
     * <p>
     * If a matching person is found, their data is replaced and the CSV file
     * is rewritten.
     *
     * @param id the ID of the person to update
     * @param person the new person data
     * @return an Optional containing the updated person if the given ID is
     * found, otherwise empty
     */
    @Override
    public Optional<Person> updateById(final int id, final Person person) {
        readPersonsFromCsv();

        boolean personFound = false;
        MyList<Person> newPersonList = new MyArrayList<>();
        Person updatedPerson = new Person(id, person.firstName(),
                person.lastName(), person.age());

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
            if (!Files.exists(path) || Files.size(path) == 0) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                        pathToFile))) {
                    writer.write(headers);
                    writer.newLine();
                }
            } else {
                readPersonsFromCsv();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize CSV file!");
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
            throw new RuntimeException("Failed to write Person data to CSV "
                    + "file!");
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
                    int id = Integer.parseInt(data[idIndex]);
                    int age = Integer.parseInt(data[ageIndex]);
                    if (nextId <= id) {
                        nextId = id + 1;
                    }
                    personList.add(new Person(id, data[firstNameIndex],
                            data[lastNameIndex], age));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from CSV file!");
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
            throw new RuntimeException("Failed to write Person list to CSV "
                    + "file!");
        }
    }
}
