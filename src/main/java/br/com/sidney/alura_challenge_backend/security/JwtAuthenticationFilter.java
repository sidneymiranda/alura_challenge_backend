package br.com.sidney.alura_challenge_backend.security;

import br.com.sidney.alura_challenge_backend.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static br.com.sidney.alura_challenge_backend.security.SecurityConstants.HEADER_STRING;
import static br.com.sidney.alura_challenge_backend.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private final String secret;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String secret) {
        this.authenticationManager = authenticationManager;
        this.secret = secret;
    }

    /**
     * Efetua a autenticação
     *
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return this.authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Caso a autenticação seja bem sucessida este método será invocado para gerar o token
     *
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException {
        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();

        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MICROSECONDS.convert(1, TimeUnit.DAYS)))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        String bearerToken = TOKEN_PREFIX + token;

        response.getWriter().write(bearerToken);
        response.addHeader(HEADER_STRING, bearerToken);
    }
}
