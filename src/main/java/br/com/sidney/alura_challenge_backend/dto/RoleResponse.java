package br.com.sidney.alura_challenge_backend.dto;

import br.com.sidney.alura_challenge_backend.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleResponse {
    private Long id;
    private String name;

    public RoleResponse(Role role) {
        this.id = role.getId();
        this.name = role.getAuthority();
    }
}
