package fi.tuni.tamk.tiko.wahalailkka.repository;

import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.model.PersonData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemPersonRepositoryTest {
    private static final PersonData JOHN_DATA = new PersonData(
            "John", "Doe", 35);
    private static final PersonData JANE_DATA = new PersonData(
            "Jane", "Doe", 34);
    private  static final PersonData JACK_DATA = new PersonData(
            "Jack", "Doe", 41);

    @Test
    void findAllReturnEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertTrue(memRepo.findAll().isEmpty());
    }

    @Test
    void createReturnCorrectData() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertEquals(new Person(1, "John", "Doe", 35),
                     memRepo.create(JOHN_DATA));
    }

    @Test
    void createShouldIncrementId() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertEquals(1, memRepo.create(JOHN_DATA).id());
        assertEquals(2, memRepo.create(JANE_DATA).id());
        assertEquals(3, memRepo.create(JACK_DATA).id());
    }
}
