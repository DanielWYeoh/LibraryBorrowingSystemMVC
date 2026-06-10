package com.fad.LibrarySystem;

import com.fad.LibrarySystem.controller.LibraryController;
import com.fad.LibrarySystem.database.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::close));
        LibraryController controller = new LibraryController();
        controller.start();
    }
}
