package fi.tuni.tamk.tiko.wahalailkka.validation;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyArrayList;
import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.PersonData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonValidator {
    private static final String NAME_PATTERN = "^[A-ZÅÄÖ][a-zåäö]+"
            + "(-[A-ZÅÄÖ][a-zåäö]+)*$";
    private static final Pattern NAME_REGEX = Pattern.compile(NAME_PATTERN);

    private PersonValidator() { }

    /**
     * Validates all fields of the given {@link PersonData}.
     * <p>
     * This method checks the validity of first name, last name, and age.
     * All validation errors are collected into a list.
     *
     * @param personData the data to validate
     * @return a {@link MyList} containing validation error messages;
     *         the list is empty if the data is valid
     */
    public static MyList<String> validatePersonData(
            final PersonData personData) {
        MyList<String> validationErrors = new MyArrayList<>();

        String firstName = personData.firstName();
        String lastName = personData.lastName();
        int age = personData.age();

        validateFirstName(firstName, validationErrors);
        validateLastName(lastName, validationErrors);
        validateAge(age, validationErrors);

        return validationErrors;
    }

    private static void validateFirstName(final String firstName,
                                   final MyList<String> errorList) {
        int maxLength = 50;

        if (firstName.isEmpty() || firstName.length() > maxLength) {
            errorList.add("First name must be 1 - 50 characters long.");
        } else {
            Matcher matcher = NAME_REGEX.matcher(firstName);
            if (!matcher.matches()) {
                errorList.add("The first letter of the first name must be "
                        + "uppercase and it can be followed by one or more "
                        + "lowercase letters. One hyphen is allowed.");
            }
        }
    }

    private static void validateLastName(final String lastName,
                                         final MyList<String> errorList) {
        int maxLength = 50;

        if (lastName.isEmpty() || lastName.length() > maxLength) {
            errorList.add("Last name must be 1 - 50 characters long.");
        } else {
            Matcher matcher = NAME_REGEX.matcher(lastName);
            if (!matcher.matches()) {
                errorList.add("The first letter of the Last name must be "
                        + "uppercase and it can be followed by one or more "
                        + "lowercase letters. One hyphen is allowed.");
            }
        }
    }

    private static void validateAge(final int age,
                                    final MyList<String> errorList) {
        int maxAge = 150;

        if (age < 0 || age > maxAge) {
            errorList.add("Age must be between 0 and 150.");
        }
    }
}
