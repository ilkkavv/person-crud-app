package fi.tuni.tamk.tiko.wahalailkka.repository;

import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.PersonData;

import java.util.Optional;

/**
 * Repository interface for managing {@link Person} entities.
 * <p>
 * Provides CRUD operations (Create, Read, Update, Delete) for Persons.
 */
public interface PersonRepository {
    /**
     * Creates and stores a new person.
     * <p>
     * The repository assigns a unique ID to the person.
     *
     * @param personData the person data used to create a new person
     * @return the created person
     */
    Person create(PersonData personData);

    /**
     * Returns all stored persons.
     *
     * @return a list containing all persons
     */
    MyList<Person> findAll();

    /**
     * Finds a person by id.
     *
     * @param id the id of the person
     * @return an Optional containing the person if found, otherwise empty
     */
    Optional<Person> findById(int id);

    /**
     * Searches for persons whose first or last name contains the given input.
     *
     * @param searchInput the text used to search for matching persons
     * @return a list of persons whose name matches the given input;
     *         an empty list if no matches are found
     */
    MyList<Person> searchByName(String searchInput);

    /**
     * Searches for persons whose age falls within the given range.
     * <p>
     * Both minimum and maximum values are inclusive.
     *
     * @param min the minimum age (inclusive)
     * @param max the maximum age (inclusive)
     * @return a list of persons whose age is within the given range;
     *         an empty list if no matches are found
     */
    MyList<Person> searchByAge(int min, int max);

    /**
     * Updates an existing person by id.
     *
     * @param id the id of the person to update
     * @param personData the new data for the person
     * @return an Optional containing the updated person if found,
     *         otherwise empty
     */
    Optional<Person> updateById(int id, PersonData personData);

    /**
     * Deletes a person by id.
     *
     * @param id the id of the person to delete
     * @return an Optional containing the deleted person if found,
     *         otherwise empty
     */
    Optional<Person> deleteById(int id);
}
