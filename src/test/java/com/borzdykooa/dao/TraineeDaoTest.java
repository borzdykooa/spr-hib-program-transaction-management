package com.borzdykooa.dao;

import com.borzdykooa.config.ApplicationConfiguration;
import com.borzdykooa.entity.Trainee;
import com.borzdykooa.entity.Trainer;
import com.borzdykooa.service.TraineeService;
import com.borzdykooa.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/*
Класс для тестирования методов TraineeDao
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class TraineeDaoTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private TestDataImporter testDataImporter;

    @Autowired
    private TraineeService traineeService;

    @Before
    public void initDb() {
        testDataImporter.deleteTestData();
        testDataImporter.importTestData();
    }

    @Test
    public void testFindAll() {
        List<Trainee> trainees = traineeService.findAll();
        assertEquals(2, trainees.size());
    }

    @Test
    public void testSave() {
        Trainer trainer;
        try (Session session = sessionFactory.openSession()) {
            trainer = session.createQuery("select t from Trainer t where t.name like 'Andrei Reut'", Trainer.class)
                    .list()
                    .stream()
                    .findFirst()
                    .orElse(null);
            assertNotNull(trainer);
        }
        Trainee alexandrAlexandrov = new Trainee("Alexandr Alexandrov", trainer);
        Long id = traineeService.save(alexandrAlexandrov);
        assertNotNull("Entity is not saved", id);
    }
}
