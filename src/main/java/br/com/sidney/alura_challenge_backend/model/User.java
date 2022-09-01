package br.com.sidney.alura_challenge_backend.model;

import br.com.sidney.alura_challenge_backend.dto.UserRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;

    @Column
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "user_fk", value = ConstraintMode.CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "role_fk", value = ConstraintMode.CONSTRAINT)))
    private Set<Role> roles;

    public User(UserRequest user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles()
                        .stream()
                        .map(Role::new)
                        .collect(Collectors.toSet());
    }
}
