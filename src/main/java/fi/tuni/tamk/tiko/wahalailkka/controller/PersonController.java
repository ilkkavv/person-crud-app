package fi.tuni.tamk.tiko.wahalailkka.controller;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.repository.PersonRepository;

import java.util.Optional;

/**
 * Controller class responsible for handling application logic related to
 * persons.
 * <p>
 * This class acts as an intermediary between the user interface and the
 * {@link PersonRepository}. It receives input data from the UI, creates
 * {@link Person} objects when needed, and delegates data operations to the
 * repository.
 */
public class PersonController {
    /** Repository used for storing and managing persons. */
    private final PersonRepository personRepository;

    /**
     * Constructs a new PersonController with the given repository.
     *
     * @param personRepository the repository implementation to use
     */
    public PersonController(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Creates a new person with the given data.
     * <p>
     * The ID is assigned by the repository.
     *
     * @param firstName the first name of the person
     * @param lastName the last name of the person
     * @param age the age of the person
     * @return the created person with assigned ID
     */
    public Person createPerson(final String firstName, final String lastName,
                         final int age) {
        return personRepository.create(new Person(0, firstName, lastName,
                age));
    }

    /**
     * Retrieves all persons from the repository.
     *
     * @return a list containing all persons
     */
    public MyList<Person> findAllPersons() {
        return personRepository.findAll();
    }

    /**
     * Finds a person by their ID.
     *
     * @param id the ID of the person
     * @return an {@link Optional} containing the found person,
     *         or empty if not found
     */
    public Optional<Person> findPersonById(final int id) {
        return personRepository.findById(id);
    }

    /**
     * Updates a person identified by the given ID.
     *
     * @param id the ID of the person to update
     * @param firstName the new first name
     * @param lastName the new last name
     * @param age the new age
     * @return an {@link Optional} containing the updated person,
     *         or empty if no person with the given ID exists
     */
    public Optional<Person> updatePersonById(final int id,
                                             final String firstName,
                                             final String lastName,
                                             final int age) {
        return personRepository.updateById(id, new Person(0, firstName,
                lastName, age));
    }

    /**
     * Deletes a person by their ID.
     *
     * @param id the ID of the person to delete
     * @return an {@link Optional} containing the deleted person,
     *         or empty if not found
     */
    public Optional<Person> deletePersonById(final int id) {
        return personRepository.deleteById(id);
    }
}
