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

import com.fad.LibrarySystem.model.Member;
import java.util.List;

/**
 * Console view for member-related output in the MVC pattern.
 *
 * MemberView is responsible for displaying all member information to
 * System.out. It contains no business logic — it only formats and
 * prints data that the controller passes to it. This keeps the
 * display layer separate from data and decision-making.
 *
 * Used by: MemberController (console)
 * Will be replaced by: MemberFXController + MemberTab.fxml (JavaFX)
 */
public class MemberView {

    /** Prints the full info string of a single member. */
    public void showMember(Member member) {
        System.out.println(member.getInfo());
    }

    /** Prints a numbered list of all members, or a "no members" message if the list is empty. */
    public void showAllMembers(List<Member> members) {
        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }
        System.out.println("----- Members -----");
        for (int i = 0; i < members.size(); i++) {
            System.out.println((i + 1) + ". " + members.get(i).getInfo());
        }
    }

    /** Prints a confirmation that a member was successfully registered. */
    public void showMemberRegistered(String name) {
        System.out.println("Member registered: " + name);
    }

    /** Prints a "member not found" message. */
    public void showMemberNotFound() {
        System.out.println("Member not found.");
    }

    /** Prints a formatted error message. */
    public void showError(String message) {
        System.out.println("[ERROR] " + message);
    }

    /** Prints the member sub-menu options. */
    public void showMemberMenu() {
        System.out.println("\n--- Member Menu ---");
        System.out.println("1. Register Member");
        System.out.println("2. View All Members");
        System.out.println("0. Back");
        System.out.print("Choose: ");
    }
}
