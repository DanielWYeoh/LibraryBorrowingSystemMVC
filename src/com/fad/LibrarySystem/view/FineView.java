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

import com.fad.LibrarySystem.model.Fine;
import java.util.List;

/**
 * Console view for fine-related output in the MVC pattern.
 *
 * FineView is responsible for displaying fine information to System.out.
 * It contains no business logic — it only formats and prints data passed
 * to it by a controller.
 *
 * Used by: (console fine controller — not yet implemented)
 * Will be replaced by: FineFXController + FineTab.fxml (JavaFX)
 */
public class FineView {

    /** Prints the full info string of a single fine. */
    public void showFine(Fine fine) {
        System.out.println(fine.getInfo());
    }

    /** Prints all fines, or a "no fines" message if the list is empty. */
    public void showAllFines(List<Fine> fines) {
        if (fines.isEmpty()) {
            System.out.println("No fines recorded.");
            return;
        }
        System.out.println("----- Fines -----");
        for (Fine f : fines) {
            System.out.println(f.getInfo());
        }
    }

    /** Prints a confirmation that a fine was issued, including the amount in Rupiah. */
    public void showFineIssued(String memberName, int amount) {
        System.out.println("Fine issued for " + memberName + ": Rp" + amount);
    }
}
