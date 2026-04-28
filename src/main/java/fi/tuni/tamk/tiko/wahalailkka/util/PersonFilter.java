package fi.tuni.tamk.tiko.wahalailkka.util;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;

/**
 * Utility class for filtering {@link Person} objects based on different
 * criteria.
 * <p>
 * This class provides reusable filtering logic that can be used by different
 * repository implementations. All methods are stateless and operate only on
 * the given input data.
 */
public class PersonFilter {
    private PersonFilter() { }

    /**
     * Filters the given list of persons by matching their first or last name
     * against the provided search input.
     * <p>
     * The search is performed as a case-insensitive substring match.
     * <p>
     * If the given input is {@code null} or empty after trimming, an empty
     * list is returned.
     *
     * @param personList the list of persons to filter
     * @param searchInput the text used to match against first and last names
     * @return a list of persons whose first or last name contains the given
     *         input; an empty list if no matches are found or input is invalid
     */
    public static MyList<Person> filterByName(final MyList<Person> personList,
                                       final String searchInput) {
        MyList<Person> matches = new MyArrayList<>();

        if (searchInput == null || searchInput.trim().isEmpty()) {
            return new MyArrayList<>();
        }

        String searchInputLower = searchInput.trim().toLowerCase();

        for (int i = 0; i < personList.size(); i++) {
            Person person = personList.get(i);
            if (person.firstName().toLowerCase().contains(searchInputLower)
                    || person.lastName().toLowerCase()
                    .contains(searchInputLower)) {
                matches.add(person);
            }
        }

        return matches;
    }

    /**
     * Filters the given list of persons by age range.
     * <p>
     * Both minimum and maximum values are inclusive. This method assumes that
     * the provided range has been validated by the caller.
     *
     * @param personList the list of persons to filter
     * @param min the minimum age (inclusive)
     * @param max the maximum age (inclusive)
     * @return a list of persons whose age is within the given range;
     *         an empty list if no matches are found
     */
    public static MyList<Person> filterByAge(final MyList<Person> personList,
                                             final int min, final int max) {
        MyList<Person> matches = new MyArrayList<>();

        for (int i = 0; i < personList.size(); i++) {
            Person person = personList.get(i);
            if (person.age() >= min && person.age() <= max) {
                matches.add(person);
            }
        }

        return matches;
    }
}
