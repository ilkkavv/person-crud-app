package fi.tuni.tamk.tiko.wahalailkka.validation;

/**
 * Represents a generic validation error.
 * <p>
 * Implementations provide an error message describing why validation failed.
 */
public interface ValidationError {
    /**
     * Returns the validation error message.
     *
     * @return the validation error message
     */
    String getMessage();
}
