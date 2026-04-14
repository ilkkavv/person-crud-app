package fi.tuni.tamk.tiko.wahalailkka.controller;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;

/**
 * Represents the result of a controller operation.
 * <p>
 * This record encapsulates the outcome of an operation, including:
 * <ul>
 *     <li>whether the operation was successful</li>
 *     <li>the resulting {@link Person}, if successful</li>
 *     <li>a list of validation errors, if validation failed</li>
 *     <li>a repository error message, if the operation failed at the data layer
 *     </li>
 * </ul>
 *
 * @param isSuccess true if the operation succeeded, false otherwise
 * @param person the resulting person, or null if the operation failed
 * @param validationErrors validation error messages, or an empty list if none
 * @param repositoryError an error message related to repository operations,
 *                        or null if none
 */
public record PersonResult(boolean isSuccess, Person person,
                           MyList<String> validationErrors,
                           String repositoryError) {
}
