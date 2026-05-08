package fi.tuni.tamk.tiko.wahalailkka.validation;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.PersonData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fi.tuni.tamk.tiko.wahalailkka.validation.PersonField.AGE;
import static fi.tuni.tamk.tiko.wahalailkka.validation.PersonField.FIRST_NAME;
import static fi.tuni.tamk.tiko.wahalailkka.validation.PersonField.ID;
import static fi.tuni.tamk.tiko.wahalailkka.validation.PersonField.LAST_NAME;
import static fi.tuni.tamk.tiko.wahalailkka.validation.SearchField.MAX_AGE;
import static fi.tuni.tamk.tiko.wahalailkka.validation.SearchField.MIN_AGE;

public final class PersonValidator {
    private static final String NAME_PATTERN = "^[A-ZÅÄÖ][a-zåäö]+"
            + "(-[A-ZÅÄÖ][a-zåäö]+)*$";
    private static final Pattern NAME_REGEX = Pattern.compile(NAME_PATTERN);

    private static final int MAX_PERSON_AGE = 150;

    private PersonValidator() { }

    /**
     * Validates all fields of the given {@link PersonData}.
     * <p>
     * This method checks the validity of first name, last name, and age.
     * All validation errors are collected into a list.
     *
     * @param personData the data to validate
     * @return a {@link MyList} containing {@link PersonValidationError}
     *         objects; the list is empty if the data is valid
     */
    public static MyList<PersonValidationError> validatePersonData(
            final PersonData personData) {
        MyList<PersonValidationError> validationErrors = new MyArrayList<>();

        String firstName = personData.firstName();
        String lastName = personData.lastName();
        int age = personData.age();

        validateFirstName(firstName, validationErrors);
        validateLastName(lastName, validationErrors);
        validateAge(age, validationErrors);

        return validationErrors;
    }

    /**
     * Validates the given person ID.
     * <p>
     * The ID must be a positive integer (greater than 0).
     *
     * @param id the ID to validate
     * @return a {@link MyList} containing structured validation errors;
     *         the list is empty if the ID is valid
     */
    public static MyList<PersonValidationError> validatePersonId(final int id) {
        MyList<PersonValidationError> validationErrors = new MyArrayList<>();

        if (id < 1) {
            validationErrors.add(new PersonValidationError(ID,
                    "Person ID must be a positive integer."));
        }

        return validationErrors;
    }

    /**
     * Validates that the given age range is valid.
     * <p>
     * The following rules are checked:
     * <ul>
     *     <li>minimum value must be at least 0</li>
     *     <li>maximum value must not exceed the allowed maximum</li>
     *     <li>maximum value must be greater than or equal to minimum</li>
     * </ul>
     *
     * @param min the minimum age value
     * @param max the maximum age value
     * @return a {@link MyList} containing {@link SearchValidationError}
     *         objects; the list is empty if the range is valid
     */
    public static MyList<SearchValidationError> validateAgeRange(final int min,
                                                                 final int max)
    {
        MyList<SearchValidationError> validationErrors = new MyArrayList<>();

        if (min < 0) {
            validationErrors.add(new SearchValidationError(MIN_AGE,
                    "Minimum value must be at least 0."));
        }

        if (max > MAX_PERSON_AGE) {
            validationErrors.add(new SearchValidationError(MAX_AGE,
                    "Maximum value must not exceed " + MAX_PERSON_AGE
                    + "."));
        }

        if (max < min) {
            validationErrors.add(new SearchValidationError(MAX_AGE,
                    "Maximum value must be at least the minimum"
                    + " value."));
        }

        return validationErrors;
    }

    private static void validateFirstName(final String firstName,
                                   final MyList<PersonValidationError>
                                           errorList) {
        final int maxLength = 50;

        if (firstName.isEmpty() || firstName.length() > maxLength) {
            errorList.add(new PersonValidationError(FIRST_NAME,
                    "First name must be 1 - 50 characters long."));
        } else {
            Matcher matcher = NAME_REGEX.matcher(firstName);
            if (!matcher.matches()) {
                errorList.add(new PersonValidationError(FIRST_NAME,
                        "The first letter of the first name must be "
                        + "uppercase and it can be followed by one or more "
                        + "lowercase letters. One hyphen is allowed."));
            }
        }
    }

    private static void validateLastName(final String lastName,
                                         final MyList<PersonValidationError>
                                                 errorList) {
        final int maxLength = 50;

        if (lastName.isEmpty() || lastName.length() > maxLength) {
            errorList.add(new PersonValidationError(LAST_NAME,
                    "Last name must be 1 - 50 characters long."));
        } else {
            Matcher matcher = NAME_REGEX.matcher(lastName);
            if (!matcher.matches()) {
                errorList.add(new PersonValidationError(LAST_NAME,
                        "The first letter of the last name must be "
                        + "uppercase and it can be followed by one or more "
                        + "lowercase letters. One hyphen is allowed."));
            }
        }
    }

    private static void validateAge(final int age,
                                    final MyList<PersonValidationError>
                                            errorList) {
        if (age < 0 || age > MAX_PERSON_AGE) {
            errorList.add(new PersonValidationError(AGE,
                    "Age must be between 0 and 150."));
        }
    }
}
