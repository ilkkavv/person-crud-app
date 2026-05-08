package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import fi.tuni.tamk.tiko.wahalailkka.controller.PersonController;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonListResult;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonResult;
import fi.tuni.tamk.tiko.wahalailkka.ui.AppUi;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
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

    private PersonTableModel personTableModel;

    private JButton createButton;

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
        createButton.addActionListener(e -> handleCreate());
    }

    private void handleCreate() {
        PersonFormDialog createDialog = new PersonFormDialog(
                SwingGui.this, "Create", "Create new person",
                "", "", "", dialog -> {
            Integer age = parseAge(dialog.getAgeText());

            if (age == null) {
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

    private Integer parseAge(final String ageText) {
        try {
            return Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void refreshPersonList() {
        PersonListResult listResult = controller.findAllPersons();
        personTableModel.setCurrentList(listResult.personList());
    }
}
