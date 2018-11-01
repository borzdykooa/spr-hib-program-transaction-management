package com.borzdykooa.dao;

import com.borzdykooa.config.ApplicationConfiguration;
import com.borzdykooa.entity.Trainer;
import com.borzdykooa.service.TrainerService;
import com.borzdykooa.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/*
Класс для тестирования методов TrainerDao
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class TrainerDaoTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private TestDataImporter testDataImporter;

    @Autowired
    private TrainerService trainerService;

    @Before
    public void initDb() {
        testDataImporter.deleteTestData();
        testDataImporter.importTestData();
    }

    @Test
    public void testFind() {
        Trainer trainer;
        try (Session session = sessionFactory.openSession()) {
            trainer = session.createQuery("select t from Trainer t ", Trainer.class)
                    .list()
                    .stream()
                    .findFirst()
                    .orElse(null);
            assertNotNull(trainer);
        }
        String name = trainer.getName();

        Trainer theSameTrainer = trainerService.find(trainer.getId());
        String theSameName = theSameTrainer.getName();
        assertEquals(name, theSameName);
    }

    @Test
    public void testSave() {
        Trainer nikolaiNikolaev = new Trainer("Nikolai Nikolaev", "C", 12);
        Long id = trainerService.save(nikolaiNikolaev);
        System.out.println(nikolaiNikolaev.toString());
        assertNotNull("Entity is not saved", id);
    }

    @Test
    public void testDelete() {
        Trainer trainer;
        try (Session session = sessionFactory.openSession()) {
            trainer = session.createQuery("select t from Trainer t where t.name like 'Andrei Reut'", Trainer.class)
                    .list()
                    .stream()
                    .findFirst()
                    .orElse(null);
            assertNotNull(trainer);
        }
        trainerService.delete(trainer);

        try (Session session = sessionFactory.openSession()) {
            Trainer theSameTrainer = session.createQuery("select t from Trainer t where t.name like 'Andrei Reut'", Trainer.class)
                    .list()
                    .stream()
                    .findFirst()
                    .orElse(null);
            assertNull("Entity is not null!", theSameTrainer);
        }
    }
}
