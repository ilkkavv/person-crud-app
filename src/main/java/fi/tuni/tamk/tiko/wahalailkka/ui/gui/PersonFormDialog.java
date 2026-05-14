package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import fi.tuni.tamk.tiko.wahalailkka.repository.CsvRepositoryException;
import static fi.tuni.tamk.tiko.wahalailkka.ui.gui.GuiExceptionHandler
        .logException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Modal dialog used for creating and updating Person objects.
 * <p>
 * This dialog contains form fields for first name, last name, and age.
 * The dialog delegates form submission handling through a callback interface.
 */
public class PersonFormDialog extends JDialog {
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton confirmButton;

    private final FormSubmitHandler onConfirm;

    private final JLabel firstNameLabel = new JLabel("First Name:");
    private final JLabel lastNameLabel = new JLabel("Last Name:");
    private final JLabel ageLabel = new JLabel("Age:");

    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField ageField;

    private static final int FIELD_WEIGHT = 25;
    private static final int MARGIN = 5;

    /**
     * Constructs a new person form dialog.
     *
     * @param owner the parent frame of this dialog
     * @param confirmButtonText the text displayed on the confirm button
     * @param formTitle the title of the dialog window
     * @param firstName the initial first name value
     * @param lastName the initial last name value
     * @param age the initial age value
     * @param onConfirm callback executed when the confirm button is pressed
     */
    public PersonFormDialog(final JFrame owner, final String confirmButtonText,
                            final String formTitle, final String firstName,
                            final String lastName, final String age,
                            final FormSubmitHandler onConfirm) {
        super(owner, formTitle, true);

        confirmButton = new JButton(confirmButtonText);
        this.onConfirm = onConfirm;

        firstNameField = new JTextField(firstName);
        lastNameField = new JTextField(lastName);
        ageField = new JTextField(age);

        firstNameField.setColumns(FIELD_WEIGHT);
        lastNameField.setColumns(FIELD_WEIGHT);
        ageField.setColumns(FIELD_WEIGHT);

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        initializeComponents();
        initializeListeners();
        pack();
        setLocationRelativeTo(owner);
    }

    /**
     * Returns the first name entered in the form.
     *
     * @return the entered first name
     */
    public String getFirstName() {
        return firstNameField.getText();
    }

    /**
     * Returns the last name entered in the form.
     *
     * @return the entered last name
     */
    public String getLastName() {
        return lastNameField.getText();
    }

    /**
     * Returns the age entered in the form as text.
     *
     * @return the entered age text
     */
    public String getAgeText() {
        return ageField.getText();
    }

    /**
     * Highlights the first name label in red.
     */
    public void highlightFirstNameLabel() {
        firstNameLabel.setForeground(Color.RED);
    }

    /**
     * Highlights the last name label in red.
     */
    public void highlightLastNameLabel() {
        lastNameLabel.setForeground(Color.RED);
    }

    /**
     * Highlights the age label in red.
     */
    public void highlightAgeLabel() {
        ageLabel.setForeground(Color.RED);
    }

    /**
     * Resets all form label colors to black.
     */
    public void resetLabelColors() {
        firstNameLabel.setForeground(Color.BLACK);
        lastNameLabel.setForeground(Color.BLACK);
        ageLabel.setForeground(Color.BLACK);
    }

    private void initializeComponents() {
        createFormPanel();
        createBottomPanel();
    }

    private void createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(MARGIN, MARGIN, MARGIN, MARGIN);
        constraints.anchor = GridBagConstraints.EAST;

        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(firstNameLabel, constraints);

        constraints.gridx = 1;
        formPanel.add(firstNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        formPanel.add(lastNameLabel, constraints);

        constraints.gridx = 1;
        formPanel.add(lastNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        formPanel.add(ageLabel, constraints);

        constraints.gridx = 1;
        formPanel.add(ageField, constraints);

        this.add(formPanel, BorderLayout.CENTER);
    }

    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        bottomPanel.add(cancelButton);
        bottomPanel.add(confirmButton);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initializeListeners() {
        cancelButton.addActionListener(_ -> dispose());

        confirmButton.addActionListener(_ -> {
            try {
                if (onConfirm.handle(PersonFormDialog.this)) {
                    dispose();
                }
            } catch (CsvRepositoryException e) {
                logException(e);
                DialogHelper.showMessage(PersonFormDialog.this, e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
