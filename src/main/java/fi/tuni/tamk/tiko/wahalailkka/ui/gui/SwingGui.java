package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import fi.tuni.tamk.tiko.wahalailkka.controller.PersonController;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonListResult;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonResult;
import fi.tuni.tamk.tiko.wahalailkka.ui.AppUi;

import javax.swing.*;
import java.awt.*;

public class SwingGui extends JFrame implements AppUi {
    /** Controller used to handle person-related application logic. */
    private final PersonController controller;

    private static final int minWindowWidth = 800;
    private static final int minWindowHeight = 600;

    private PersonTableModel personTableModel;

    private JButton createButton;

    public SwingGui(final PersonController controller) {
        super("Person CRUD App");
        this.controller = controller;

        initializeFrame();
        initializeComponents();
        initializeListeners();
    }

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
        JTable personTable = new JTable(personTableModel);
        JScrollPane scrollPane = new JScrollPane(personTable);

        this.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        createButton = new JButton("Create");

        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        bottomPanel.add(createButton);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initializeListeners() {
        createButton.addActionListener(e -> {
            handleCreate();
        });
    }

    private void handleCreate() {
        PersonFormDialog createDialog = new PersonFormDialog(
                SwingGui.this, "Create", "Create new person",
                "", "", "", dialog -> {
            int age;

            try {
                age = Integer.parseInt(dialog.getAgeText());
            } catch (NumberFormatException e1) {
                // Show error message
                return false;
            }

            PersonResult result = controller.createPerson(
                    dialog.getFirstName(),
                    dialog.getLastName(),
                    age);

            if (result.isSuccess()) {
                refreshPersonList();
                return true;
            } else {
                // Show error message
                return false;
            }
        });

        createDialog.setVisible(true);
    }

    private void refreshPersonList() {
        PersonListResult listResult = controller.findAllPersons();
        personTableModel.setCurrentList(listResult.personList());
    }
}
