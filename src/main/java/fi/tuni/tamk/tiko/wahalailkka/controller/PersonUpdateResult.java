package fi.tuni.tamk.tiko.wahalailkka.controller;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.validation.ValidationError;

/**
 * Represents the result of an update operation in the controller layer.
 * <p>
 * This record encapsulates the outcome of an update, including:
 * <ul>
 *     <li>whether the operation was successful</li>
 *     <li>the updated {@link Person}, if successful</li>
 *     <li>a list of changed fields describing what was modified</li>
 *     <li>a list of structured validation errors, if validation failed</li>
 *     <li>a repository error message, if the operation failed at the data
 *         layer</li>
 * </ul>
 *
 * @param isSuccess {@code true} if the operation succeeded,
 *                  {@code false} otherwise
 * @param person the updated person, or {@code null} if the operation failed
 * @param changedFields a list describing which fields were changed,
 *                      or an empty list if none or if the operation failed
 * @param validationErrors structured validation errors,
 *                         or an empty list if none
 * @param repositoryError an error message related to repository operations,
 *                        or {@code null} if none
 */
public record PersonUpdateResult(boolean isSuccess, Person person,
                           MyList<String> changedFields,
                           MyList<ValidationError> validationErrors,
                           String repositoryError) {
    /**
     * Creates a successful update result containing the updated person
     * and a list of changed fields.
     * <p>
     * This method should be used when an update operation completes
     * successfully. The returned result contains the updated person and
     * information about which fields were modified.
     *
     * @param person the updated person
     * @param changedFields a list describing which fields were changed
     * @return a successful {@link PersonUpdateResult}
     */
    public static PersonUpdateResult success(final Person person,
                                       final MyList<String> changedFields) {
        return new PersonUpdateResult(true, person, changedFields,
                new MyArrayList<>(), null);
    }

    /**
     * Creates a result representing validation failure.
     * <p>
     * This method should be used when input validation fails. The returned
     * result contains structured validation errors and no updated person
     * or repository error.
     *
     * @param validationErrors a list of structured validation errors
     * @return a failed {@link PersonUpdateResult} containing validation errors
     */
    public static PersonUpdateResult validationFailure(
            final MyList<ValidationError> validationErrors) {
        return new PersonUpdateResult(false, null,
                new MyArrayList<>(), validationErrors, null);
    }

    /**
     * Creates a result representing a repository error where a person
     * was not found.
     * <p>
     * This method should be used when an update operation fails because no
     * entity exists with the given identifier. The returned result contains
     * an error message and no updated person or validation errors.
     *
     * @param notFoundMsg the error message describing the failure
     * @return a failed {@link PersonUpdateResult} containing the error message
     */
    public static PersonUpdateResult notFound(final String notFoundMsg) {
        return new PersonUpdateResult(false, null,
                new MyArrayList<>(), new MyArrayList<>(),
                notFoundMsg);
    }
}
