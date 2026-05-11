package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import fi.tuni.tamk.tiko.wahalailkka.controller.PersonController;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonListResult;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonOperationResult;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonResult;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonUpdateResult;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.ui.AppUi;
import fi.tuni.tamk.tiko.wahalailkka.validation.PersonValidationError;
import fi.tuni.tamk.tiko.wahalailkka.validation.ValidationError;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * Swing-based graphical user interface for the Person CRUD application.
 * <p>
 * This class is responsible for displaying person data in a table and
 * handling user interactions through GUI components.
 */
public class SwingGui extends JFrame implements AppUi {
    /** Controller used to handle person-related application logic. */
    private final PersonController controller;

    private static final int minWindowWidth = 800;
    private static final int minWindowHeight = 600;

    private JTable personTable;
    private PersonTableModel personTableModel;

    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;

    /**
     * Constructs a new Swing GUI with the given controller.
     *
     * @param controller the controller used by this UI
     */
    public SwingGui(final PersonController controller) {
        super("Person CRUD App");
        this.controller = controller;

        initializeFrame();
        initializeComponents();
        initializeListeners();
    }

    /**
     * Starts the graphical user interface.
     */
    @Override
    public void run() {
        this.setVisible(true);
    }

    private void initializeFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(minWindowWidth, minWindowHeight));
        this.setSize(minWindowWidth, minWindowHeight);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        personTableModel = new PersonTableModel(controller.findAllPersons().
                personList());
        personTable = new JTable(personTableModel);
        JScrollPane scrollPane = new JScrollPane(personTable);

        this.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        createButton = new JButton("Create");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        bottomPanel.add(createButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initializeListeners() {
        createButton.addActionListener(e -> handleCreate());
        updateButton.addActionListener(e -> handleUpdate());
        deleteButton.addActionListener(e -> handleDelete());

        personTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = personTable.getSelectedRow();
            updateButton.setEnabled(selectedRow > -1);
            deleteButton.setEnabled(selectedRow > -1);
        });
    }

    private void handleCreate() {
        PersonFormDialog createDialog = new PersonFormDialog(
                SwingGui.this, "Create", "Create new person",
                "", "", "", dialog -> {
            int age = parseAge(dialog.getAgeText());

            PersonResult result = controller.createPerson(
                    dialog.getFirstName(),
                    dialog.getLastName(),
                    age);

            if (result.isSuccess()) {
                Person person = result.person();

                String personData = String.format("ID: %d | %s %s | Age: %d%n",
                        person.id(), person.firstName(), person.lastName(),
                        person.age());

                showMessage(SwingGui.this, "New person created:"
                        + "\n" + personData, "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                refreshPersonList();
                return true;
            } else {
                handleErrors(result, dialog);
                highlightInvalidFields(result, dialog);
                return false;
            }
        });

        createDialog.setVisible(true);
    }

    private void handleUpdate() {
        Person person = personTableModel.getPersonAt(
                personTable.getSelectedRow());

        PersonFormDialog updateDialog = new PersonFormDialog(
                SwingGui.this, "Update", "Update person data",
                person.firstName(), person.lastName(),
                Integer.toString(person.age()), dialog -> {
            int age = parseAge(dialog.getAgeText());

            PersonUpdateResult result = controller.updatePersonById(
                    person.id(),
                    dialog.getFirstName(),
                    dialog.getLastName(),
                    age);

            if (result.isSuccess()) {
                MyList<String> changedFields = result.changedFields();
                String updatedFields = "No fields were changed.";
                String title = "Update canceled";

                if (!changedFields.isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder(
                            "Updated fields:");

                    for (int i = 0; i < result.changedFields().size(); i++) {
                        stringBuilder.append("\n")
                                .append(result.changedFields().get(i));
                    }

                    updatedFields = stringBuilder.toString();
                    title = "Success";
                }

                showMessage(SwingGui.this, updatedFields, title,
                        JOptionPane.INFORMATION_MESSAGE);

                refreshPersonList();
                return true;
            } else {
                handleErrors(result, dialog);
                highlightInvalidFields(result, dialog);
                return false;
            }
        });

        updateDialog.setVisible(true);
    }

    private void handleDelete() {
        Person person = personTableModel.getPersonAt(
                personTable.getSelectedRow());

        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this person?",
                "Confirm deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            PersonOperationResult result = controller.deletePersonById(
                    person.id());

            if (result.isSuccess()) {
                Person deletedPerson = result.person();

                String message = String.format("Person deleted:\nID: %d | %s %s"
                        + " | Age: %d%n", deletedPerson.id(),
                        deletedPerson.firstName(), deletedPerson.lastName(),
                        deletedPerson.age());

                refreshPersonList();

                showMessage(SwingGui.this, message,"Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                handleErrors(result, SwingGui.this);
            }
        }
    }

    /**
     * Displays validation or repository errors from a person operation result.
     * <p>
     * Repository errors are displayed in an error dialog. Validation errors are
     * combined into a single message and displayed in a warning dialog.
     *
     * @param result the result containing validation or repository errors
     * @param parent the parent component for the dialog
     */
    private void handleErrors(final PersonOperationResult result,
                              final Component parent) {
        StringBuilder errors = new StringBuilder();

        if (result.repositoryError() != null) {
            errors.append(result.repositoryError());

            showMessage(parent, errors.toString(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            MyList<ValidationError> validationErrors =
                    result.validationErrors();

            for (int i = 0; i < validationErrors.size(); i++) {
                ValidationError error = validationErrors.get(i);
                errors.append(error.getMessage());

                if (i != validationErrors.size() - 1) {
                    errors.append("\n");
                }
            }

            showMessage(parent, errors.toString(), "Invalid input",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Highlights invalid form fields based on validation errors.
     * <p>
     * All label colors are first reset to black. Labels associated with
     * validation errors are then highlighted in red.
     *
     * @param result the result containing validation errors
     * @param dialog the dialog whose labels are updated
     */
    private void highlightInvalidFields(final PersonOperationResult result,
                                        final PersonFormDialog dialog) {
        dialog.resetLabelColors();

        MyList<ValidationError> validationErrors = result.validationErrors();

        for (int i = 0; i < validationErrors.size(); i++) {
            ValidationError error = validationErrors.get(i);

            if (error instanceof PersonValidationError personError) {
                switch (personError.field()) {
                    case FIRST_NAME:
                        dialog.highlightFirstNameLabel();
                        break;
                    case LAST_NAME:
                        dialog.highlightLastNameLabel();
                        break;
                    case AGE:
                        dialog.highlightAgeLabel();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Parses age text into an integer value.
     * <p>
     * Returns -1 if parsing fails so that validation can be handled by the
     * controller layer.
     *
     * @param ageText the age text to parse
     * @return the parsed age, or -1 if parsing fails
     */
    private int parseAge(final String ageText) {
        try {
            return Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Refreshes the table model with the latest person data from the
     * controller.
     */
    private void refreshPersonList() {
        PersonListResult listResult = controller.findAllPersons();
        personTableModel.setCurrentList(listResult.personList());
    }

    /**
     * Displays a message dialog.
     *
     * @param parent the parent component of the dialog
     * @param message the message to display
     * @param title the dialog title
     * @param type the message type defined by {@link JOptionPane}
     */
    private void showMessage(final Component parent, final String message,
                             final String title, final int type) {
        JOptionPane.showMessageDialog(parent, message, title, type);
    }
}
