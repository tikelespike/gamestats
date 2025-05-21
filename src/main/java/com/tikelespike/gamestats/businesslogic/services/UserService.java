package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.businesslogic.entities.UserCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.InvalidDataException;
import com.tikelespike.gamestats.businesslogic.mapper.UserPlayerEntityMapper;
import com.tikelespike.gamestats.businesslogic.mapper.UserRoleEntityMapper;
import com.tikelespike.gamestats.data.entities.UserEntity;
import com.tikelespike.gamestats.data.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service for managing user registration and querying.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final UserPlayerEntityMapper mapper;
    private final UserRoleEntityMapper roleMapper;

    /**
     * Creates a new user service. This is usually done by the Spring framework, which manages the service's lifecycle
     * and injects the required dependencies.
     *
     * @param repository repository managing user entities
     * @param mapper mapper for converting between user business objects and user entities
     * @param roleMapper mapper for converting between user role business objects and user role entities
     */
    public UserService(UserRepository repository, UserPlayerEntityMapper mapper, UserRoleEntityMapper roleMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public User loadUserByUsername(String username) {
        var user = repository.findByEmail(username);
        return mapper.toBusinessObject(user);
    }

    /**
     * Loads a user by its unique identifier.
     *
     * @param id the unique identifier of the user
     *
     * @return the user with the given id, or null if no such user exists
     */
    public User getUser(Long id) {
        var user = repository.findById(id)
                .orElse(null);
        return mapper.toBusinessObject(user);
    }

    /**
     * Checks if a user with the given id exists in the system.
     *
     * @param id the id of the user to check
     *
     * @return true if a user with the given id exists, false otherwise
     */
    public boolean userExists(Long id) {
        return repository.existsById(id);
    }

    /**
     * Creates a new user account.
     *
     * @param data the user creation request data
     *
     * @return the newly created user
     */
    @Transactional
    public User createUser(UserCreationRequest data) {
        Objects.requireNonNull(data, "Creation request may not be null");
        if (repository.findByEmail(data.email()) != null) {
            throw new InvalidDataException("User with that mail address already exists");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserEntity transferObject = new UserEntity(
                null,
                null,
                data.name(),
                data.email(),
                encryptedPassword,
                null,
                roleMapper.toTransferObjectNoCheck(data.role())
        );
        UserEntity saved = repository.save(transferObject);
        return mapper.toBusinessObject(saved);
    }
}
