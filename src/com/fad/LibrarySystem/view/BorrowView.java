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

import com.fad.LibrarySystem.model.BorrowRecord;
import java.util.List;

/**
 * Console view for borrow and return operations in the MVC pattern.
 *
 * BorrowView is responsible for displaying all borrow/return feedback
 * to System.out. It contains no business logic — it only formats and
 * prints results that BorrowController passes to it.
 *
 * Used by: BorrowController (console)
 * Will be replaced by: BorrowFXController + BorrowTab.fxml (JavaFX)
 */
public class BorrowView {

    /** Prints the info string of a single borrow record. */
    public void showRecord(BorrowRecord record) {
        System.out.println(record.getInfo());
    }

    /** Prints all borrow records, or a "no records" message if the list is empty. */
    public void showAllRecords(List<BorrowRecord> records) {
        if (records.isEmpty()) {
            System.out.println("No borrow records.");
            return;
        }
        System.out.println("----- Borrow Records -----");
        for (BorrowRecord r : records) {
            System.out.println(r.getInfo());
        }
    }

    /** Prints a success message after a borrow operation. */
    public void showBorrowSuccess(String memberName, String itemTitle) {
        System.out.println(memberName + " successfully borrowed: " + itemTitle);
    }

    /** Prints a success message after a return operation. */
    public void showReturnSuccess(String memberName, String itemTitle) {
        System.out.println(memberName + " successfully returned: " + itemTitle);
    }

    /** Prints a message when a member has reached their borrow limit. */
    public void showBorrowLimitReached() {
        System.out.println("Borrow limit reached. Cannot borrow more items.");
    }

    /** Prints a message when the requested item is not available. */
    public void showItemNotAvailable(String title) {
        System.out.println("Item not available: " + title);
    }

    /** Prints a "not found" message with the given detail string. */
    public void showNotFound(String message) {
        System.out.println("Not found: " + message);
    }

    /** Prints a formatted error message. */
    public void showError(String message) {
        System.out.println("[ERROR] " + message);
    }

    /** Prints the borrow/return sub-menu options. */
    public void showBorrowMenu() {
        System.out.println("\n--- Borrow / Return Menu ---");
        System.out.println("1. Borrow Item");
        System.out.println("2. Return Item");
        System.out.println("3. View All Records");
        System.out.println("0. Back");
        System.out.print("Choose: ");
    }
}
