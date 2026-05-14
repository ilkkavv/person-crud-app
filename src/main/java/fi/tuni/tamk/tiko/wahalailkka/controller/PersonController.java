package fi.tuni.tamk.tiko.wahalailkka.controller;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.model.PersonData;
import fi.tuni.tamk.tiko.wahalailkka.repository.PersonRepository;
import fi.tuni.tamk.tiko.wahalailkka.util.PersonSorter;

import static fi.tuni.tamk.tiko.wahalailkka.validation.PersonValidator.
        validatePersonData;
import static fi.tuni.tamk.tiko.wahalailkka.validation.PersonValidator.
        validatePersonId;
import static fi.tuni.tamk.tiko.wahalailkka.validation.PersonValidator.
        validateAgeRange;
import static fi.tuni.tamk.tiko.wahalailkka.validation.SearchField.SEARCH;

import java.util.Optional;

import fi.tuni.tamk.tiko.wahalailkka.validation.SearchValidationError;
import fi.tuni.tamk.tiko.wahalailkka.validation.ValidationError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger LOGGER = LogManager.getLogger(
            PersonController.class);
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
        MyList<ValidationError> validationErrors = new MyArrayList<>();
        validationErrors.addAll(validatePersonData(newPersonData));

        if (validationErrors.isEmpty()) {
            PersonResult success = PersonResult.success(personRepository.create(
                    newPersonData));
            LOGGER.info("Created person: {}", success.person());
            return success;
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
     * Sorts the given list of persons by first or last name.
     * <p>
     * The given list is not modified. A copy of the list is created, sorted,
     * and returned as part of the result.
     *
     * @param personList the list of persons to sort
     * @param nameField the name field used for sorting (first or last name)
     * @param ascendingOrder {@code true} for ascending order,
     *                       {@code false} for descending order
     * @return a {@link PersonListResult} containing the sorted list of persons
     */
    public PersonListResult sortPersonsByName(final MyList<Person> personList,
                           final PersonSorter.NameField nameField,
                           final boolean ascendingOrder) {
        MyList<Person> sortedList = new MyArrayList<>();
        sortedList.addAll(personList);
        PersonSorter.sortByName(sortedList, nameField, ascendingOrder);

        return PersonListResult.success(sortedList);
    }

    /**
     * Sorts the given list of persons by age.
     * <p>
     * The given list is not modified. A copy of the list is created, sorted,
     * and returned as part of the result.
     *
     * @param personList the list of persons to sort
     * @param ascendingOrder {@code true} for ascending order (youngest first),
     *                       {@code false} for descending order (oldest first)
     * @return a {@link PersonListResult} containing the sorted list of persons
     */
    public PersonListResult sortPersonsByAge(final MyList<Person> personList,
                                             final boolean ascendingOrder) {
        MyList<Person> sortedList = new MyArrayList<>();
        sortedList.addAll(personList);
        PersonSorter.sortByAge(sortedList, ascendingOrder);

        return PersonListResult.success(sortedList);
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
        MyList<ValidationError> validationErrors = new MyArrayList<>();
        validationErrors.addAll(validatePersonId(id));

        if (validationErrors.isEmpty()) {
            Optional<Person> person = personRepository.findById(id);

            if (person.isEmpty()) {
                logNotFound(id);
                return PersonResult.notFound(notFoundMsg + id);
            } else {
                return PersonResult.success(person.get());
            }
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
            MyList<ValidationError> validationErrors =
                    new MyArrayList<>();
            validationErrors.add(new SearchValidationError(SEARCH,
                    "Search input cannot be empty."));
            return PersonListResult.failure(validationErrors);
        }

        return PersonListResult.success(personRepository.searchByName(
                searchInput));
    }

    /**
     * Searches for persons whose age falls within the given range.
     * <p>
     * The input range is validated before performing the search. If validation
     * fails, the result contains validation errors. Otherwise, the
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
        MyList<ValidationError> validationErrors = new MyArrayList<>();
        validationErrors.addAll(validateAgeRange(min, max));

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
     * fails, the result contains validation errors. If no person with the given
     * ID exists, the result contains a repository error message.
     * <p>
     * Before updating, the current person data is compared with the new data.
     * If no fields have changed, the repository update is skipped and the
     * current person is returned with an empty list of changed fields.
     *
     * @param id the ID of the person to update
     * @param firstName the new first name
     * @param lastName the new last name
     * @param age the new age
     * @return a {@link PersonUpdateResult} containing:
     * <ul>
     *     <li>the updated person if the update is successful</li>
     *     <li>the current person if no fields were changed</li>
     *     <li>a list of changed fields if any values were modified</li>
     *     <li>validation errors if the ID or input data is invalid</li>
     *     <li>an error message if no person with the given ID exists</li>
     * </ul>
     */
    public PersonUpdateResult updatePersonById(final int id,
                                         final String firstName,
                                         final String lastName,
                                         final int age) {
        MyList<ValidationError> validationErrors = new MyArrayList<>();
        validationErrors.addAll(validatePersonId(id));
        PersonData newPersonData = new PersonData(firstName, lastName, age);
        validationErrors.addAll(validatePersonData(newPersonData));

        if (!validationErrors.isEmpty()) {
            return PersonUpdateResult.validationFailure(validationErrors);
        }

        Optional<Person> person = personRepository.findById(id);

        if (person.isEmpty()) {
            PersonUpdateResult notFound =  PersonUpdateResult.notFound(
                    notFoundMsg + id);
            logNotFound(id);
            return notFound;
        }

        Person existingPerson = person.get();
        MyList<String> changedFields = compareFields(newPersonData,
                existingPerson);

        if (changedFields.isEmpty()) {
            PersonUpdateResult success = PersonUpdateResult.success(
                    existingPerson, changedFields);
            LOGGER.warn("Update skipped for person with ID {} because"
                    + " no fields changed", id);
            return success;
        }

        Optional<Person> updatedPerson = personRepository.updateById(id,
                newPersonData);

        if (updatedPerson.isEmpty()) {
            PersonUpdateResult notFound =  PersonUpdateResult.notFound(
                    notFoundMsg + id);
            logNotFound(id);
            return notFound;
        } else {
            LOGGER.info("Person with ID {} updated", id);
            return PersonUpdateResult.success(updatedPerson.get(),
                    changedFields);
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
        MyList<ValidationError> validationErrors = new MyArrayList<>();
        validationErrors.addAll(validatePersonId(id));

        if (validationErrors.isEmpty()) {
            Optional<Person> deletedPerson = personRepository.deleteById(id);

            if (deletedPerson.isEmpty()) {
                PersonResult notFound =  PersonResult.notFound(notFoundMsg
                        + id);
                logNotFound(id);
                return notFound;
            } else {
                LOGGER.info("Person with ID {} deleted", id);
                return PersonResult.success(deletedPerson.get());
            }
        } else {
            return PersonResult.validationFailure(validationErrors);
        }
    }

    private MyList<String> compareFields(final PersonData newData,
                                         final Person person) {
        MyList<String> changedFields = new MyArrayList<>();

        if (!newData.firstName().equals(person.firstName())) {
            changedFields.add("first name: " + person.firstName() + " -> "
                    + newData.firstName());
        }

        if (!newData.lastName().equals(person.lastName())) {
            changedFields.add("last name: " + person.lastName() + " -> "
                    + newData.lastName());
        }

        if (newData.age() != person.age()) {
            changedFields.add("age: " + person.age() + " -> " + newData.age());
        }

        return changedFields;
    }

    private void logNotFound(final int id) {
        LOGGER.warn(notFoundMsg + "{}", id);
    }
}
