/**
 * @author      masjohncook
 * @version     0.0.1
 * @copyright   (C) Copyright 2026
 * @license     None
 * @maintainer  masjohncook
 * @email       mas.john.cook@gmail.com
 * @status      Development
 */
package com.fad.LibrarySystem.view;

/**
 * Console view for the main menu of the library system.
 *
 * MenuView is responsible for printing the top-level navigation menu
 * and any general-purpose info or error messages to System.out.
 * It is used by LibraryController to drive the console application loop.
 *
 * Used by: LibraryController (console)
 */
public class MenuView {

    /** Prints the main menu with all available options. */
    public void showMainMenu() {
        System.out.println("\n===== Library System =====");
        System.out.println("1. Manage Books");
        System.out.println("2. Manage Members");
        System.out.println("3. Borrow / Return Item");
        System.out.println("4. Reservations");
        System.out.println("5. Fines");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    /** Prints a general information message prefixed with [INFO]. */
    public void showMessage(String message) {
        System.out.println("[INFO] " + message);
    }

    /** Prints an error message prefixed with [ERROR]. */
    public void showError(String error) {
        System.out.println("[ERROR] " + error);
    }

    /** Prints a horizontal separator line. */
    public void showSeparator() {
        System.out.println("---------------------------");
    }
}
