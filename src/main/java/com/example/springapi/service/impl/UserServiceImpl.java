package com.example.springapi.service.impl;
import com.example.springapi.model.User;
import com.example.springapi.repository.UserRepository;
import com.example.springapi.service.UserService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    public UserServiceImpl(UserRepository repo){this.repo=repo;}
    public List<User> getAll(){return repo.findAll();}
    public User getById(Long id){return repo.findById(id);}
    public User save(User u){return repo.save(u);}
    public void delete(Long id){repo.delete(id);}
}