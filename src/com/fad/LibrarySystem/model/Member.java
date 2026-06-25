/**
 * @author      masjohncook
 * @version     0.0.1
 * @copyright   (C) Copyright 2026
 * @license     None
 * @maintainer  masjohncook
 * @email       mas.john.cook@gmail.com
 * @status      Development
 */
package com.fad.LibrarySystem.model;

/**
 * Represents a registered library member who can borrow and return items.
 * Inherits from Person — gains id and name.
 *
 * A Member tracks the items they currently have on loan via an internal
 * fixed-size array. The number of items a member may hold at one time
 * is controlled by getBorrowLimit(), which returns 3 by default and can
 * be overridden in a subclass if different rules apply.
 *
 * Inheritance:
 *   Member extends Person
 *   - Inherits : id (used as memberId), name
 *   - Adds     : borrowedItems[], borrowCount
 *   - Overrides: getInfo(), toString()
 *
 * Attributes:
 *   - borrowedItems : fixed-size array of items currently on loan
 *   - borrowCount   : number of items currently borrowed (0 to getBorrowLimit())
 *   - MAX_BORROW    : physical array capacity (5) — larger than the policy limit
 *                     to allow the DB loader to restore state without triggering
 *                     the borrow-limit check
 */
public class Member extends Person {

    protected LibraryItem[] borrowedItems;
    protected int borrowCount;
    private static final int MAX_BORROW = 5;

    /** Policy limit — how many items this member may borrow at once. */
    protected int getBorrowLimit() { return 3; }

    public Member(String memberId, String name) {
        super(memberId, name);
        this.borrowedItems = new LibraryItem[MAX_BORROW];
        this.borrowCount   = 0;
    }

    /**
     * Returns the three seed members inserted into the database on first startup.
     *
     * @return array of Member objects used for initial system data
     */
    public static Member[] getInitialMembers() {
        Member[] initial = new Member[3];
        initial[0] = new Member("M001", "Alice");
        initial[1] = new Member("M002", "Bob");
        initial[2] = new Member("M003", "Charlie");
        return initial;
    }

    /**
     * Attempts to borrow an item.
     * Fails silently and returns false if the item is unavailable or the
     * member has reached their borrow limit — the caller (controller/view)
     * is responsible for showing the user an appropriate message.
     *
     * @param item the item to borrow
     * @return true if the borrow succeeded; false otherwise
     */
    public boolean borrowItem(LibraryItem item) {
        if (!item.isAvailable()) return false;
        if (borrowCount >= getBorrowLimit()) return false;
        borrowedItems[borrowCount++] = item;
        item.setAvailable(false);
        return true;
    }

    /**
     * Attempts to return a borrowed item.
     * Searches the borrowedItems array by item ID. If found, marks the
     * item available again and removes it from the array using a swap
     * with the last element (order is not significant).
     *
     * @param item the item to return
     * @return true if the item was found and returned; false otherwise
     */
    public boolean returnItem(LibraryItem item) {
        for (int i = 0; i < borrowCount; i++) {
            if (borrowedItems[i] != null
                    && borrowedItems[i].getItemId().equals(item.getItemId())) {
                item.setAvailable(true);
                borrowedItems[i] = borrowedItems[--borrowCount];
                borrowedItems[borrowCount] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public String getInfo() {
        return "Member[" + id + "] " + name + " (borrowing: " + borrowCount + " item(s))";
    }

    @Override
    public String toString() { return getInfo(); }

    /**
     * Restores a borrowed item directly during database loading.
     * Bypasses the availability and limit checks that borrowItem() enforces,
     * because items loaded from the DB are already confirmed as on-loan.
     *
     * @param item the item to add to borrowedItems without side effects
     */
    void addBorrowedItem(LibraryItem item) {
        if (borrowCount < borrowedItems.length) borrowedItems[borrowCount++] = item;
    }

    public String       getMemberId()    { return id; }
    public LibraryItem[] getBorrowedItems() { return borrowedItems; }
    public int          getBorrowCount() { return borrowCount; }

    public void setMemberId(String memberId) { this.id = memberId; }
}
