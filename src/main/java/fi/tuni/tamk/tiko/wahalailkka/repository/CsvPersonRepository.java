package fi.tuni.tamk.tiko.wahalailkka.repository;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class CsvPersonRepository implements PersonRepository {
    private final String pathToFile;
    private final String csvDelimiter = ",";

    private MyList<Person> personList = new MyArrayList<>();
    private int nextId = 1;

    private final String headers = String.format("id%sfirstName%slastName%sage",
            csvDelimiter, csvDelimiter, csvDelimiter);

    private final int idIndex = 0;
    private final int firstNameIndex = 1;
    private final int lastNameIndex = 2;
    private final int ageIndex = 3;

    public CsvPersonRepository(final String pathToFile) {
        this.pathToFile = pathToFile;
        initializeCsv();
    }

    @Override
    public Person create(final Person person) {
        Person newPerson = new Person(nextId, person.firstName(),
                person.lastName(), person.age());
        writePersonToCsv(newPerson);
        nextId++;
        return newPerson;
    }

    @Override
    public MyList<Person> findAll() {
        readPersonsFromCsv();
        return personList;
    }

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

    @Override
    public Optional<Person> updateById(int id, Person person) {
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

    @Override
    public Optional<Person> deleteById(int id) {
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

    private void writeHeadersToCsv() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                pathToFile))) {
            writer.write(headers);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write headers to CSV file!");
        }
    }

    private void initializeCsv() {
        Path path = Path.of(pathToFile);

        try {
            if (!Files.exists(path) || Files.size(path) == 0) {
                writeHeadersToCsv();
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
            readPersonsFromCsv();
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
                if (!line.isEmpty()) {
                    String[] data = line.split(csvDelimiter);
                    int id = Integer.parseInt(data[idIndex]);
                    int age = Integer.parseInt(data[ageIndex]);
                    if (nextId < id) {
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
        writeHeadersToCsv();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                pathToFile, true))) {
            for (int i = 0; i < personList.size(); i++) {
                Person person = personList.get(i);
                writer.write(String.format("%d%s%s%s%s%s%d",
                        person.id(), csvDelimiter,
                        person.firstName(), csvDelimiter,
                        person.lastName(), csvDelimiter,
                        person.age()));
                writer.newLine();
            }
            readPersonsFromCsv();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Person list to CSV "
                    + "file!");
        }
    }
}
