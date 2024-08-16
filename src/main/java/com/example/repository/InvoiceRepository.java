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

    @Transactional
    public Invoice findById(Long id) {
        return entityManager.find(Invoice.class, id);
    }

    @Transactional
    public List<Invoice> findByCustomerName(String customerName) {
        String firstName = customerName.split(" ")[0];
        String lastName = customerName.split(" ")[1];
        try{
            return entityManager.createQuery("from Invoice i where i.customer.first_name = :firstName OR i.customer.last_name = :lastName", Invoice.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName)
                    .getResultList();
        }catch (Exception e){
            return null;
        }
    }

}
