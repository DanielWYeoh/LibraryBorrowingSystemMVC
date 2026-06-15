package com.fad.LibrarySystem;

import com.fad.LibrarySystem.controller.LibraryController;
import com.fad.LibrarySystem.database.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::close));
        try {
            LibraryController controller = new LibraryController();
            controller.start();
        } catch (RuntimeException e) {
            System.out.println("[FATAL] Application failed to start: " + e.getMessage());
        }
    }
}
