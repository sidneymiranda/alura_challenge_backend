package br.com.sidney.alura_challenge_backend.model;

import br.com.sidney.alura_challenge_backend.dto.RoleRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import java.util.Set;

import static br.com.sidney.alura_challenge_backend.security.SecurityConstants.ROLE;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    public Role(RoleRequest role) {
        this.name = ROLE + role.getName().toUpperCase();
    }

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Override
    public String getAuthority() {
        return this.name;
    }

}
