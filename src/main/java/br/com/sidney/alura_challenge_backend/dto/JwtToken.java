package br.com.sidney.alura_challenge_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object to return as body in JWT Authentication.
 */
public class JwtToken {
    private final String token;
    private final String type;

    public JwtToken(String token) {
        this.token = token;
        this.type = "Bearer";
    }

    @JsonProperty("token")
    String getToken() {
        return token;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }
}
