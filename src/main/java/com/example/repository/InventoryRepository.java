package com.example.repository;

import com.example.model.Inventory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class InventoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Object entity) {
        entityManager.persist(entity);
    }

    @Transactional
    public void update(Object entity) {
        entityManager.merge(entity);
    }

    @Transactional
    public void delete(Object entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    @Transactional
    public Inventory findByProductId(BigInteger productId) {
        try{
            return entityManager.createQuery("from Inventory i where i.product.product_id = :productId", Inventory.class)
                    .setParameter("productId", productId)
                    .getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

    @Transactional
    public void saveOrUpdate(Inventory inventory) {
        if (findByProductId(inventory.getProduct().getProduct_id()) == null) {
            save(inventory);
        } else {
            update(inventory);
        }
    }
}
