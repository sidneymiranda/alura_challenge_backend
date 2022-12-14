package br.com.sidney.alura_challenge_backend.service;

import br.com.sidney.alura_challenge_backend.dto.UserRequest;
import br.com.sidney.alura_challenge_backend.dto.UserResponse;
import br.com.sidney.alura_challenge_backend.exceptions.ResourceNotFoundException;
import br.com.sidney.alura_challenge_backend.exceptions.ValidationException;
import br.com.sidney.alura_challenge_backend.model.Role;
import br.com.sidney.alura_challenge_backend.model.User;
import br.com.sidney.alura_challenge_backend.repository.AuthorityRepository;
import br.com.sidney.alura_challenge_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public UserResponse save(UserRequest user) {
        Optional<User> found = userRepository.findByUsername(user.getUsername());

        if(found.isPresent()) {
            throw new ValidationException("Username already exists, try again!");
        }
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        User newUser = User.builder()
                .password(encoder.encode(user.getPassword()))
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(mapAuthority(user))
                .build();
        User savedUser = userRepository.save(newUser);

        return new UserResponse(savedUser);
    }

    private Set<Role> mapAuthority(UserRequest user) {
        return user.getRoles().stream()
                .map(role -> authorityRepository.findByName("ROLE_" + role.getName().toUpperCase())
                        .orElseThrow(() -> new ResourceNotFoundException("Authority not found")))
                .collect(Collectors.toSet());
    }
}
