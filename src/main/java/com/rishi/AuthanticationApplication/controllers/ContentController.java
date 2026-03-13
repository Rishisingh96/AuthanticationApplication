package com.rishi.AuthanticationApplication.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentController {

    @GetMapping("/api/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("This is PUBLIC content. No authentication required.");
    }

    @GetMapping("/api/user")
    public ResponseEntity<String> userEndpoint() {
        return ResponseEntity.ok("This is USER content. Accessible by USER and ADMIN roles.");
    }

    @GetMapping("/api/admin")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("This is ADMIN content. Only ADMIN can access.");
    }
}
