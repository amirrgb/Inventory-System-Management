package com.example.repository;

import com.example.model.Invoice_item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
public class InvoicesItemRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Invoice_item item) {
        entityManager.persist(item);
    }

    @Transactional
    public void update(Invoice_item item) {
        entityManager.merge(item);
    }

    @Transactional
    public List<Invoice_item> findAllByInvoiceId(BigInteger invoice_id){
        try{
            return entityManager.createQuery("SELECT i FROM Invoice_item i WHERE i.invoice.invoice_id =: invoice_id", Invoice_item.class)
                    .setParameter("invoice_id", invoice_id)
                    .getResultList();
        }catch (Exception e){
            return null;
        }

    }
}
