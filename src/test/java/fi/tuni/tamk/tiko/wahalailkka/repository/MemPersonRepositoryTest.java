package fi.tuni.tamk.tiko.wahalailkka.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemPersonRepositoryTest {
    @Test
    void findAllReturnEmpty() {
        MemPersonRepository memRepo = new MemPersonRepository();

        assertTrue(memRepo.findAll().isEmpty());
    }
}
