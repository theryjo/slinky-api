package com.theryjo.slinky.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.theryjo.slinky.model.response.TokenResponse;
import com.theryjo.slinky.security.Authorities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SigninController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiry.millis}")
    private long jwtExpiryMillis;

    @PostMapping("/signin")
    public TokenResponse signin () {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var username = ((UserDetails) auth.getPrincipal()).getUsername();
        var grantClaims = auth.getAuthorities().stream().map(grantedAuthority ->
                grantedAuthority.getAuthority()).collect(Collectors.toList());

        return new TokenResponse(getJWTToken(username, grantClaims));
    }

    private String getJWTToken(String subject, List<String> claims) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(Date.from(Instant.now().plusMillis(jwtExpiryMillis)))
                .withClaim(Authorities.GRANTED_AUTHORITIES, claims)
                .sign(Algorithm.HMAC512(jwtSecret.getBytes()));
    }
}
