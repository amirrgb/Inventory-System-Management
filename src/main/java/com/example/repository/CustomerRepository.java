package com.example.repository;

import com.example.model.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Customer customer) {
        entityManager.persist(customer);
    }

    @Transactional
    public void update(Customer customer) {
        entityManager.merge(customer);
    }

    @Transactional
    public Customer findByPhoneNumber(String phoneNumber) {
        try{
            return entityManager.find(Customer.class, phoneNumber);
        }catch (Exception e){
            return null;
        }
    }

}
