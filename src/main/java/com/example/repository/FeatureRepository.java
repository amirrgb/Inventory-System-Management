package com.example.repository;

import com.example.model.Feature;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public class FeatureRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Feature feature) {
        entityManager.persist(feature);
    }

    @Transactional
    public void update(Feature feature) {
        entityManager.merge(feature);
    }

    @Transactional
    public Feature findById(Long id) {
        return entityManager.find(Feature.class, id);
    }

    @Transactional
    public List<Feature>  findAll() {
        return entityManager.createQuery("SELECT f FROM Feature f", Feature.class).getResultList();
    }



    @Transactional
    public Feature findByNameAndCategory(String featureName, BigInteger categoryId) {
        try {
            return entityManager.createQuery("SELECT f FROM Feature f WHERE f.feature = :featureName AND f.category.category_id = :categoryId", Feature.class)
                    .setParameter("featureName", featureName)
                    .setParameter("categoryId", categoryId)
                    .getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

    @Transactional
    public List<Feature> findAllByCategory(BigInteger categoryId) {
        try{
            return entityManager.createQuery("SELECT f FROM Feature f WHERE f.category.category_id = :categoryId", Feature.class)
                   .setParameter("categoryId", categoryId)
                   .getResultList();
        }catch (Exception e){
            return null;
        }
    }


    @Transactional
    public void delete(Feature feature) {
        if (entityManager.contains(feature)) {
            entityManager.remove(feature);
        } else {
            Feature managedFeature = entityManager.find(Feature.class, feature.getFeature_id());
            if (managedFeature != null) {
                entityManager.remove(managedFeature);
            }
        }
    }

}
