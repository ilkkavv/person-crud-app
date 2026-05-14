package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;

/**
 * Panel containing filtering and search controls for the person table.
 * <p>
 * This panel provides controls for:
 * <ul>
 *     <li>Finding a person by ID</li>
 *     <li>Searching persons by name</li>
 *     <li>Filtering persons by age range</li>
 *     <li>Resetting the current table view</li>
 * </ul>
 */
public class PersonFilterPanel extends JPanel {
    private static final int ID_FIELD_WIDTH = 3;
    private static final int NAME_FIELD_WIDTH = 10;
    private static final int AGE_FIELD_WIDTH = 3;

    private JTextField findByIdField;
    private JButton findByIdButton;

    private JTextField searchByNameField;
    private JButton searchByNameButton;

    private JTextField ageRangeMinField;
    private JTextField ageRangeMaxField;
    private JButton filterByAgeRangeButton;

    private JButton resetViewButton;

    /**
     * Constructs a new filter panel with all filtering controls initialized.
     */
    public PersonFilterPanel() {
        this.setLayout(new BorderLayout());

        JPanel westPanel = new JPanel(new FlowLayout());
        JPanel eastPanel = new JPanel(new FlowLayout());

        westPanel.add(initializeIdPanel());
        westPanel.add(initializeNamePanel());
        westPanel.add(initializeAgePanel());

        eastPanel.add(initializeResetPanel());

        this.add(westPanel, BorderLayout.WEST);
        this.add(eastPanel, BorderLayout.EAST);
    }

    /**
     * Returns the button used for finding a person by ID.
     *
     * @return the find-by-ID button
     */
    public JButton getFindByIdButton() {
        return findByIdButton;
    }

    /**
     * Returns the button used for searching persons by name.
     *
     * @return the search-by-name button
     */
    public JButton getSearchByNameButton() {
        return searchByNameButton;
    }

    /**
     * Returns the button used for filtering persons by age range.
     *
     * @return the age range filter button
     */
    public JButton getFilterByAgeRangeButton() {
        return filterByAgeRangeButton;
    }

    /**
     * Returns the button used for resetting the current table view.
     *
     * @return the reset view button
     */
    public JButton getResetViewButton() {
        return resetViewButton;
    }

    /**
     * Returns the text field used for person ID input.
     *
     * @return the find-by-ID text field
     */
    public JTextField getFindByIdField() {
        return findByIdField;
    }

    /**
     * Returns the text field used for name search input.
     *
     * @return the search-by-name text field
     */
    public JTextField getSearchByNameField() {
        return searchByNameField;
    }

    /**
     * Returns the text field used for minimum age input.
     *
     * @return the minimum age text field
     */
    public JTextField getAgeRangeMinField() {
        return ageRangeMinField;
    }

    /**
     * Returns the text field used for maximum age input.
     *
     * @return the maximum age text field
     */
    public JTextField getAgeRangeMaxField() {
        return ageRangeMaxField;
    }

    private JPanel initializeIdPanel() {
        JPanel idPanel = new JPanel();
        idPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        findByIdButton = new JButton("Find");
        findByIdField = new JTextField();
        findByIdField.setColumns(ID_FIELD_WIDTH);

        idPanel.add(new JLabel("ID:"));
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

        namePanel.add(new JLabel("Name:"));
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

        agePanel.add(new JLabel("Age range:"));
        agePanel.add(ageRangeMinField);
        agePanel.add(new JLabel("–"));
        agePanel.add(ageRangeMaxField);
        agePanel.add(filterByAgeRangeButton);

        return agePanel;
    }

    private JPanel initializeResetPanel() {
        JPanel showPanel = new JPanel();
        showPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

        resetViewButton = new JButton("Reset view");

        showPanel.add(resetViewButton);

        return showPanel;
    }
}
