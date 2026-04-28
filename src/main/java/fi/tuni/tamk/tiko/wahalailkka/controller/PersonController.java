package fi.tuni.tamk.tiko.wahalailkka.controller;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.model.PersonData;
import fi.tuni.tamk.tiko.wahalailkka.repository.PersonRepository;
import fi.tuni.tamk.tiko.wahalailkka.validation.PersonValidator;

import static fi.tuni.tamk.tiko.wahalailkka.validation.PersonValidator.
        validatePersonData;
import static fi.tuni.tamk.tiko.wahalailkka.validation.PersonValidator.
        validatePersonId;
import static fi.tuni.tamk.tiko.wahalailkka.validation.PersonValidator.
        validateAgeRange;

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
    /** Message displayed when Person with given ID is not found. */
    private final String notFoundMsg = "Person not found with ID: ";

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
     *     <li>validation errors if the input data is invalid</li>
     * </ul>
     */
    public PersonResult createPerson(final String firstName,
                                     final String lastName, final int age) {
        PersonData newPersonData = new PersonData(firstName, lastName, age);
        MyList<String> validationErrors = validatePersonData(newPersonData);

        if (validationErrors.isEmpty()) {
            return PersonResult.success(personRepository.create(newPersonData));
        } else {
            return PersonResult.validationFailure(validationErrors);
        }
    }

    /**
     * Retrieves all persons from the repository.
     *
     * @return a {@link PersonListResult} containing the list of all persons
     */
    public PersonListResult findAllPersons() {
        return PersonListResult.success(personRepository.findAll());
    }

    /**
     * Finds a person by their ID.
     * <p>
     * The given ID is validated before querying the repository. If the ID is
     * invalid, the result contains validation errors. If the ID is valid but no
     * matching person exists, the result contains a repository error message.
     *
     * @param id the ID of the person to find
     * @return a {@link PersonResult} containing:
     * <ul>
     *     <li>the found person if successful</li>
     *     <li>validation errors if the ID is invalid</li>
     *     <li>an error message if no person with the given ID exists</li>
     * </ul>
     */
    public PersonResult findPersonById(final int id) {
        MyList<String> validationErrors = validatePersonId(id);

        if (validationErrors.isEmpty()) {
            Optional<Person> person = personRepository.findById(id);
            return person.map(PersonResult::success)
                    .orElseGet(() -> PersonResult.notFound(notFoundMsg + id));
        } else {
            return PersonResult.validationFailure(validationErrors);
        }
    }

    /**
     * Searches for persons whose first or last name matches the given input.
     * <p>
     * The input is validated before performing the search. If the input is
     * {@code null} or empty after trimming, the result contains validation
     * error. Otherwise, the search is delegated to the repository.
     *
     * @param searchInput the text used to search for matching persons
     * @return a {@link PersonListResult} containing:
     * <ul>
     *     <li>the list of matching persons if the search is successful</li>
     *     <li>validation error if the input is invalid</li>
     * </ul>
     */
    public PersonListResult searchPersonsByName(final String searchInput) {
        if (searchInput == null || searchInput.trim().isEmpty()) {
            MyList<String> validationErrors = new MyArrayList<>();
            validationErrors.add("Search input cannot be empty.");
            return PersonListResult.failure(validationErrors);
        }

        return PersonListResult.success(personRepository.searchByName(
                searchInput));
    }

    /**
     * Searches for persons whose age falls within the given range.
     * <p>
     * The input range is validated before performing the search. If validation
     * fails, the result contains validation error messages. Otherwise, the
     * search is delegated to the repository layer.
     *
     * @param min the minimum age (inclusive)
     * @param max the maximum age (inclusive)
     * @return a {@link PersonListResult} containing:
     * <ul>
     *     <li>the list of matching persons if the search is successful</li>
     *     <li>validation errors if the given range is invalid</li>
     * </ul>
     */
    public PersonListResult searchPersonsByAge(final int min, final int max) {
        MyList<String> validationErrors = validateAgeRange(min, max);

        if (validationErrors.isEmpty()) {
            return PersonListResult.success(personRepository.searchByAge(
                    min, max));
        } else {
            return PersonListResult.failure(validationErrors);
        }
    }

    /**
     * Updates a person identified by the given ID.
     * <p>
     * The given ID and input data are validated before updating. If validation
     * fails, the result contains validation errors. If validation succeeds but
     * no person with the given ID exists, the result contains a repository
     * error message.
     *
     * @param id the ID of the person to update
     * @param firstName the new first name
     * @param lastName the new last name
     * @param age the new age
     * @return a {@link PersonResult} containing:
     * <ul>
     *     <li>the updated person if successful</li>
     *     <li>validation errors if the ID or input data is invalid</li>
     *     <li>an error message if no person with the given ID exists</li>
     * </ul>
     */
    public PersonResult updatePersonById(final int id,
                                         final String firstName,
                                         final String lastName,
                                         final int age) {
        MyList<String> validationErrors = validatePersonId(id);
        PersonData newPersonData = new PersonData(firstName, lastName, age);
        validationErrors.addAll(validatePersonData(newPersonData));

        if (validationErrors.isEmpty()) {
            Optional<Person> updatedPerson = personRepository.updateById(id,
                    newPersonData);
            return updatedPerson.map(PersonResult::success)
                    .orElseGet(() -> PersonResult.notFound(notFoundMsg + id));
        } else {
            return PersonResult.validationFailure(validationErrors);
        }
    }

    /**
     * Deletes a person by their ID.
     * <p>
     * The given ID is validated before deleting. If the ID is invalid, the
     * result contains validation errors. If the ID is valid but no matching
     * person exists, the result contains a repository error message.
     *
     * @param id the ID of the person to delete
     * @return a {@link PersonResult} containing:
     * <ul>
     *     <li>the deleted person if successful</li>
     *     <li>validation errors if the ID is invalid</li>
     *     <li>an error message if no person with the given ID exists</li>
     * </ul>
     */
    public PersonResult deletePersonById(final int id) {
        MyList<String> validationErrors = validatePersonId(id);

        if (validationErrors.isEmpty()) {
            Optional<Person> deletedPerson = personRepository.deleteById(id);
            return deletedPerson.map(PersonResult::success)
                    .orElseGet(() -> PersonResult.notFound(notFoundMsg + id));
        } else {
            return PersonResult.validationFailure(validationErrors);
        }
    }
}
