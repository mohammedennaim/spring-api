package com.example.springapi.repository;

import com.example.springapi.model.User;
import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(Long id);
    boolean existsById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    long count();
}
