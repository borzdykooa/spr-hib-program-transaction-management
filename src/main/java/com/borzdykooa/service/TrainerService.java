package com.borzdykooa.service;

import com.borzdykooa.dao.TrainerDao;
import com.borzdykooa.entity.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/*
Сервисный класс, обеспечивающий транзакционность методам Dao
 */
@Service
public class TrainerService {

    private TransactionTemplate transactionTemplate;
    private TrainerDao trainerDao;

    @Autowired
    public TrainerService(HibernateTransactionManager transactionManager, TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public Long save(Trainer trainer) {
        return transactionTemplate.execute(status -> trainerDao.save(trainer));

    }

    public Trainer find(Long id) {
        return transactionTemplate.execute(status -> trainerDao.find(id));
    }

    public void delete(Trainer trainer) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            public void doInTransactionWithoutResult(TransactionStatus status) {
                trainerDao.delete(trainer);
            }
        });
    }
}
