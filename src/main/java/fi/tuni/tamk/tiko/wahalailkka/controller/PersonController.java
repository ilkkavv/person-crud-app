package fi.tuni.tamk.tiko.wahalailkka.controller;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.model.PersonData;
import fi.tuni.tamk.tiko.wahalailkka.repository.PersonRepository;
import static fi.tuni.tamk.tiko.wahalailkka.validation.PersonValidator.
        validatePersonData;

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
    /** Stores validation error messages */
    private final MyList<String> validationErrors = new MyArrayList<>();

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
     * The input data is validated before creation. If validation fails, the
     * result contains validation errors. Otherwise, a new person is created
     * and assigned a unique ID by the repository.
     *
     * @param firstName the first name of the person
     * @param lastName the last name of the person
     * @param age the age of the person
     * @return a {@link PersonResult} containing:
     * <ul>
     *     <li>the created person if successful</li>
     *     <li>validation errors if input is invalid</li>
     * </ul>
     */
    public PersonResult createPerson(final String firstName,
                                     final String lastName, final int age) {
        PersonData newPersonData = new PersonData(firstName, lastName, age);
        MyList<String> validationErrors = validatePersonData(newPersonData);

        if (validationErrors.size() == 0) {
            return new PersonResult(true,
                    personRepository.create(newPersonData),
                    validationErrors, null);
        } else {
            return new PersonResult(false, null,
                    validationErrors, null);
        }
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
     * <p>
     * The input data is first validated. If validation fails, the result
     * contains validation errors. If validation succeeds but no person with
     * the given ID exists, the result contains a repository error message.
     *
     * @param id the ID of the person to update
     * @param firstName the new first name
     * @param lastName the new last name
     * @param age the new age
     * @return a {@link PersonResult} containing:
     * <ul>
     *     <li>the updated person if successful</li>
     *     <li>validation errors if input is invalid</li>
     *     <li>an error message if the person was not found</li>
     * </ul>
     */
    public PersonResult updatePersonById(final int id,
                                         final String firstName,
                                         final String lastName,
                                         final int age) {
        PersonData newPersonData = new PersonData(firstName, lastName, age);
        MyList<String> validationErrors = validatePersonData(newPersonData);

        if (validationErrors.size() == 0) {
            Optional<Person> updatedPerson = personRepository.updateById(id,
                    newPersonData);
            if (updatedPerson.isEmpty()) {
                return new PersonResult(false, null,
                        validationErrors,
                        "Person not found with ID: " + id);
            } else {
                return new PersonResult(true, updatedPerson.get(),
                        validationErrors, null);
            }
        } else {
            return new PersonResult(false, null,
                    validationErrors, null);
        }
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
