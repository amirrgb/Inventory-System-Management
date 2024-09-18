package com.example.repository;

import com.example.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
public class ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Product product) {
        entityManager.persist(product);
    }

    @Transactional
    public void update(Product product) {
        entityManager.merge(product);
    }

    @Transactional
    public List<Product> findAll() {
        return entityManager.createQuery("from Product", Product.class).getResultList();
    }

    @Transactional
    public Product findById(BigInteger id) {
        return entityManager.find(Product.class, id);
    }

    @Transactional
    public Product findByName(String name) {
        try {
            return entityManager.createQuery("from Product p where p.name = :name", Product.class)
                    .setParameter("name", name)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public List<Product> findAllByCategory(BigInteger categoryId) {
        try{
            return entityManager.createQuery("from Product p where p.category.category_id = :categoryId", Product.class)
                   .setParameter("categoryId", categoryId)
                   .getResultList();
        }catch (Exception e){
            return null;
        }

    }

    @Transactional
    public void delete(Product product) {
        Product managedProduct = entityManager.find(Product.class, product.getProduct_id());
        if (managedProduct != null) {
            entityManager.remove(managedProduct);
        }
    }
}
