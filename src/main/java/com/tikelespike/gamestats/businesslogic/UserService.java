package com.tikelespike.gamestats.businesslogic;

import com.tikelespike.gamestats.businesslogic.entities.SignupRequest;
import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.businesslogic.entities.UserRole;
import com.tikelespike.gamestats.businesslogic.mapper.UserEntityMapper;
import com.tikelespike.gamestats.data.entities.UserEntity;
import com.tikelespike.gamestats.data.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service for managing user registration and querying.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final UserEntityMapper mapper;

    /**
     * Creates a new user service. This is usually done by the Spring framework, which manages the service's lifecycle
     * and injects the required dependencies.
     *
     * @param repository repository managing user entities
     * @param mapper mapper for converting between user business objects and user entities
     */
    public UserService(UserRepository repository, UserEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = repository.findByEmail(username);
        return mapper.toBusinessObject(user);
    }

    /**
     * Creates a new user account.
     *
     * @param data the sign-up request data
     *
     * @return the credentials of the newly created user
     */
    public UserDetails signUp(SignupRequest data) {
        if (repository.findByEmail(data.email()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.name(), data.email(), encryptedPassword, Set.of(UserRole.USER));
        UserEntity transferObject = mapper.toTransferObject(newUser);
        UserEntity saved = repository.save(transferObject);
        return mapper.toBusinessObject(saved);
    }
}
