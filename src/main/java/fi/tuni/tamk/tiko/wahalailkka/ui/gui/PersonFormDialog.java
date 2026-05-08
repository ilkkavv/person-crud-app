package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonFormDialog extends JDialog {
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton confirmButton;

    private final FormSubmitHandler onConfirm;

    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField ageField;

    private final int gridCols = 2;
    private final int gridRows = 3;
    private final int borderMargin = 10;
    private final int gridMargin = 5;

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

    public String getFirstName() {
        return firstNameField.getText();
    }

    public String getLastName() {
        return lastNameField.getText();
    }

    public String getAgeText() {
        return ageField.getText();
    }

    private void initializeComponents() {
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

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        bottomPanel.add(cancelButton);
        bottomPanel.add(confirmButton);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initializeListeners() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onConfirm.handle(PersonFormDialog.this)) {
                    dispose();
                }
            }
        });
    }
}
