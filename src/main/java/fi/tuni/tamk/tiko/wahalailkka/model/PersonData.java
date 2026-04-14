package fi.tuni.tamk.tiko.wahalailkka.model;

/**
 * Represents input data for creating or updating a {@link Person}.
 * <p>
 * This record does not include an ID, as it is assigned by the repository.
 *
 * @param firstName the first name of the person
 * @param lastName the last name of the person
 * @param age the age of the person
 */
public record PersonData(String firstName, String lastName, int age) { }
