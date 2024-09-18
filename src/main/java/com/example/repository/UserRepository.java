package com.example.repository;

import com.example.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Transactional
    public User findUserByUsername(String username) {
        try{
            return entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

    @Transactional
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Transactional
    public boolean isFirstTime(){
        try{
            Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
            System.out.println("count of users: " + count);
            return count == 0;
        }catch (Exception e){
            return false;
        }
    }


}
