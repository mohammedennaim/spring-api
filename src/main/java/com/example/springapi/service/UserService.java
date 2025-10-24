package com.example.springapi.service;
import com.example.springapi.model.User;
import java.util.List;
public interface UserService {
    List<User> getAll();
    User getById(Long id);
    User save(User u);
    void delete(Long id);
}