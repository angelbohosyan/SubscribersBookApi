package com.example.subscribebook.controllers;

import java.io.IOException;

import com.example.subscribebook.models.*;
import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.repositories.UserSaltRepository;
import com.example.subscribebook.services.AccountService;
import com.example.subscribebook.services.MailService;
import com.example.subscribebook.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
public class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepository;

    private final UserSaltRepository userSaltRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final MailService mailService;

    public AccountController(AccountService accountService, UserRepository userRepository, UserSaltRepository userSaltRepository, JwtTokenUtil jwtTokenUtil, MailService mailService) {
        this.accountService = accountService;
        this.userRepository = userRepository;
        this.userSaltRepository = userSaltRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.mailService = mailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        Integer id = userRepository.getUserWithName(authenticationRequest.getUsername()).getId();
        return accountService.getToken(id,authenticationRequest);
    }
    @PostMapping("/forgottenPassword")
    public ResponseEntity<?> forgottenPassword(@RequestBody UserName userName) {
        User user = userRepository.getUserWithName(userName.getName());
        mailService.sendMail("New Password","localhost:8080/refreshPassword/" + user.getPassword(),user.getEmail());
        return ResponseEntity.ok().body("Email is sent!");
    }
    @PostMapping("/refreshPassword/{password}")
    public ResponseEntity<?> refreshPassword(@PathVariable String password,@RequestBody Password newPassword) {
        return accountService.refreshPassword(password,newPassword);
    }


    @GetMapping("account/{name}")
    public ResponseEntity<?> getProfile(@PathVariable String name) {
        Integer id = userRepository.getUserWithName(name).getId();
        return accountService.getProfile(id);
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestHeader(name = "Authorization") String token,@RequestBody Base64Json base64String) throws IOException {
        Integer idFrom = jwtTokenUtil.extractIdWithBearer(token);
        return accountService.createProfilePicture(idFrom,base64String.getBase64String());
    }

    @GetMapping("/image/{username}")
    public ResponseEntity<?> getImage(@RequestHeader(name = "Authorization") String token) throws IOException {
        Integer idFrom = jwtTokenUtil.extractIdWithBearer(token);
        return accountService.getProfilePicture(idFrom);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterInput registerInput) {
        return accountService.register(registerInput);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestHeader(name = "Authorization") String token) {
        return accountService.delete(jwtTokenUtil.extractIdWithBearer(token));
    }
}
