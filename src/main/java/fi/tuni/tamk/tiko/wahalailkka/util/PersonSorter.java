package fi.tuni.tamk.tiko.wahalailkka.util;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;

/**
 * Utility class for sorting {@link Person} objects.
 * <p>
 * This class provides static methods for sorting lists of persons based on
 * different criteria. All methods operate directly on the given list and
 * modify its order in-place.
 */
public final class PersonSorter {
    /**
     * Defines which name field is used for name-based sorting.
     */
    public enum NameField {
        /** Sort by person's first name. **/
        FIRST_NAME,
        /** Sort by person's last name. **/
        LAST_NAME
    }

    private PersonSorter() { }

    /**
     * Sorts the given list of persons by first or last name.
     * <p>
     * The sorting is performed using the selection sort algorithm and modifies
     * the given list in-place. Name comparison is case-insensitive.
     *
     * @param personList the list of persons to sort
     * @param nameField the name field used for sorting
     * @param ascendingOrder {@code true} for ascending order,
     *                       {@code false} for descending order
     */
    public static void sortByName(final MyList<Person> personList,
                                  final NameField nameField,
                                  final boolean ascendingOrder) {
        int size = personList.size();

        for (int i = 0; i < size; i++) {
            int min = i;

            for (int j = i + 1; j < size; j++) {
                int comparisonResult = getName(personList.get(j), nameField)
                        .compareTo(getName(personList.get(min),
                                nameField));

                if (ascendingOrder) {
                    if (comparisonResult < 0) {
                        min = j;
                    }
                } else {
                    if (comparisonResult > 0) {
                        min = j;
                    }
                }
            }

            if (min != i) {
                Person temp = personList.get(min);
                personList.set(min, personList.get(i));
                personList.set(i, temp);
            }
        }
    }

    /**
     * Sorts the given list of persons by age.
     * <p>
     * The sorting is performed using the selection sort algorithm and modifies
     * the given list in-place.
     * <p>
     * The order of sorting can be controlled with the {@code ascendingOrder}
     * parameter.
     *
     * @param personList the list of persons to sort
     * @param ascendingOrder {@code true} for ascending order (youngest first),
     *                       {@code false} for descending order (oldest first)
     */
    public static void sortByAge(final MyList<Person> personList,
                                 final boolean ascendingOrder) {
        int size = personList.size();

        for (int i = 0; i < size; i++) {
            int min = i;

            for (int j = i + 1; j < size; j++) {
                if (ascendingOrder) {
                    if (personList.get(j).age() < personList.get(min).age()) {
                        min = j;
                    }
                } else {
                    if (personList.get(j).age() > personList.get(min).age()) {
                        min = j;
                    }
                }
            }

            if (min != i) {
                Person temp = personList.get(min);
                personList.set(min, personList.get(i));
                personList.set(i, temp);
            }
        }
    }

    private static String getName(final Person person,
                                  final NameField nameField) {
        if (nameField == NameField.FIRST_NAME) {
            return person.firstName().toLowerCase();
        } else {
            return person.lastName().toLowerCase();
        }
    }
}
