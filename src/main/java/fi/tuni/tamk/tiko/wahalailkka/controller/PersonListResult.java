package fi.tuni.tamk.tiko.wahalailkka.controller;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.validation.ValidationError;

/**
 * Represents the result of an operation that returns a list of persons.
 * <p>
 * This result is used for operations such as search, where the operation may
 * either succeed with a list of persons or fail due to validation errors.
 *
 * @param isSuccess true if the operation succeeded, false otherwise
 * @param personList the resulting list of persons
 * @param validationErrors structured validation errors, empty if none
 */
public record PersonListResult(boolean isSuccess, MyList<Person> personList,
                               MyList<ValidationError> validationErrors) {
    /**
     * Creates a successful result containing the given list of persons.
     *
     * @param personList the resulting list of persons
     * @return a successful result containing the person list
     */
    public static PersonListResult success(final MyList<Person> personList) {
        return new PersonListResult(true, personList,
                new MyArrayList<>());
    }

    /**
     * Creates a failed result containing validation errors.
     *
     * @param validationErrors the structured validation errors
     * @return a failed result containing validation errors
     */
    public static PersonListResult failure(
            final MyList<ValidationError> validationErrors) {
        return new PersonListResult(false, new MyArrayList<>(),
                validationErrors);
    }
}
