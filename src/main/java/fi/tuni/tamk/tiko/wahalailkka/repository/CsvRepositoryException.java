package fi.tuni.tamk.tiko.wahalailkka.repository;

/**
 * Exception thrown when an error occurs in the CSV-based repository.
 * <p>
 * This exception is used to wrap lower-level exceptions (such as
 * {@link java.io.IOException}) and provide a meaningful message related to
 * CSV data persistence operations.
 */
public final class CsvRepositoryException extends RuntimeException {
    /**
     * Constructs a new CsvRepositoryException with the specified detail
     * message.
     *
     * @param message the detail message describing the error
     */
    public CsvRepositoryException(final String message) {
        super(message);
    }

    /**
     * Constructs a new CsvRepositoryException with the specified detail
     * message and cause.
     * <p>
     * The cause is typically an underlying exception such as
     * {@link java.io.IOException}.
     *
     * @param message the detail message describing the error
     * @param cause the underlying cause of the exception
     */
    public CsvRepositoryException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
