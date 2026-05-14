package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;

/**
 * Panel containing controls for sorting the person table.
 * <p>
 * This panel provides controls for selecting:
 * <ul>
 *     <li>The field used for sorting</li>
 *     <li>The sorting order</li>
 * </ul>
 * and a button for applying the selected sorting options.
 */
public class PersonSortPanel extends JPanel {
    private static final int BORDER_MARGIN = 5;

    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String AGE = "Age";

    private final JComboBox<String> sortByComboBox;
    private final JComboBox<String> sortOrderComboBox;
    private final JButton sortButton;

    private static final String ASCENDING_ORDER = "Ascending Order";
    private static final String DESCENDING_ORDER = "Descending Order";

    /**
     * Constructs a new sort panel with sorting controls initialized.
     */
    public PersonSortPanel() {
        this.setLayout(new FlowLayout(FlowLayout.TRAILING));
        this.setBorder(new EmptyBorder(0, BORDER_MARGIN, BORDER_MARGIN,
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
        orderOptions.add(DESCENDING_ORDER);

        for (int i = 0; i < orderOptions.size(); i++) {
            sortOrderComboBox.addItem(orderOptions.get(i));
        }

        this.add(new JLabel("Sort by:"));
        this.add(sortByComboBox);
        this.add(sortOrderComboBox);
        this.add(sortButton);
    }

    /**
     * Returns the button used for applying sorting.
     *
     * @return the sort button
     */
    public JButton getSortButton() {
        return sortButton;
    }

    /**
     * Returns the combo box used for selecting the sorting field.
     *
     * @return the sort-by combo box
     */
    public JComboBox<String> getSortByComboBox() {
        return sortByComboBox;
    }

    /**
     * Returns the combo box used for selecting the sorting order.
     *
     * @return the sort-order combo box
     */
    public JComboBox<String> getSortOrderComboBox() {
        return sortOrderComboBox;
    }
}
