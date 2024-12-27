package com.tikelespike.gamestats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


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

}
