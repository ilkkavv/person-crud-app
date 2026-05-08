package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

/**
 * Functional callback interface used for handling form submission events in
 * PersonFormDialog.
 * <p>
 * Implementations define what happens when the user confirms the form.
 * The return value determines whether the dialog should be closed.
 */
@FunctionalInterface
public interface FormSubmitHandler {
    /**
     * Handles the form submission event.
     *
     * @param dialog the dialog containing the submitted form data
     * @return true if the dialog should be closed, false otherwise
     */
    boolean handle(PersonFormDialog dialog);
}
