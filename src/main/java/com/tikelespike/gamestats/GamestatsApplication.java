package com.tikelespike.gamestats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * The main class of the application. Starts the Spring Boot application.
 */
// CHECKSTYLE.OFF: FinalClass // Spring Boot requires this class not to be final
@SpringBootApplication
public class GamestatsApplication {
// CHECKSTYLE.ON: FinalClass

    private GamestatsApplication() {
    }

    /**
     * Entry point of the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(GamestatsApplication.class, args);
    }

}
