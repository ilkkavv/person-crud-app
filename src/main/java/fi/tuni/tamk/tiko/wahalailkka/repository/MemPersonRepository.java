package fi.tuni.tamk.tiko.wahalailkka.repository;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.model.PersonData;
import fi.tuni.tamk.tiko.wahalailkka.util.PersonFilter;

import java.util.Optional;

/**
 * In-memory implementation of {@link PersonRepository}.
 * <p>
 * This repository stores {@link Person} objects in a custom list
 * implementation. By default, it uses {@link MyArrayList}, but the underlying
 * data structure can be easily replaced with other list implementation as long
 * as it mplements the {@link MyList} interface.
 * <p>
 * Data is not persisted and will be lost when the application terminates.
 */
public class MemPersonRepository implements PersonRepository {
    /** Internal list storing persons. */
    private final MyList<Person> personList = new MyArrayList<>();
    /** Counter used to generate unique IDs for persons. */
    private int nextId = 1;

    /**
     * Creates a new person and assigns an unique ID.
     *
     * @param personData  the person data used to create a new person
     * @return the created person
     * @throws IllegalArgumentException if person is null
     */
    @Override
    public Person create(final PersonData personData) {
        if (personData == null) {
            throw new IllegalArgumentException("Person data must not be null.");
        }
        Person newPerson = new Person(nextId, personData.firstName(),
                personData.lastName(), personData.age());
        personList.add(newPerson);
        nextId++;
        return newPerson;
    }

    /**
     * Returns all persons stored in the repository.
     *
     * @return list of all persons
     */
    @Override
    public MyList<Person> findAll() {
        MyList<Person> newPersonList = new MyArrayList<>();
        newPersonList.addAll(personList);
        return newPersonList;
    }

    /**
     * Finds a person by ID.
     *
     * @param id the ID of the person
     * @return an {@link Optional} containing the found person,
     *         or empty if not found
     */
    @Override
    public Optional<Person> findById(final int id) {
        for (int i = 0; i < personList.size(); i++) {
            Person listedPerson = personList.get(i);
            if (listedPerson.id() == id) {
                return Optional.of(listedPerson);
            }
        }
        return Optional.empty();
    }

    /**
     * Searches for persons whose first or last name matches the given input.
     * <p>
     * This method delegates the filtering logic to {@link PersonFilter}.
     *
     * @param searchInput the text used to search for matching persons
     * @return a list of persons matching the given input;
     *         an empty list if no matches are found or input is invalid
     */
    @Override
    public MyList<Person> searchByName(final String searchInput) {
        return PersonFilter.filterByName(personList, searchInput);
    }

    /**
     * Searches for persons whose age falls within the given range.
     * <p>
     * This method delegates the filtering logic to {@link PersonFilter}.
     *
     * @param min the minimum age (inclusive)
     * @param max the maximum age (inclusive)
     * @return a list of persons whose age is within the given range;
     *         an empty list if no matches are found
     */
    @Override
    public MyList<Person> searchByAge(final int min, final int max) {
        return PersonFilter.filterByAge(personList, min, max);
    }

    /**
     * Updates a person identified by ID.
     * <p>
     * The ID remains unchanged, but other fields are updated.
     *
     * @param id the ID of the person to update
     * @param personData the new person data
     * @return an {@link Optional} containing the updated person,
     *         or empty if no person with the given ID exists
     * @throws IllegalArgumentException if person is null
     */
    @Override
    public Optional<Person> updateById(final int id,
                                       final PersonData personData) {
        if (personData == null) {
            throw new IllegalArgumentException("Person data must not be null.");
        }
        for (int i = 0; i < personList.size(); i++) {
            Person listedPerson = personList.get(i);
            if (listedPerson.id() == id) {
                Person updatedPerson = new Person(listedPerson.id(),
                        personData.firstName(), personData.lastName(),
                        personData.age());
                personList.set(i, updatedPerson);
                return Optional.of(updatedPerson);
            }
        }
        return Optional.empty();
    }

    /**
     * Deletes a person by ID.
     *
     * @param id the ID of the person to delete
     * @return an {@link Optional} containing the removed person,
     *         or empty if not found
     */
    @Override
    public Optional<Person> deleteById(final int id) {
        for (int i = 0; i < personList.size(); i++) {
            Person person = personList.get(i);
            if (person.id() == id) {
                personList.remove(i);
                return Optional.of(person);
            }
        }
        return Optional.empty();
    }
}
