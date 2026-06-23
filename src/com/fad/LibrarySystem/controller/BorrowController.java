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

import com.fad.LibrarySystem.model.LibraryItem;
import com.fad.LibrarySystem.model.LibraryService;
import com.fad.LibrarySystem.model.Member;
import com.fad.LibrarySystem.view.BorrowView;
import java.util.Scanner;

/**
 * Console controller for borrow and return operations in the MVC pattern.
 *
 * BorrowController drives the Borrow/Return sub-menu in the console application.
 * It reads user input from the Scanner, delegates transactions to LibraryService
 * (the model), and asks BorrowView to print results (the view).
 *
 * Borrow flow:
 *   1. Read member ID and item ID from input
 *   2. Look up the Member and LibraryItem via the service
 *   3. Call member.borrowItem(item) to update in-memory state
 *   4. Call service.recordBorrow() to persist the transaction
 *   5. On DB failure, call member.returnItem() to roll back in-memory state
 *
 * Return flow mirrors borrow: member.returnItem() first, then service.recordReturn(),
 * with member.borrowItem() as the rollback if the DB write fails.
 *
 * Used by: LibraryController
 * MVC role: Controller
 */
public class BorrowController {

    private final LibraryService service;
    private final BorrowView     borrowView;
    private final Scanner        scanner;

    public BorrowController(LibraryService service, Scanner scanner) {
        this.service    = service;
        this.borrowView = new BorrowView();
        this.scanner    = scanner;
    }

    /** Displays the borrow/return sub-menu and handles input until the user chooses Back (0). */
    public void handleMenu() {
        int choice = -1;
        while (choice != 0) {
            borrowView.showBorrowMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                borrowView.showError("Please enter a valid number.");
                continue;
            }
            switch (choice) {
                case 1  -> borrowItem();
                case 2  -> returnItem();
                case 3  -> viewAllRecords();
                case 0  -> { return; }
                default -> borrowView.showError("Invalid option. Please try again.");
            }
        }
    }

    /** Prompts for member ID and item ID, then processes a borrow transaction. */
    private void borrowItem() {
        System.out.print("Member ID : "); String memberId = scanner.nextLine().trim();
        System.out.print("Item ID   : "); String itemId   = scanner.nextLine().trim();

        if (memberId.isEmpty() || itemId.isEmpty()) {
            borrowView.showError("Member ID and Item ID cannot be empty.");
            return;
        }

        Member      member = service.findMemberById(memberId);
        LibraryItem item   = service.findItemById(itemId);

        if (member == null) { borrowView.showNotFound("Member " + memberId); return; }
        if (item == null)   { borrowView.showNotFound("Item " + itemId);     return; }
        if (!item.isAvailable()) { borrowView.showItemNotAvailable(item.getTitle()); return; }

        boolean success = member.borrowItem(item);
        if (success) {
            try {
                service.recordBorrow(member, item);
                borrowView.showBorrowSuccess(member.getName(), item.getTitle());
            } catch (RuntimeException e) {
                member.returnItem(item); // roll back in-memory state if DB write failed
                borrowView.showError("Failed to record borrow: " + e.getMessage());
            }
        } else {
            borrowView.showBorrowLimitReached();
        }
    }

    /** Prompts for member ID and item ID, then processes a return transaction. */
    private void returnItem() {
        System.out.print("Member ID : "); String memberId = scanner.nextLine().trim();
        System.out.print("Item ID   : "); String itemId   = scanner.nextLine().trim();

        if (memberId.isEmpty() || itemId.isEmpty()) {
            borrowView.showError("Member ID and Item ID cannot be empty.");
            return;
        }

        Member      member = service.findMemberById(memberId);
        LibraryItem item   = service.findItemById(itemId);

        if (member == null) { borrowView.showNotFound("Member " + memberId); return; }
        if (item == null)   { borrowView.showNotFound("Item " + itemId);     return; }

        boolean success = member.returnItem(item);
        if (success) {
            try {
                service.recordReturn(member, item);
                borrowView.showReturnSuccess(member.getName(), item.getTitle());
            } catch (RuntimeException e) {
                member.borrowItem(item); // roll back in-memory state if DB write failed
                borrowView.showError("Failed to record return: " + e.getMessage());
            }
        } else {
            borrowView.showNotFound("Item not in member's borrowed list.");
        }
    }

    /** Fetches all borrow records from the service and passes them to the view. */
    private void viewAllRecords() {
        borrowView.showAllRecords(service.getBorrowRecords());
    }
}
