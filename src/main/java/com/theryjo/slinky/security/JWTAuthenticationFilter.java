package com.theryjo.slinky.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.theryjo.slinky.security.Authorities;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private final String jwtSecret;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    public JWTAuthenticationFilter(AuthenticationManager authManager, String jwtSecret) {
        super(authManager);
        this.jwtSecret = jwtSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        var header = req.getHeader(AUTHORIZATION_HEADER);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        SecurityContextHolder.getContext()
                .setAuthentication(getAuthentication(req));
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (token != null) {
            var jwt = JWT.require(Algorithm.HMAC512(jwtSecret.getBytes()))
                    .build()
                    .verify(token.replace(BEARER_PREFIX, ""));
            var user = jwt.getSubject();
            var grantClaims = jwt.getClaim(Authorities.GRANTED_AUTHORITIES).asList(String.class);
            var grants = grantClaims.stream().map(grant ->
                    new SimpleGrantedAuthority(grant)).collect(Collectors.toList());
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, grants);
            }
        }
        return null;
    }
}