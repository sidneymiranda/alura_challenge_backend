package br.com.sidney.alura_challenge_backend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {
    private final Key key;
    private final JwtParser jwtParser;
    private final String BASE_64_SECRET = "YWNjb3VudC1tYW5hZ2VyLWFwaVNpZG5leU1pcmFuZGE=";

    public TokenProvider() {
        byte[] keyBytes = Decoders.BASE64.decode(BASE_64_SECRET);

        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public String createToken(final Authentication authentication) {
        final String authorities = authentication
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));


        return Jwts
                .builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MICROSECONDS.convert(1, TimeUnit.DAYS)))
                .compact();
    }

    public boolean validateToken(final String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);

            return true;
        } catch (Exception e) {
            log.trace("Invalid JWT token.", e);
            log.error("Token validation error {}", e.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(final String token) {
        final Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get("auth").toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
