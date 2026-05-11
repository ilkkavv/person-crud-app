package fi.tuni.tamk.tiko.wahalailkka.controller;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.validation.ValidationError;

/**
 * Represents the common result contract for person-related operations.
 * <p>
 * This interface provides access to validation and repository errors for
 * controller operations such as create, update, and delete.
 */
public interface PersonOperationResult {
    /**
     * Returns the validation errors associated with the operation.
     *
     * @return a list of validation errors, or an empty list if none
     */
    MyList<ValidationError> validationErrors();

    /**
     * Returns the repository error message associated with the operation.
     *
     * @return the repository error message, or {@code null} if none
     */
    String repositoryError();
}
