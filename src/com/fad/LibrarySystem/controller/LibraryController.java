package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.LibraryService;
import com.fad.LibrarySystem.view.MenuView;
import java.util.Scanner;

/** Console entry point — Books tab is now handled by App.java (JavaFX). */
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
