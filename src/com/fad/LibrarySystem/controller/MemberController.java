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
import com.fad.LibrarySystem.model.Member;
import com.fad.LibrarySystem.view.MemberView;
import java.util.Scanner;

/**
 * Console controller for member management in the MVC pattern.
 *
 * MemberController drives the Member sub-menu in the console application.
 * It reads user input from the Scanner, delegates operations to LibraryService
 * (the model), and asks MemberView to print results (the view).
 *
 * Responsibilities:
 *   - Display the member sub-menu and read the user's choice
 *   - Register a new member (prompt for ID and name, call service, show result)
 *   - View all members (fetch from service, pass to view)
 *
 * Used by: LibraryController
 * MVC role: Controller
 */
public class MemberController {

    private final LibraryService service;
    private final MemberView     memberView;
    private final Scanner        scanner;

    public MemberController(LibraryService service, Scanner scanner) {
        this.service    = service;
        this.memberView = new MemberView();
        this.scanner    = scanner;
    }

    /** Displays the member sub-menu and handles input until the user chooses Back (0). */
    public void handleMenu() {
        int choice = -1;
        while (choice != 0) {
            memberView.showMemberMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                memberView.showError("Please enter a valid number.");
                continue;
            }
            switch (choice) {
                case 1  -> registerMember();
                case 2  -> viewAllMembers();
                case 0  -> { return; }
                default -> memberView.showError("Invalid option. Please try again.");
            }
        }
    }

    /** Prompts for member ID and name, then registers a new member via the service. */
    private void registerMember() {
        System.out.print("Member ID : "); String id   = scanner.nextLine().trim();
        System.out.print("Name      : "); String name = scanner.nextLine().trim();

        if (id.isEmpty() || name.isEmpty()) {
            memberView.showError("Member ID and Name cannot be empty.");
            return;
        }
        try {
            Member member = service.registerMember(id, name);
            if (member != null) memberView.showMemberRegistered(name);
            else memberView.showError("Could not register member (duplicate ID or member list full).");
        } catch (RuntimeException e) {
            memberView.showError("Could not save member: " + e.getMessage());
        }
    }

    /** Fetches all members from the service and passes them to the view for display. */
    private void viewAllMembers() {
        memberView.showAllMembers(service.getMembers());
    }
}
