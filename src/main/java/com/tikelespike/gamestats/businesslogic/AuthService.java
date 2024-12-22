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

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository repository;
    private final UserEntityMapper mapper;

    public AuthService(UserRepository repository, UserEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = repository.findByEmail(username);
        return mapper.toBusinessObject(user);
    }

    public UserDetails signUp(SignupRequest data) {
        if (repository.findByEmail(data.getEmail()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
        User newUser = new User(data.getEmail(), encryptedPassword, Set.of(UserRole.USER));
        UserEntity transferObject = mapper.toTransferObject(newUser);
        UserEntity saved = repository.save(transferObject);
        return mapper.toBusinessObject(saved);
    }
}
