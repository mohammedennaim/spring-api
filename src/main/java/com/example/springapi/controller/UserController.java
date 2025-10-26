package com.example.springapi.controller;
import com.example.springapi.model.User;
import com.example.springapi.model.ApiResponse;
import com.example.springapi.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final IUserService service;
    public UserController(IUserService s){this.service=s;}

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(){
        try {
            List<User> users = service.getAll();
            ApiResponse<List<User>> response = ApiResponse.success("Utilisateurs récupérés avec succès", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<User>> response = ApiResponse.error("Erreur lors de la récupération des utilisateurs", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable("id") Long id){
        try {
            User user = service.getById(id);
            ApiResponse<User> response = ApiResponse.success("Utilisateur récupéré avec succès", user);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            ApiResponse<User> response = ApiResponse.error("Utilisateur non trouvé", "Aucun utilisateur trouvé avec l'ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<User> response = ApiResponse.error("Erreur lors de la récupération de l'utilisateur", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping 
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user){
        try {
            if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
                ApiResponse<User> response = ApiResponse.error("Données invalides", "Le nom est obligatoire");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            User savedUser = service.save(user);
            ApiResponse<User> response = ApiResponse.success("Utilisateur créé avec succès", savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<User> response = ApiResponse.error("Erreur lors de la création de l'utilisateur", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PutMapping("/{id}") 
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable("id") Long id, @RequestBody User user){
        try {
            if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
                ApiResponse<User> response = ApiResponse.error("Données invalides", "Le nom est obligatoire");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            service.getById(id);
            user.setId(id);
            User updatedUser = service.save(user);
            ApiResponse<User> response = ApiResponse.success("Utilisateur mis à jour avec succès", updatedUser);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            ApiResponse<User> response = ApiResponse.error("Utilisateur non trouvé", "Aucun utilisateur trouvé avec l'ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<User> response = ApiResponse.error("Erreur lors de la mise à jour de l'utilisateur", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @DeleteMapping("/{id}") 
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable("id") Long id){
        try {
            service.getById(id);
            service.delete(id);
            ApiResponse<String> response = ApiResponse.success("Utilisateur supprimé avec succès");
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            ApiResponse<String> response = ApiResponse.error("Utilisateur non trouvé", "Aucun utilisateur trouvé avec l'ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error("Erreur lors de la suppression de l'utilisateur", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}