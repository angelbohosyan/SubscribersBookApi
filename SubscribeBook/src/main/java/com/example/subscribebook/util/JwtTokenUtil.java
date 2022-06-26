package com.example.subscribebook.util;

import com.example.subscribebook.repositories.UserRepository;
import com.example.subscribebook.services.DecodeService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenUtil {

    public String SECRET_KEY = "samokov15";

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private DecodeService decodeService;

    public Integer extractIdWithBearer(String token) {
        return decodeService.decode(extractClaim(token.substring(7), Claims::getSubject));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final  Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userRepository.getUserWithName(userDetails.getUsername()).getId());
    }

    public char getSymbolOfInt(int a) {
        if(a==0) return 'a';
        if(a==1) return 'b';
        if(a==2) return 'c';
        if(a==3) return 'd';
        if(a==4) return 'e';
        if(a==5) return 'f';
        if(a==6) return 'g';
        if(a==7) return 'h';
        if(a==8) return 'i';
        if(a==9) return 'j';
        return 'q';
    }
    private String createToken(Map<String, Object> claims, int username) {
        StringBuilder stringBuilder= new StringBuilder();
        while(username>0) {
            stringBuilder.append(getSymbolOfInt(username%10));
            username /= 10;
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(stringBuilder.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final Integer id = extractIdWithBearer(token);
        return userRepository.getUser(id).getName().equals(userDetails.getUsername()) && !isTokenExpired(token.substring(7));
    }

}
