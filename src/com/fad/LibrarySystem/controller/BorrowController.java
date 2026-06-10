package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.BorrowRecord;
import com.fad.LibrarySystem.model.LibraryItem;
import com.fad.LibrarySystem.model.Librarian;
import com.fad.LibrarySystem.model.Member;
import com.fad.LibrarySystem.view.BorrowView;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BorrowController {

    private Librarian  librarian;
    private BorrowView borrowView;
    private Scanner    scanner;

    public BorrowController(Librarian librarian, Scanner scanner) {
        this.librarian  = librarian;
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
                continue;
            }
            switch (choice) {
                case 1  -> borrowItem();
                case 2  -> returnItem();
                case 3  -> viewAllRecords();
                case 0  -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void borrowItem() {
        System.out.print("Member ID : "); String memberId = scanner.nextLine();
        System.out.print("Item ID   : "); String itemId   = scanner.nextLine();

        Member     member = librarian.findMemberById(memberId);
        LibraryItem item  = librarian.findItemById(itemId);

        if (member == null) {
            borrowView.showNotFound("Member " + memberId);          // View
            return;
        }
        if (item == null || !item.isAvailable()) {
            borrowView.showItemNotAvailable(itemId);                // View
            return;
        }

        boolean success = member.borrowItem(item);                  // Model
        if (success) {
            librarian.recordBorrow(member, item);                   // Model
            borrowView.showBorrowSuccess(member.getName(), item.getTitle()); // View
        } else {
            borrowView.showBorrowLimitReached();                    // View
        }
    }

    private void returnItem() {
        System.out.print("Member ID : "); String memberId = scanner.nextLine();
        System.out.print("Item ID   : "); String itemId   = scanner.nextLine();

        Member     member = librarian.findMemberById(memberId);
        LibraryItem item  = librarian.findItemById(itemId);

        if (member == null || item == null) {
            borrowView.showNotFound("Member or item not found.");   // View
            return;
        }

        boolean success = member.returnItem(item);                  // Model
        if (success) {
            librarian.recordReturn(member, item);                   // Model
            borrowView.showReturnSuccess(member.getName(), item.getTitle()); // View
        } else {
            borrowView.showNotFound("Item not in member's borrowed list.");
        }
    }

    private void viewAllRecords() {
        List<BorrowRecord> records = new ArrayList<>();
        for (int i = 0; i < librarian.getRecordCount(); i++) {
            records.add(librarian.getBorrowRecords()[i]);
        }
        borrowView.showAllRecords(records);                         // View
    }
}
