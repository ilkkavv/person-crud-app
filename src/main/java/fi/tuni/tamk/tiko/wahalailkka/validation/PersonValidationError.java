package fi.tuni.tamk.tiko.wahalailkka.validation;

/**
 * Represents a validation error related to a specific person field.
 * <p>
 * This record stores both the field that failed validation and the
 * corresponding validation error message.
 *
 * @param field the person field associated with the validation error
 * @param message the validation error message
 */
public record PersonValidationError(PersonField field, String message)
        implements ValidationError {
    /**
     * Returns the validation error message.
     *
     * @return the validation error message
     */
    @Override
    public String getMessage() {
        return message();
    }
}
