package fi.tuni.tamk.tiko.wahalailkka.validation;

/**
 * Represents a validation error related to a specific search field.
 *
 * @param input the search field associated with the validation error
 * @param message the validation error message
 */
public record SearchValidationError(SearchField input, String message) {
}
