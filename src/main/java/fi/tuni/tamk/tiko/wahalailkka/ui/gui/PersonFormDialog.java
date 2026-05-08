package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

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

    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField ageField;

    private static final int gridCols = 2;
    private static final int gridRows = 3;
    private static final int borderMargin = 10;
    private static final int gridMargin = 5;

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

    private void initializeComponents() {
        createFormPanel();
        createBottomPanel();
    }

    private void createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setBorder(new EmptyBorder(borderMargin, borderMargin,
                borderMargin, borderMargin));
        formPanel.setLayout(new GridLayout(gridRows, gridCols, gridMargin,
                gridMargin));
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);
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
        cancelButton.addActionListener(e -> dispose());

        confirmButton.addActionListener(e -> {
            if (onConfirm.handle(PersonFormDialog.this)) {
                dispose();
            }
        });
    }
}
