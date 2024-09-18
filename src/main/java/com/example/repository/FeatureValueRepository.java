package com.example.repository;

import com.example.model.Feature_value;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
public class FeatureValueRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Feature_value feature_value){
        entityManager.persist(feature_value);
    }

    @Transactional
    public void update(Feature_value feature_value){
        entityManager.merge(feature_value);
    }

    @Transactional
    public Feature_value findByProductAndFeature(BigInteger productId, BigInteger featureId){
        try{
            return entityManager.createQuery("from Feature_value fv where fv.product.product_id = :productId and fv.feature.feature_id = :featureId", Feature_value.class)
                    .setParameter("productId", productId)
                    .setParameter("featureId", featureId)
                    .getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

    @Transactional
    public List<Feature_value> findAllByProduct(BigInteger productId){
        try{
            return entityManager.createQuery("from Feature_value fv where fv.product.product_id = :productId", Feature_value.class)
                    .setParameter("productId", productId)
                    .getResultList();
        }catch (Exception e){
            return null;
        }
    }
}
