package com.example.subscribebook.services;

import com.example.subscribebook.models.*;
import com.example.subscribebook.repositories.*;
import com.example.subscribebook.util.JwtTokenUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;

public class AccountService {

    private final MailService mailService;
    private final UserSaltRepository userSaltRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final ProfilePictureRepository profilePictureRepository;

    private final UrlUrlResultsRepository urlUrlResultsRepository;

    private final UrlRepository urlRepository;

    public AccountService(MailService mailService, UserSaltRepository userSaltRepository, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService, UserRepository userRepository, NotificationService notificationService, ProfilePictureRepository profilePictureRepository, UrlUrlResultsRepository urlUrlResultsRepository, UrlRepository urlRepository1) {
        this.mailService = mailService;
        this.userSaltRepository = userSaltRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.profilePictureRepository = profilePictureRepository;
        this.urlUrlResultsRepository = urlUrlResultsRepository;
        this.urlRepository = urlRepository1;
    }

    public ResponseEntity<?> getToken(int id, AuthenticationRequest authenticationRequest) {
        authenticationRequest.setPassword(DigestUtils.sha256Hex(authenticationRequest.getPassword()+userSaltRepository.getSaltByUser(id)));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().build();
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    public ResponseEntity<?> register(RegisterInput registerInput) {
        if(userRepository.existsByUsername(registerInput.getName())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with that name already exists");
        if(registerInput.getName().length()<6) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name should be at least 6");
        if(userRepository.existsByEmail(registerInput.getEmail())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with that email already exists");
        if(!EmailValidator.getInstance().isValid(registerInput.getEmail())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email does not match ");
        String salt = getSalt();
        String sha256hex = DigestUtils.sha256Hex(registerInput.getPassword()+salt);
        User user = userRepository.createUser(registerInput.getName(), sha256hex, registerInput.getEmail());
        userSaltRepository.createUserSalt(user.getId(),salt);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    public String getSalt() {
        byte[] s = new byte[16];
        new SecureRandom().nextBytes(s);
        return Base64.getEncoder().encodeToString(s);
    }

    public ResponseEntity<?> delete(int id) {
        userSaltRepository.deleteUserSalt(id);
        urlUrlResultsRepository.deleteUrlUrlResult(urlRepository.getUrlsById(id));
        urlRepository.getUrlsById(id).forEach(urlRepository::deleteUrlById);
        userRepository.deleteUserWithName(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getProfile(Integer id) {
        User user = userRepository.getUser(id);
        user.setPassword(null);
        return ResponseEntity.ok().body(user);
    }

    public ResponseEntity<?>  createProfilePicture(int idFrom, String base64) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
        Files.write(Paths.get("C:\\Users\\angel.bohosyan\\Desktop\\images\\"+idFrom+".png"), decodedBytes);
        profilePictureRepository.deleteProfilePicture(idFrom);
        profilePictureRepository.createProfilePicture(idFrom,"C:\\Users\\angel.bohosyan\\Desktop\\images\\"+idFrom+".png");
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getProfilePicture(Integer idFrom) throws IOException {
        String path = profilePictureRepository.getPath(idFrom);
        File file = new File(path);
        byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(org.apache.commons.io.FileUtils.readFileToByteArray(file));
        return ResponseEntity.ok().body(new String(encoded, StandardCharsets.US_ASCII));
    }

    public ResponseEntity<?> refreshPassword(String oldPassword, Password newPassword) {
        User user = userRepository.getUserWithPassword(oldPassword);
        String salt = getSalt();
        String sha256hex = DigestUtils.sha256Hex(newPassword.getPassword()+salt);
        userRepository.refreshPassword(user.getId(),sha256hex);
        userSaltRepository.refreshSalt(user.getId(),salt);
        return ResponseEntity.ok().build();
    }
}

