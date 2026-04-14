package fi.tuni.tamk.tiko.wahalailkka.model;

/**
 * Represents a person entity stored in the system.
 *
 * @param id the unique identifier of the person
 * @param firstName the first name of the person
 * @param lastName the last name of the person
 * @param age the age of the person
 */
public record Person(int id, String firstName, String lastName, int age) { }
