package fi.tuni.tamk.tiko.wahalailkka.repository;

import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.model.PersonData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemPersonRepositoryTest {
    @Test
    void findAllReturnEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertTrue(memRepo.findAll().isEmpty());
    }

    @Test
    void createReturnCorrectData() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertEquals(new Person(1, "John", "Doe", 35),
                     memRepo.create(new PersonData("John", "Doe", 35)));
    }

    @Test
    void createShouldIncrementId() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertEquals(1, memRepo.create(new PersonData(
                "John", "Doe", 35)).id());
        assertEquals(2, memRepo.create(new PersonData(
                "Jane", "Doe", 32)).id());
        assertEquals(3, memRepo.create(new PersonData(
                "Jack", "Doe", 41)).id());
    }
}
