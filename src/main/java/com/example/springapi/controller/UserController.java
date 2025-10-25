package com.example.springapi.controller;
import com.example.springapi.model.User;
import com.example.springapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserService service;
    public UserController(UserService s){this.service=s;}

    @GetMapping
    public List<User> getAllUsers(){return service.getAll();}
    
    @GetMapping("/getAll")
    public List<User> all(){return service.getAll();}
    
    @GetMapping("/{id:[0-9]+}") 
    public User one(@PathVariable("id") Long id){
        return service.getById(id);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User u){
        return service.save(u);
    }
    
    @PutMapping("/{id:[0-9]+}")
    public User update(@PathVariable("id") Long id, @RequestBody User u){
        u.setId(id);
        return service.save(u);
    }
    
    @DeleteMapping("/{id:[0-9]+}")
    public void delete(@PathVariable("id") Long id){
        service.delete(id);
    }
}