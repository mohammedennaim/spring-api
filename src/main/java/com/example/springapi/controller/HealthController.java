package com.example.springapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {
    
    @Value("${spring.profiles.active:default}")
    private String activeProfile;
    
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("activeProfile", activeProfile);
        response.put("datasourceUrl", datasourceUrl);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/simple")
    public ResponseEntity<String> simpleHealth() {
        return ResponseEntity.ok("OK");
    }
}