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
    private PersonSorter() { }

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

            Person temp = personList.get(min);
            personList.set(min, personList.get(i));
            personList.set(i, temp);
        }
    }
}
