package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.LibraryItem;
import com.fad.LibrarySystem.model.LibraryService;
import com.fad.LibrarySystem.model.Member;
import com.fad.LibrarySystem.view.BorrowView;
import java.util.Scanner;

public class BorrowController {

    private final LibraryService service;
    private final BorrowView     borrowView;
    private final Scanner        scanner;

    public BorrowController(LibraryService service, Scanner scanner) {
        this.service    = service;
        this.borrowView = new BorrowView();
        this.scanner    = scanner;
    }

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
                member.returnItem(item);
                borrowView.showError("Failed to record borrow: " + e.getMessage());
            }
        } else {
            borrowView.showBorrowLimitReached();
        }
    }

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
                member.borrowItem(item);
                borrowView.showError("Failed to record return: " + e.getMessage());
            }
        } else {
            borrowView.showNotFound("Item not in member's borrowed list.");
        }
    }

    private void viewAllRecords() {
        borrowView.showAllRecords(service.getBorrowRecords());
    }
}
