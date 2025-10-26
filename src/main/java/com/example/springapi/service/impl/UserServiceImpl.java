package com.example.springapi.service.impl;
import com.example.springapi.model.User;
import com.example.springapi.repository.IUserRepository;
import com.example.springapi.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private final IUserRepository repository;
    
    public UserServiceImpl(IUserRepository repository) {
        this.repository = repository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return repository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
        
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new NoSuchElementException("Utilisateur non trouvé avec l'ID: " + id);
        }
        
        return user.get();
    }
    
    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null");
        }
        
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom complet est obligatoire");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }

        if (user.getId() == null) {
            if (repository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
            }
        } else {
            Optional<User> existingUser = repository.findByEmail(user.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
            }
        }
        
        return repository.save(user);
    }
    
    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
        
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Utilisateur non trouvé avec l'ID: " + id);
        }
        
        repository.deleteById(id);
    }
}