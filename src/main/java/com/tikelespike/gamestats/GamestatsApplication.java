package com.tikelespike.gamestats;

import com.tikelespike.gamestats.businesslogic.entities.UserCreationRequest;
import com.tikelespike.gamestats.businesslogic.entities.UserRole;
import com.tikelespike.gamestats.businesslogic.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * The main class of the application. Starts the Spring Boot application.
 */
// Spring Boot requires this class not to be final and to have a public constructor
// CHECKSTYLE.OFF: FinalClass
// CHECKSTYLE.OFF: HideUtilityClassConstructor
@SpringBootApplication
public class GamestatsApplication {
// CHECKSTYLE.ON: FinalClass
// CHECKSTYLE.ON: HideUtilityClassConstructor

    /**
     * Entry point of the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(GamestatsApplication.class, args);
    }

    /**
     * Upon application startup, check if an admin user exists, and if not, create one with a default password.
     *
     * @param userService the user service to use for checking and creating the admin user
     * @param encoder the password encoder to use for encoding the admin password
     *
     * @return a CommandLineRunner that will be executed on application startup
     */
    @Bean
    CommandLineRunner initAdmin(UserService userService, PasswordEncoder encoder) {
        return args -> {
            if (userService.loadUserByUsername("admin") == null) {
                UserCreationRequest admin = new UserCreationRequest(
                        "System Administrator",
                        "admin",
                        encoder.encode("admin"),
                        UserRole.ADMIN,
                        null
                );
                userService.createUser(admin);
            }
        };
    }

}
