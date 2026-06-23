/**
 * @author      masjohncook
 * @version     0.0.1
 * @copyright   (C) Copyright 2026
 * @license     None
 * @maintainer  masjohncook
 * @email       mas.john.cook@gmail.com
 * @status      Development
 */
package com.fad.LibrarySystem;

import com.fad.LibrarySystem.controller.LibraryController;
import com.fad.LibrarySystem.database.DatabaseManager;

/**
 * Entry point for the console version of the Library Borrowing System.
 *
 * Main initialises the application, registers a shutdown hook to close
 * the database connection cleanly on exit, and hands control to
 * LibraryController which runs the main menu loop.
 *
 * Note: The JavaFX GUI version uses App.java as its entry point instead.
 * Run the GUI with: mvn javafx:run
 */
public class Main {

    public static void main(String[] args) {
        // Register a shutdown hook so the SQLite connection is closed
        // cleanly when the JVM exits, regardless of how the app terminates.
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::close));

        try {
            LibraryController controller = new LibraryController();
            controller.start();
        } catch (RuntimeException e) {
            System.out.println("[FATAL] Application failed to start: " + e.getMessage());
        }
    }
}
