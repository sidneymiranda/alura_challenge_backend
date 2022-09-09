package br.com.sidney.alura_challenge_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object to return as body in JWT Authentication.
 */
public class JwtToken {
    private String idToken;
    private String type;

    public JwtToken(String idToken) {
        this.idToken = idToken;
        this.type = "Bearer";
    }

    @JsonProperty("id_token")
    String getIdToken() {
        return idToken;
    }

    public String getType() {
        return type;
    }
}
