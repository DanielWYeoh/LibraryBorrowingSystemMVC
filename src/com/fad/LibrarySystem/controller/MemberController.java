package com.fad.LibrarySystem.controller;

import com.fad.LibrarySystem.model.Member;
import com.fad.LibrarySystem.model.Librarian;
import com.fad.LibrarySystem.view.MemberView;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemberController {

    private Librarian   librarian;
    private MemberView  memberView;
    private Scanner     scanner;

    public MemberController(Librarian librarian, Scanner scanner) {
        this.librarian  = librarian;
        this.memberView = new MemberView();
        this.scanner    = scanner;
    }

    public void handleMenu() {
        int choice = -1;
        while (choice != 0) {
            memberView.showMemberMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                continue;
            }
            switch (choice) {
                case 1  -> registerMember();
                case 2  -> viewAllMembers();
                case 0  -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void registerMember() {
        System.out.print("Member ID : "); String id   = scanner.nextLine();
        System.out.print("Name      : "); String name = scanner.nextLine();

        Member member = librarian.registerMember(id, name);     // Model
        if (member != null) memberView.showMemberRegistered(name); // View
        else memberView.showMemberNotFound();
    }

    private void viewAllMembers() {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < librarian.getMemberCount(); i++) {
            members.add(librarian.getMembers()[i]);
        }
        memberView.showAllMembers(members);                      // View
    }
}
