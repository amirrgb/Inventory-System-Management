package com.example.repository;

import com.example.model.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Transaction transaction) {
        System.out.println("try to save transaction: " + transaction);
        entityManager.persist(transaction);
    }


}
