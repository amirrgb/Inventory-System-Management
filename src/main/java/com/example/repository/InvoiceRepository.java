package com.example.repository;

import com.example.model.Invoice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class InvoiceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Invoice invoice) {
        entityManager.persist(invoice);
    }

    @Transactional
    public void update(Invoice invoice) {
        entityManager.merge(invoice);
    }

    @Transactional
    public List<Invoice> findAll() {
        return entityManager.createQuery("from Invoice", Invoice.class).getResultList();
    }
}
