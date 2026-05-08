package fi.tuni.tamk.tiko.wahalailkka.validation;

/**
 * Represents a validation error related to a specific person form field.
 *
 * @param field the field associated with the validation error
 * @param message the validation error message
 */
public record PersonValidationError(PersonField field, String message)
        implements ValidationError {
}
