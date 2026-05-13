package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import fi.tuni.tamk.tiko.wahalailkka.controller.PersonController;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonListResult;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonOperationResult;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonResult;
import fi.tuni.tamk.tiko.wahalailkka.controller.PersonUpdateResult;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.ui.AppUi;
import fi.tuni.tamk.tiko.wahalailkka.util.PersonSorter;
import fi.tuni.tamk.tiko.wahalailkka.validation.PersonValidationError;
import fi.tuni.tamk.tiko.wahalailkka.validation.ValidationError;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Swing-based graphical user interface for the Person CRUD application.
 * <p>
 * This class is responsible for displaying person data in a table and
 * handling user interactions through GUI components.
 */
public class SwingGui extends JFrame implements AppUi {
    /** Controller used to handle person-related application logic. */
    private final PersonController controller;

    private static final int ID_FIELD_WIDTH = 3;
    private static final int NAME_FIELD_WIDTH = 10;
    private static final int AGE_FIELD_WIDTH = 3;

    private static final int BORDER_MARGIN = 5;

    private static final int BUTTON_HEIGHT = 40;
    private static final int BUTTON_WIDTH = 100;

    private static final int MIN_WINDOW_WIDTH = 800;
    private static final int MIN_WINDOW_HEIGHT = 600;

    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String AGE = "Age";
    private static final String ASCENDING_ORDER = "Ascending Order";
    private static final String DESCENDING_ODER = "Descending Order";

