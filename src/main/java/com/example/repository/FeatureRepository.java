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
    public List<Feature> findAllByProduct(BigInteger productId) {
        try{
            return entityManager.createQuery("SELECT f FROM Feature f WHERE f.product.product_id = :productId", Feature.class)
                    .setParameter("productId", productId)
                    .getResultList();
        }catch (Exception e){
            return null;
        }

    }

    @Transactional
    public long getPriceByProduct(BigInteger productId) {
        String[] priceFeatureNames = new String[]{"price"};
        Feature resultFeature = null;
        for (String priceFeatureName : priceFeatureNames) {
            resultFeature = findByNameAndProduct(priceFeatureName, productId);
            if (resultFeature!=null) break;
        }
        try {
            return Long.parseLong(resultFeature.getFeature_value());
        }catch (Exception e){
            return 0;
        }
    }

    @Transactional
    public long getDiscountByProduct(BigInteger productId) {
        String[] discountFeatureNames = new String[]{"discount"};
        Feature resultFeature = null;
        for (String discountFeatureName : discountFeatureNames) {
            resultFeature = findByNameAndProduct(discountFeatureName, productId);
            if (resultFeature!=null) break;
        }
        try {
            return Long.parseLong(resultFeature.getFeature_value());
        }catch (Exception e){
            return 0;
        }
    }

    @Transactional
    public Feature findByNameAndProduct(String featureName, BigInteger productId) {
        try {
            return entityManager.createQuery("SELECT f FROM Feature f WHERE f.feature = :featureName AND f.product.product_id = :productId", Feature.class)
                    .setParameter("featureName", featureName)
                    .setParameter("productId", productId)
                    .getSingleResult();
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
