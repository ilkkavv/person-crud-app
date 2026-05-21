package fi.tuni.tamk.tiko.wahalailkka.repository;

import fi.tuni.tamk.tiko.wahalailkka.datastructure.MyList;
import fi.tuni.tamk.tiko.wahalailkka.model.Person;
import fi.tuni.tamk.tiko.wahalailkka.model.PersonData;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void findAllEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertTrue(memRepo.findAll().isEmpty());
    }

    @Test
    void createCorrectData() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertEquals(JOHN, memRepo.create(JOHN_DATA));
    }

    @Test
    void createIncrementsId() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertEquals(1, memRepo.create(JOHN_DATA).id());
        assertEquals(2, memRepo.create(JANE_DATA).id());
        assertEquals(3, memRepo.create(JACK_DATA).id());
    }

    @Test
    void findAllPersons() {
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
    void findByIdEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        Optional<Person> person = memRepo.findById(1);

        assertTrue(person.isEmpty());
    }

    @Test
    void findByIdPerson() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        Optional<Person> person = memRepo.findById(1);

        assertTrue(person.isPresent());
        assertEquals(JOHN, person.get());
    }

    @Test
    void updateByIdEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertTrue(memRepo.updateById(1, JANE_DATA).isEmpty());
    }

    @Test
    void updateByIdUpdates() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        memRepo.updateById(1, JANE_DATA);
        Optional<Person> person = memRepo.findById(1);

        assertTrue(person.isPresent());
        assertEquals(new Person(1, JANE_DATA.firstName(), JANE_DATA.lastName(),
                        JANE_DATA.age()), person.get());
    }

    @Test
    void updateByIdKeepsId() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        memRepo.updateById(1, JANE_DATA);
        Optional<Person> person = memRepo.findById(1);

        assertTrue(person.isPresent());
        assertEquals(1, person.get().id());
    }

    @Test
    void deleteByIdEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        Optional<Person> person = memRepo.deleteById(1);

        assertTrue(person.isEmpty());
    }

    @Test
    void deleteByIdRemoves() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        memRepo.deleteById(1);

        assertTrue(memRepo.findById(1).isEmpty());
    }

    @Test
    void deleteByIdPerson() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        Optional<Person> person = memRepo.deleteById(1);

        assertTrue(person.isPresent());
        assertEquals(JOHN, person.get());
    }

    @Test
    void searchByNameEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        MyList<Person> personList = memRepo.searchByName(JANE_DATA.firstName());

        assertTrue(personList.isEmpty());
    }

    @Test
    void searchByFirstName() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        MyList<Person> personList = memRepo.searchByName(JOHN_DATA.firstName());

        assertEquals(1, personList.size());
        assertEquals(JOHN, personList.get(0));
    }

    @Test
    void searchByLastName() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        MyList<Person> personList = memRepo.searchByName(JOHN_DATA.lastName());

        assertEquals(1, personList.size());
        assertEquals(JOHN, personList.get(0));
    }

    @Test
    void searchByAgeEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        memRepo.create(JANE_DATA);
        memRepo.create(JACK_DATA);
        MyList<Person> personList = memRepo.searchByAge(0, 10);

        assertTrue(personList.isEmpty());
    }

    @Test
    void searchByAgeRange() {
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
    void searchByAgeBoundaries() {
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

    @Test
    void findAllDefensiveCopy() {
        MemPersonRepository memRepo = new MemPersonRepository();

        memRepo.create(JOHN_DATA);
        memRepo.create(JANE_DATA);
        memRepo.create(JACK_DATA);
        MyList<Person> returnedList = memRepo.findAll();
        returnedList.remove(1);
        MyList<Person> repositoryList = memRepo.findAll();

        assertEquals(2, returnedList.size());
        assertEquals(3, repositoryList.size());
    }
}