    private static final String HELP_MSG = """
    NAME
        person-crud-app

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

    private JTable personTable;
    private PersonTableModel personTableModel;

    private final JLabel idLabel = new JLabel("ID:");
    private JTextField findByIdField;
    private JButton findByIdButton;

    private final JLabel nameLabel = new JLabel("Name:");
    private JTextField searchByNameField;
    private JButton searchByNameButton;

    private final JLabel ageRangeLabel = new JLabel("Age range:");
    private JTextField ageRangeMinField;
    private final JLabel ageRangeDashLabel = new JLabel("–");
    private JTextField ageRangeMaxField;
    private JButton filterByAgeRangeButton;

    private JButton resetViewButton;

    private final JLabel sortLabel = new JLabel("Sort by:");
    private JComboBox<String> sortByComboBox;
    private JComboBox<String> sortOrderComboBox;
    private JButton sortButton;

    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;

    private JButton helpButton;
    private JButton exitButton;

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
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));
        this.setSize(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        this.add(initializeTopPanel(), BorderLayout.NORTH);
        this.add(initializePersonTable(), BorderLayout.CENTER);
        this.add(initializeBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel initializeTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        topPanel.add(initializeFilterPanel());
        topPanel.add(initializeSortPanel());

        return topPanel;
    }

    private JPanel initializeFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BorderLayout());

        JPanel westPanel = new JPanel(new FlowLayout());
        JPanel eastPanel = new JPanel(new FlowLayout());

        westPanel.add(initializeIdPanel());
        westPanel.add(initializeNamePanel());
        westPanel.add(initializeAgePanel());

        eastPanel.add(initializeShowPanel());

        filterPanel.add(westPanel, BorderLayout.WEST);
        filterPanel.add(eastPanel, BorderLayout.EAST);

        return filterPanel;
    }

    private JPanel initializeIdPanel() {
        JPanel idPanel = new JPanel();
        idPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        findByIdButton = new JButton("Find");
        findByIdField = new JTextField();
        findByIdField.setColumns(ID_FIELD_WIDTH);

        idPanel.add(idLabel);
        idPanel.add(findByIdField);
        idPanel.add(findByIdButton);

        return idPanel;
    }

    private JPanel initializeNamePanel() {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        searchByNameButton = new JButton("Search");
        searchByNameField = new JTextField();
        searchByNameField.setColumns(NAME_FIELD_WIDTH);

        namePanel.add(nameLabel);
        namePanel.add(searchByNameField);
        namePanel.add(searchByNameButton);

        return namePanel;
    }

    private JPanel initializeAgePanel() {
        JPanel agePanel = new JPanel();
        agePanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        filterByAgeRangeButton = new JButton("Filter");
        ageRangeMinField = new JTextField();
        ageRangeMinField.setColumns(AGE_FIELD_WIDTH);
        ageRangeMaxField = new JTextField();
        ageRangeMaxField.setColumns(AGE_FIELD_WIDTH);

        agePanel.add(ageRangeLabel);
        agePanel.add(ageRangeMinField);
        agePanel.add(ageRangeDashLabel);
        agePanel.add(ageRangeMaxField);
        agePanel.add(filterByAgeRangeButton);

        return agePanel;
    }

    private JPanel initializeShowPanel() {
        JPanel showPanel = new JPanel();
        showPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

        resetViewButton = new JButton("Reset view");

        showPanel.add(resetViewButton);

        return showPanel;
    }

    private JPanel initializeSortPanel() {
        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        sortPanel.setBorder(new EmptyBorder(0, BORDER_MARGIN, BORDER_MARGIN,
                BORDER_MARGIN));

        sortByComboBox = new JComboBox<>();
        sortOrderComboBox = new JComboBox<>();
        sortButton = new JButton("Sort");

        MyList<String> sortOptions = new MyArrayList<>();
        sortOptions.add(FIRST_NAME);
        sortOptions.add(LAST_NAME);
        sortOptions.add(AGE);

        for (int i = 0; i < sortOptions.size(); i++) {
            sortByComboBox.addItem(sortOptions.get(i));
        }

        MyList<String> orderOptions = new MyArrayList<>();
        orderOptions.add(ASCENDING_ORDER);
        orderOptions.add(DESCENDING_ODER);

        for (int i = 0; i < orderOptions.size(); i++) {
            sortOrderComboBox.addItem(orderOptions.get(i));
        }

        sortPanel.add(sortLabel);
        sortPanel.add(sortByComboBox);
        sortPanel.add(sortOrderComboBox);
        sortPanel.add(sortButton);

        return sortPanel;
    }

    private JScrollPane initializePersonTable() {
        personTableModel = new PersonTableModel(controller.findAllPersons().
                personList());

        personTable = new JTable(personTableModel);

        return new JScrollPane(personTable);
    }

    private JPanel initializeBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        JPanel westPanel = new JPanel(new FlowLayout());
        JPanel eastPanel = new JPanel(new FlowLayout());

        westPanel.add(initializeCrudPanel());
        eastPanel.add(initializeExitPanel());

        bottomPanel.add(westPanel, BorderLayout.WEST);
        bottomPanel.add(eastPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    private JPanel initializeCrudPanel() {
        JPanel crudPanel = new JPanel();
        crudPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        createButton = new JButton("Create");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        Dimension buttonDimension = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

        createButton.setPreferredSize(buttonDimension);
        updateButton.setPreferredSize(buttonDimension);
        deleteButton.setPreferredSize(buttonDimension);

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        crudPanel.add(createButton);
        crudPanel.add(updateButton);
        crudPanel.add(deleteButton);

        return crudPanel;
    }

    private JPanel initializeExitPanel() {
        JPanel exitPanel = new JPanel();
        exitPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

        helpButton = new JButton("Help");
        exitButton = new JButton("Exit");

        Dimension buttonDimension = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

        helpButton.setPreferredSize(buttonDimension);
        exitButton.setPreferredSize(buttonDimension);

        exitPanel.add(helpButton);
        exitPanel.add(exitButton);

        return exitPanel;
    }

    private void initializeListeners() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleExit();
            }
        });

        findByIdButton.addActionListener(e -> handleFindById());
        searchByNameButton.addActionListener(e -> handleSearchByName());
        filterByAgeRangeButton.addActionListener(e -> handleFilterByAgeRange());
        resetViewButton.addActionListener(e -> handleResetView());

        sortButton.addActionListener(e -> handleSort());

        personTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = personTable.getSelectedRow();
            updateButton.setEnabled(selectedRow > -1);
            deleteButton.setEnabled(selectedRow > -1);
        });

        createButton.addActionListener(e -> handleCreate());
        updateButton.addActionListener(e -> handleUpdate());
        deleteButton.addActionListener(e -> handleDelete());

        helpButton.addActionListener(e -> handleHelp());
        exitButton.addActionListener(e -> handleExit());
    }

    private void handleFindById() {
        String idText = findByIdField.getText();
        int id = parseId(idText);

        if (id == -1) {
            return;
        }

        MyList<Person> personList = new MyArrayList<>();
        PersonResult result = controller.findPersonById(id);

        if (result.isSuccess()) {
            personList.add(result.person());
            personTableModel.setCurrentList(personList);
        } else {
            if (result.validationErrors().isEmpty()) {
                showMessage(this, result.repositoryError(),
                        "Person not found", JOptionPane.WARNING_MESSAGE);
            } else {
                handleValidationErrors(result.validationErrors());
            }
        }
    }

    private void handleSearchByName() {
        String searchInput = searchByNameField.getText();

        MyList<Person> personList;
        PersonListResult result = controller.searchPersonsByName(searchInput);

        if (result.isSuccess()) {
            personList = result.personList();
            personTableModel.setCurrentList(personList);
        } else {
            handleValidationErrors(result.validationErrors());
        }
    }

    private void handleFilterByAgeRange() {
        String minAgeText = ageRangeMinField.getText();
        String maxAgeText = ageRangeMaxField.getText();

        int minAge = parseAge(minAgeText);
        int maxAge = parseAge(maxAgeText);

        if (minAge == -1 || maxAge == -1) {
            showMessage(this,
                    "Please enter valid numbers for minimum and maximum age.",
                    "Invalid input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PersonListResult result = controller.searchPersonsByAge(minAge, maxAge);

        if (result.isSuccess()) {
            personTableModel.setCurrentList(result.personList());
        } else {
            handleValidationErrors(result.validationErrors());
        }
    }

    private void handleSort() {
        if (sortByComboBox.getSelectedItem() == null ||
                sortOrderComboBox.getSelectedItem() == null) {
            return;
        }

        String sortBy = sortByComboBox.getSelectedItem().toString();
        boolean ascending = sortOrderComboBox.getSelectedItem().toString()
                .equals(ASCENDING_ORDER);

        PersonListResult result;
        MyList<Person> currentList = personTableModel.getCurrentList();
        PersonSorter.NameField nameField;

        if (sortBy.equals(AGE)) {
            result = controller.sortPersonsByAge(currentList, ascending);
        } else {
            if (sortBy.equals(FIRST_NAME)) {
                nameField = PersonSorter.NameField.FIRST_NAME;
            } else {
                nameField = PersonSorter.NameField.LAST_NAME;
            }

            result = controller.sortPersonsByName(currentList, nameField,
                    ascending);
        }

        if (result.isSuccess()) {
            personTableModel.setCurrentList(result.personList());
        }
    }

    private void handleValidationErrors(final MyList<ValidationError> errors) {
        showMessage(this, formatValidationErrors(errors), "Invalid input",
                JOptionPane.WARNING_MESSAGE);
    }

    private void handleResetView() {
        refreshPersonList();
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

                showMessage(SwingGui.this, message, "Success",
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

    private void handleHelp() {
        showHelp();
    }

    private void handleExit() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit Person CRUD App?",
                "Exit",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
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

    private int parseId(final String idText) {
        try {
            int id = Integer.parseInt(idText);

            if (id > 0) {
                return id;
            }
        } catch (NumberFormatException _) {
        }

        showMessage(this,
                "Person ID must be a positive integer.",
                "Invalid input",
                JOptionPane.WARNING_MESSAGE);

        return -1;
    }

    private String formatValidationErrors(
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

    private void showHelp() {
        JTextArea helpText = new JTextArea(HELP_MSG);
        helpText.setOpaque(false);
        helpText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(helpText);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane,
                "Help", JOptionPane.INFORMATION_MESSAGE);
    }
}
