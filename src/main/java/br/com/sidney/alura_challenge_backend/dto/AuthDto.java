package br.com.sidney.alura_challenge_backend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthDto implements Serializable {
    private String username;
    private String password;
}
