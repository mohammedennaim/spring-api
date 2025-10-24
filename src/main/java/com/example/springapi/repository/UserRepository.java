package com.example.springapi.repository;
import com.example.springapi.model.User;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class UserRepository {
    @PersistenceContext EntityManager em;

    public List<User> findAll(){return em.createQuery("FROM User",User.class).getResultList();}
    public User findById(Long id){return em.find(User.class,id);}
    public User save(User u){
        if(u.getId()==null){em.persist(u);return u;}else{return em.merge(u);}
    }
    public void delete(Long id){User u=em.find(User.class,id);if(u!=null)em.remove(u);}
}