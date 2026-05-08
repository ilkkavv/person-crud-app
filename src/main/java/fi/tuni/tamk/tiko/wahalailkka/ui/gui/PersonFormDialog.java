package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonFormDialog extends JDialog {
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton confirmButton;

    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField ageField;

    private boolean confirmed;

    public PersonFormDialog(final JFrame owner, final String confirmButtonText,
                            final String formTitle, final String firstName,
                            final String lastName, final String age) {
        super(owner, formTitle, true);

        confirmButton = new JButton(confirmButtonText);

        firstNameField = new JTextField(firstName);
        lastNameField = new JTextField(lastName);
        ageField = new JTextField(age);

        this.setLayout(new BorderLayout());
        initializeComponents();
        initializeListeners();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initializeComponents() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2));
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
                confirmed = true;
                dispose();
            }
        });
    }
}
