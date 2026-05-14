package fi.tuni.tamk.tiko.wahalailkka.ui.gui;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;

import javax.swing.table.AbstractTableModel;

/**
 * Table model for displaying Person objects in a JTable.
 * <p>
 * This class adapts a MyList of Person objects into a table format that can
 * be used by Swing components. The table contains four columns:
 * ID, first name, last name, and age.
 */
public final class PersonTableModel extends AbstractTableModel {
    private MyList<Person> currentList;
    private final MyList<String> columnNames = new MyArrayList<>();

    /**
     * Constructs a new table model using the given person list.
     *
     * @param personList the list of persons displayed in the table
     */
    public PersonTableModel(final MyList<Person> personList) {
        columnNames.add("ID");
        columnNames.add("First Name");
        columnNames.add("Last Name");
        columnNames.add("Age");

        currentList = personList;
    }

    /**
     * Replaces the current person list and refreshes the table view.
     *
     * @param newPersonList the new list of persons
     */
    public void setCurrentList(final MyList<Person> newPersonList) {
        currentList = newPersonList;
        fireTableDataChanged();
    }

    /**
     * Returns the list currently displayed in the table model.
     *
     * @return the current list of persons shown in the table
     */
    public MyList<Person> getCurrentList() {
        return currentList;
    }

    /**
     * Returns the number of rows in the table.
     *
     * @return the number of persons in the current list
     */
    @Override
    public int getRowCount() {
        return currentList.size();
    }

    /**
     * Returns the number of columns in the table.
     *
     * @return the number of table columns
     */
    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * Returns the value at the specified row and column.
     *
     * @param rowIndex the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value stored at the specified table cell
     * @throws IndexOutOfBoundsException if the column index is invalid
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex < 0 || columnIndex >= columnNames.size()) {
            throw new IndexOutOfBoundsException("Invalid column index: "
                    + columnIndex);
        }

        Person person = currentList.get(rowIndex);

        final int ageIndex = 3;

        return switch (columnIndex) {
            case 0 -> person.id();
            case 1 -> person.firstName();
            case 2 -> person.lastName();
            case ageIndex -> person.age();
            default -> null;
        };
    }

    /**
     * Returns the name of the specified column.
     *
     * @param columnIndex the index of the column
     * @return the name of the specified column
     * @throws IndexOutOfBoundsException if the column index is invalid
     */
    @Override
    public String getColumnName(final int columnIndex) {
        if (columnIndex < 0 || columnIndex >= columnNames.size()) {
            throw new IndexOutOfBoundsException("Invalid column index: "
                    + columnIndex);
        }

        return columnNames.get(columnIndex);
    }

    /**
     * Returns the person at the specified row index.
     *
     * @param rowIndex the index of the row
     * @return the person at the given row index
     */
    public Person getPersonAt(final int rowIndex) {
        return currentList.get(rowIndex);
    }
}
