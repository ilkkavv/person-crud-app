package fi.tuni.tamk.tiko.wahalailkka.validation;

/**
 * Represents a validation error related to a specific search field.
 * <p>
 * This record stores both the search field that failed validation and the
 * corresponding validation error message.
 *
 * @param input the search field associated with the validation error
 * @param message the validation error message
 */
public record SearchValidationError (SearchField input, String message)
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
