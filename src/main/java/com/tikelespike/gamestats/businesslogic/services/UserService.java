package com.tikelespike.gamestats.businesslogic.services;

import com.tikelespike.gamestats.businesslogic.entities.User;
import com.tikelespike.gamestats.businesslogic.entities.UserCreationRequest;
import com.tikelespike.gamestats.businesslogic.exceptions.InvalidDataException;
import com.tikelespike.gamestats.businesslogic.exceptions.ResourceNotFoundException;
import com.tikelespike.gamestats.businesslogic.exceptions.StaleDataException;
import com.tikelespike.gamestats.businesslogic.mapper.UserPlayerEntityMapper;
import com.tikelespike.gamestats.businesslogic.mapper.UserRoleEntityMapper;
import com.tikelespike.gamestats.data.entities.UserEntity;
import com.tikelespike.gamestats.data.repositories.UserRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
     * Returns all users in the system.
     *
     * @return a list of all users in the system
     */
    public List<User> getAllUsers() {
        return repository.findAll().stream().map(mapper::toBusinessObject).toList();
    }

    /**
     * Deletes a user from the system. No effect if user does not exist.
     *
     * @param id the ID of the user to delete
     */
    @Transactional
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    /**
     * Updates an existing user in the system.
     *
     * @param user the user to update. A user with the same id must already exist in the system.
     *
     * @return the updated user
     * @throws ResourceNotFoundException if the user with the given id does not exist
     * @throws StaleDataException if the user has been modified or deleted in the meantime (concurrently)
     */
    @Transactional()
    public User updateUser(User user) throws StaleDataException {
        Objects.requireNonNull(user, "User may not be null");

        if (!repository.existsById(user.getId())) {
            throw new ResourceNotFoundException("User with id " + user.getId() + " does not exist");
        }

        UserEntity existingUser = repository.findByEmail(user.getEmail());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new InvalidDataException("User with that mail address already exists");
        }

        if (user.getPlayer() != null) {
            UserEntity otherOwner = repository.findByPlayerId(user.getPlayer().getId());
            if (otherOwner != null && !otherOwner.getId().equals(user.getId())) {
                throw new InvalidDataException("Player is already associated with another user");
            }
        }

        UserEntity entityToSave = mapper.toTransferObject(user);
        UserEntity savedEntity;
        try {
            savedEntity = repository.save(entityToSave);
        } catch (StaleObjectStateException | OptimisticLockException | OptimisticLockingFailureException e) {
            throw new StaleDataException(e);
        }
        return mapper.toBusinessObject(savedEntity);
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

        // Check if the player is already associated with another user
        if (data.player() != null) {
            UserEntity otherOwner = repository.findByPlayerId(data.player().getId());
            if (otherOwner != null) {
                throw new InvalidDataException("Player is already associated with another user");
            }
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserEntity transferObject = new UserEntity(
                null,
                null,
                data.name(),
                data.email(),
                encryptedPassword,
                data.player() == null ? null : mapper.toTransferObject(data.player()),
                roleMapper.toTransferObjectNoCheck(data.role())
        );
        UserEntity saved = repository.save(transferObject);
        return mapper.toBusinessObject(saved);
    }
}
