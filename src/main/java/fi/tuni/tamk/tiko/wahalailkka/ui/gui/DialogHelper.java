package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import fi.tuni.tamk.tiko.wahalailkka.controller.PersonOperationResult;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.validation.ValidationError;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Utility class for displaying dialogs in the graphical user interface.
 * <p>
 * This class provides helper methods for displaying:
 * <ul>
 *     <li>Validation error dialogs</li>
 *     <li>Operation error dialogs</li>
 *     <li>General message dialogs</li>
 *     <li>The application help dialog</li>
 * </ul>
 */
public final class DialogHelper {
    private static final int SCROLL_PANE_WIDTH = 400;
    private static final int SCROLL_PANE_HEIGHT = 300;

    private static final String INVALID_INPUT = "Invalid input";

    private static final String HELP_MSG = """
    NAME
        person-crud-app
    
    VERSION
        1.0.0

    DESCRIPTION
        person-crud-app is a desktop application for managing
        a collection of persons.

        Each person contains:
            - ID
            - First name
            - Last name
            - Age

        IDs are assigned automatically by the application.

    FEATURES
        Create
            Adds a new person to the collection.

        Update
            Updates the selected person's information.

        Delete
            Deletes the selected person after confirmation.

        Find by ID
            Displays a person with the given ID.

        Search by Name
            Searches persons by first and last name.

        Filter by Age Range
            Displays persons whose age is within the given range.

        Sort
            Sorts the current list by:
                - First name
                - Last name
                - Age

            Sorting can be ascending or descending.

        Reset View
            Restores the full person list and clears
            filtering and sorting results.

    USAGE
        Select a row from the table before using
        Update or Delete operations.
    """;

    /**
     * Prevents instantiation of this utility class.
     */
    private DialogHelper() { }

    /**
     * Displays validation or repository errors from a person operation result.
     * <p>
     * Repository errors are displayed in an error dialog. Validation errors are
     * combined into a single message and displayed in a warning dialog.
     *
     * @param result the result containing validation or repository errors
     * @param parent the parent component for the dialog
     */
    public static void showOperationErrorDialog(
            final PersonOperationResult result, final Component parent) {
        StringBuilder errors = new StringBuilder();

        if (result.repositoryError() != null) {
            errors.append(result.repositoryError());

            showMessage(parent, errors.toString(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            showValidationErrors(result.validationErrors(), parent);
        }
    }

    /**
     * Displays validation errors in a warning dialog.
     *
     * @param errors the validation errors to display
     * @param parent the parent component for the dialog
     */
    public static void showValidationErrors(
            final MyList<ValidationError> errors, final Component parent) {
        showMessage(parent, formatValidationErrors(errors), INVALID_INPUT,
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Displays a message dialog.
     *
     * @param parent the parent component of the dialog
     * @param message the message to display
     * @param title the dialog title
     * @param type the message type defined by {@link JOptionPane}
     */
    public static void showMessage(final Component parent, final String message,
                                   final String title, final int type) {
        JOptionPane.showMessageDialog(parent, message, title, type);
    }

    /**
     * Displays the application help dialog.
     *
     * @param parent the parent component for the dialog
     */
    public static void showHelp(final Component parent) {
        JTextArea helpText = new JTextArea(HELP_MSG);
        helpText.setOpaque(false);
        helpText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH,
                                                  SCROLL_PANE_HEIGHT));
        JOptionPane.showMessageDialog(parent, scrollPane,
                "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Formats validation errors into a single multiline string.
     *
     * @param errors the validation errors to format
     * @return the formatted validation error message
     */
    private static String formatValidationErrors(
            final MyList<ValidationError> errors) {
        StringBuilder errorList = new StringBuilder();

        for (int i = 0; i < errors.size(); i++) {
            errorList.append(errors.get(i).getMessage());
            if (i != errors.size() - 1) {
                errorList.append("\n");
            }
        }

        return errorList.toString();
    }
}
