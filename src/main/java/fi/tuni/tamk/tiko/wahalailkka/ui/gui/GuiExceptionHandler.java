package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import fi.tuni.tamk.tiko.wahalailkka.repository.CsvRepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JOptionPane;
import java.awt.Component;

/**
 * Utility class for handling exceptions in Swing GUI actions.
 * <p>
 * This class provides a centralized way to execute GUI actions safely
 * and display user-friendly error dialogs when exceptions occur.
 * <p>
 * Repository-related exceptions are shown directly to the user, while
 * unexpected exceptions are logged and displayed as a generic error
 * message.
 */
public final class GuiExceptionHandler {
    private static final String ERROR = "Error";
    private static final String UNEXPECTED_ERROR = "Unexpected error.";

    private static final Logger LOGGER = LogManager.getLogger(
            GuiExceptionHandler.class);

    private GuiExceptionHandler() { }

    /**
     * Executes the given GUI action safely.
     * <p>
     * If a {@link CsvRepositoryException} occurs, its message is shown
     * in an error dialog. Any other unexpected exception is logged and
     * displayed as a generic error dialog.
     *
     * @param parent the parent component for error dialogs
     * @param action the GUI action to execute
     */
    public static void runSafely(final Component parent,
                                 final Runnable action) {
        try {
            action.run();
        } catch (CsvRepositoryException e) {
            DialogHelper.showMessage(parent, e.getMessage(), ERROR,
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            LOGGER.error(UNEXPECTED_ERROR, e);
            DialogHelper.showMessage(parent, UNEXPECTED_ERROR, ERROR,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Logs the given exception using the application logger.
     *
     * @param e the exception to log
     */
    public static void logException(final Exception e) {
        LOGGER.error(e.getMessage(), e);
    }
}
