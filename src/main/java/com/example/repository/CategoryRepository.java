package com.example.repository;

import com.example.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Category category) {
        entityManager.merge(category);
    }


    @Transactional
    public List<Category> findAll() {
        return entityManager.createQuery("from Category", Category.class).getResultList();
    }

    @Transactional
    public Category findById(BigInteger id) {
        return entityManager.find(Category.class, id);
    }

    @Transactional
    public Category findByName(String name) {
        try{
            return entityManager.createQuery("from Category c where c.name = :name", Category.class)
                    .setParameter("name", name)
                    .getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

    @Transactional
    public void delete(Category category) {
        if (entityManager.contains(category)) {
            entityManager.remove(category);
        } else {
            Category managedCategory = entityManager.find(Category.class, category.getCategory_id());
            if (managedCategory != null) {
                entityManager.remove(managedCategory);
            }
        }
    }

    @Transactional
    public void updateCategory(Category category) {
        if (category != null && category.getCategory_id() != null) {
            entityManager.merge(category);
        }
    }
}
