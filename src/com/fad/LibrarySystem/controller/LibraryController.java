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
 * sub-controllers for Members and Borrow/Return, then delegates to them
 * based on the user's menu choice.
 *
 * Note on Books (option 1):
 *   The Books tab has been migrated to JavaFX (BookFXController + BookTab.fxml).
 *   Selecting option 1 in the console now shows a redirect message pointing to
 *   the GUI. Once all tabs are migrated, this class and Main.java will be retired.
 *
 * Used by: Main (console entry point)
 * MVC role: Top-level Controller
 */
public class LibraryController {

    private final LibraryService   service;
    private final MenuView         menuView;
    private final MemberController memberController;
    private final BorrowController borrowController;
    private final Scanner          scanner;

    public LibraryController() {
        this.service          = new LibraryService();
        this.menuView         = new MenuView();
        this.scanner          = new Scanner(System.in);
        this.memberController = new MemberController(service, scanner);
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
                case 2  -> memberController.handleMenu();
                case 3  -> borrowController.handleMenu();
                case 4  -> menuView.showMessage("Reservations — coming soon.");
                case 5  -> menuView.showMessage("Fines — coming soon.");
                case 0  -> menuView.showMessage("Goodbye!");
                default -> menuView.showError("Invalid option. Try again.");
            }
        }
    }
}
