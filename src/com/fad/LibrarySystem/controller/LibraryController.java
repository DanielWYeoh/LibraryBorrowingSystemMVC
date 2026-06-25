/**
 * @author      masjohncook
 * @version     0.0.1
 * @copyright   (C) Copyright 2026
 * @license     None
 * @maintainer  masjohncook
 * @email       mas.john.cook@gmail.com
 * @status      Development
 */
package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.LibraryService;
import com.fad.LibrarySystem.view.MenuView;
import java.util.Scanner;

/**
 * Top-level console controller — the entry point for the console application loop.
 *
 * LibraryController is created by Main.java and owns the main menu loop.
 * It creates one LibraryService (the shared data/business layer) and the
 * BorrowController sub-controller, then delegates to it based on the user's choice.
 *
 * Migration status:
 *   - Books (option 1)   → migrated to BookFXController   + BookTab.fxml
 *   - Members (option 2) → migrated to MemberFXController + MemberTab.fxml
 *   - Borrow (option 3)  → still active in the console (BorrowController)
 *   - Reservations (4)   → coming soon
 *   - Fines (5)          → coming soon
 *
 * Once all tabs are migrated, this class and Main.java will be retired.
 *
 * Used by: Main (console entry point)
 * MVC role: Top-level Controller
 */
public class LibraryController {

    private final LibraryService   service;
    private final MenuView         menuView;
    private final BorrowController borrowController;
    private final Scanner          scanner;

    public LibraryController() {
        this.service          = new LibraryService();
        this.menuView         = new MenuView();
        this.scanner          = new Scanner(System.in);
        this.borrowController = new BorrowController(service, scanner);
    }

    /** Starts the main menu loop. Runs until the user selects 0 (Exit). */
    public void start() {
        int choice = -1;
        while (choice != 0) {
            menuView.showMainMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                menuView.showError("Please enter a number.");
                continue;
            }
            switch (choice) {
                case 1  -> menuView.showMessage("Books — use the JavaFX GUI (run App.java).");
                case 2  -> menuView.showMessage("Members — use the JavaFX GUI (run App.java).");
                case 3  -> borrowController.handleMenu();
                case 4  -> menuView.showMessage("Reservations — coming soon.");
                case 5  -> menuView.showMessage("Fines — coming soon.");
                case 0  -> menuView.showMessage("Goodbye!");
                default -> menuView.showError("Invalid option. Try again.");
            }
        }
    }
}
