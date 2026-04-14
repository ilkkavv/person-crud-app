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
