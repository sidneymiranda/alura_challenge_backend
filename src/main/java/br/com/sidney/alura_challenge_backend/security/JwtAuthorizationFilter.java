package br.com.sidney.alura_challenge_backend.security;

import br.com.sidney.alura_challenge_backend.service.JwtUserDetailsService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static br.com.sidney.alura_challenge_backend.security.SecurityConstants.HEADER_STRING;
import static br.com.sidney.alura_challenge_backend.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private JwtUserDetailsService userDetailsService;
    private final String secret;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtUserDetailsService userDetailsService, String secret) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.secret = secret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        if(header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if(token == null) return null;

        String username = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return username != null
                ? new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities())
                : null;
    }
}
