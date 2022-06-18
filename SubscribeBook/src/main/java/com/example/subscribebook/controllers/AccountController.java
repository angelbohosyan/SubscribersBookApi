package com.example.subscribebook.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import com.example.subscribebook.models.AuthenticationRequest;
import com.example.subscribebook.models.Base64Json;
import com.example.subscribebook.models.RegisterInput;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.services.AccountService;
import com.example.subscribebook.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
public class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    public AccountController(AccountService accountService, UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.accountService = accountService;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        Integer id = userRepository.getUserWithName(authenticationRequest.getUsername()).getId();
        return accountService.getToken(id,authenticationRequest);
    }


    @GetMapping("account/{name}")
    public ResponseEntity<?> getProfile(@PathVariable String name) {
        Integer id = userRepository.getUserWithName(name).getId();
        return accountService.getProfile(id);
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestHeader(name = "Authorization") String token,@RequestBody Base64Json base64String) throws IOException {
        Integer idFrom = userRepository.getUserWithName(jwtTokenUtil.extractUsername(token.substring(7))).getId();
        return accountService.createProfilePicture(idFrom,base64String.getBase64String());
    }

    @GetMapping("/image/{username}")
    public ResponseEntity<?> getImage(@RequestHeader(name = "Authorization") String token) throws IOException {
        Integer idFrom = userRepository.getUserWithName(jwtTokenUtil.extractUsername(token.substring(7))).getId();
        return accountService.getProfilePicture(idFrom);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterInput registerInput) {
        return accountService.register(registerInput);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestHeader(name = "Authorization") String token) {
        return accountService.delete(userRepository.getUserWithName(jwtTokenUtil.extractUsername(token.substring(7))).getId());
    }
}
