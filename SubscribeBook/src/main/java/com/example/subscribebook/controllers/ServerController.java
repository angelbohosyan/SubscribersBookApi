package com.example.subscribebook.controllers;

import com.example.subscribebook.Properties;
import com.example.subscribebook.models.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class ServerController {

    @PostMapping("/token")
    public ResponseEntity<?> newToken(@RequestBody Token token) {
        Properties.key = token.getToken();
        return ResponseEntity.ok().build();
    }
}
