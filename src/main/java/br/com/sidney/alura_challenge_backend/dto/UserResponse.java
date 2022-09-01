package br.com.sidney.alura_challenge_backend.dto;

import br.com.sidney.alura_challenge_backend.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private List<RoleResponse> roles;

    public UserResponse(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(RoleResponse::new).collect(Collectors.toList());
    }
}
