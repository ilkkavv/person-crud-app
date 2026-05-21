package fi.tuni.tamk.tiko.wahalailkka.repository;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.model.PersonData;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class MemPersonRepositoryTest {
    private static final Person JOHN = new Person(
            1, "John", "Doe", 35);
    private static final Person JANE = new Person(
            2, "Jane", "Doe", 34);
    private static final Person JACK = new Person(
            3, "Jack", "Doe", 41);

    private static final PersonData JOHN_DATA = new PersonData(
            "John", "Doe", 35);
    private static final PersonData JANE_DATA = new PersonData(
            "Jane", "Doe", 34);
    private static final PersonData JACK_DATA = new PersonData(
            "Jack", "Doe", 41);

    @Test
    void findAllReturnsEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertTrue(memRepo.findAll().isEmpty());
    }

    @Test
    void createReturnsCorrectData() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertEquals(JOHN, memRepo.create(JOHN_DATA));
    }

    @Test
    void createShouldIncrementId() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertEquals(1, memRepo.create(JOHN_DATA).id());
        assertEquals(2, memRepo.create(JANE_DATA).id());
        assertEquals(3, memRepo.create(JACK_DATA).id());
    }

    @Test
    void findAllReturnsAll() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        memRepo.create(JANE_DATA);
        memRepo.create(JACK_DATA);

        MyList<Person> personList = memRepo.findAll();

        assertEquals(3, personList.size());
        assertEquals(JOHN, personList.get(0));
        assertEquals(JANE, personList.get(1));
        assertEquals(JACK, personList.get(2));
    }

    @Test
    void findByIdReturnsEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        Optional<Person> person = memRepo.findById(1);

        assertTrue(person.isEmpty());
    }

    @Test
    void findByIdReturnsPerson() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        Optional<Person> person = memRepo.findById(1);

        assertTrue(person.isPresent());
        assertEquals(JOHN, person.get());
    }

    @Test
    void updateByIdReturnsEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertTrue(memRepo.updateById(1, JANE_DATA).isEmpty());
    }

    @Test
    void updateByIdShouldUpdate() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        memRepo.updateById(1, JANE_DATA);
        Optional<Person> person = memRepo.findById(1);

        assertTrue(person.isPresent());
        assertEquals(new Person(1, JANE_DATA.firstName(), JANE_DATA.lastName(),
                        JANE_DATA.age()), person.get());
    }

    @Test
    void updateByIdShouldKeepId() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        memRepo.updateById(1, JANE_DATA);
        Optional<Person> person = memRepo.findById(1);

        assertTrue(person.isPresent());
        assertEquals(1, person.get().id());
    }

    @Test
    void deleteByIdReturnsEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        Optional<Person> person = memRepo.deleteById(1);

        assertTrue(person.isEmpty());
    }

    @Test
    void deleteByIdShouldRemove() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        memRepo.deleteById(1);

        assertTrue(memRepo.findById(1).isEmpty());
    }

    @Test
    void deleteByIdReturnsPerson() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        Optional<Person> person = memRepo.deleteById(1);

        assertTrue(person.isPresent());
        assertEquals(JOHN, person.get());
    }

    @Test
    void searchByNameReturnsEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);

        MyList<Person> personList = memRepo.searchByName(JANE_DATA.firstName());

        assertTrue(personList.isEmpty());
    }

    @Test
    void searchByNameShouldFindByFirstName() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);

        MyList<Person> personList = memRepo.searchByName(JOHN_DATA.firstName());

        assertEquals(1, personList.size());
        assertEquals(JOHN, personList.get(0));
    }

    @Test
    void searchByNameShouldFindByLastName() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);

        MyList<Person> personList = memRepo.searchByName(JOHN_DATA.lastName());

        assertEquals(1, personList.size());
        assertEquals(JOHN, personList.get(0));
    }

    @Test
    void searchByAgeReturnsEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        memRepo.create(JANE_DATA);
        memRepo.create(JACK_DATA);
        MyList<Person> personList = memRepo.searchByAge(0, 10);

        assertTrue(personList.isEmpty());
    }

    @Test
    void searchByAgeReturnsPersonsWithinRange() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        memRepo.create(JANE_DATA);
        memRepo.create(JACK_DATA);
        MyList<Person> personList = memRepo.searchByAge(0, 40);

        assertEquals(2, personList.size());
        assertEquals(JOHN, personList.get(0));
        assertEquals(JANE, personList.get(1));
    }

    @Test
    void searchByAgeShouldIncludeBoundaryValues() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        memRepo.create(JANE_DATA);
        memRepo.create(JACK_DATA);
        MyList<Person> personList = memRepo.searchByAge(
                JANE_DATA.age(), JOHN_DATA.age());

        assertEquals(2, personList.size());
        assertEquals(JOHN, personList.get(0));
        assertEquals(JANE, personList.get(1));
    }
}
