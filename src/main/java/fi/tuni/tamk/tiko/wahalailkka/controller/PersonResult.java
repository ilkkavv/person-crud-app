package fi.tuni.tamk.tiko.wahalailkka.controller;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.validation.PersonValidationError;

/**
 * Represents the result of a controller operation.
 * <p>
 * This record encapsulates the outcome of an operation, including:
 * <ul>
 *     <li>whether the operation was successful</li>
 *     <li>the resulting {@link Person}, if successful</li>
 *     <li>a list of structured validation errors, if validation failed</li>
 *     <li>a repository error message, if the operation failed at the data layer
 *     </li>
 * </ul>
 *
 * @param isSuccess true if the operation succeeded, false otherwise
 * @param person the resulting person, or null if the operation failed
 * @param validationErrors structured validation errors, or an empty list if none
 * @param repositoryError an error message related to repository operations,
 *                        or null if none
 */
public record PersonResult(boolean isSuccess, Person person,
                           MyList<PersonValidationError> validationErrors,
                           String repositoryError) {
    /**
     * Creates a successful result containing the given person.
     * <p>
     * This method should be used when an operation completes successfully.
     * The returned result contains the created, found, or updated person,
     * and no validation or repository errors.
     *
     * @param person the resulting person
     * @return a successful {@link PersonResult} containing the person
     */
    public static PersonResult success(final Person person) {
        return new PersonResult(true, person,
                new MyArrayList<>(), null);
    }

    /**
     * Creates a result representing validation failure.
     * <p>
     * This method should be used when input validation fails. The returned
     * result contains structured validation errors and no person or repository
     * error.
     *
     * @param validationErrors a list of structured validation errors
     * @return a failed {@link PersonResult} containing validation errors
     */
    public static PersonResult validationFailure(
            final MyList<PersonValidationError> validationErrors) {
        return new PersonResult(false, null,
                validationErrors, null);
    }

    /**
     * Creates a result representing a repository error where a person
     * was not found.
     * <p>
     * This method should be used when an operation fails because no
     * entity exists with the given identifier. The returned result
     * contains an error message and no person or validation errors.
     *
     * @param notFoundMsg the error message describing the failure
     * @return a failed {@link PersonResult} containing the error message
     */
    public static PersonResult notFound(final String notFoundMsg) {
        return new PersonResult(false, null,
                new MyArrayList<>(), notFoundMsg);
    }
}
