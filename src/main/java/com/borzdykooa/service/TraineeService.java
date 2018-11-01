package com.borzdykooa.service;

import com.borzdykooa.dao.TraineeDao;
import com.borzdykooa.entity.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/*
Сервисный класс, обеспечивающий транзакционность методам Dao
 */
@Service
public class TraineeService {

    private TransactionTemplate transactionTemplate;
    private TraineeDao traineeDao;

    @Autowired
    public TraineeService(HibernateTransactionManager transactionManager, TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public Long save(Trainee trainee) {
        return transactionTemplate.execute(status -> traineeDao.save(trainee));
    }

    public List<Trainee> findAll() {
        return transactionTemplate.execute(status -> traineeDao.findAll());
    }
}
