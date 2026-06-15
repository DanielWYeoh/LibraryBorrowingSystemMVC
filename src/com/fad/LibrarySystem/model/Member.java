package com.fad.LibrarySystem.model;

public class Member extends Person {

    protected LibraryItem[] borrowedItems;
    protected int borrowCount;
    private static final int MAX_BORROW = 5;

    protected int getBorrowLimit() { return 3; }

    public Member(String memberId, String name) {
        super(memberId, name);
        this.borrowedItems = new LibraryItem[MAX_BORROW];
        this.borrowCount   = 0;
    }

    public static Member[] getInitialMembers() {
        Member[] initial = new Member[3];
        initial[0] = new Member("M001", "Alice");
        initial[1] = new Member("M002", "Bob");
        initial[2] = new Member("M003", "Charlie");
        return initial;
    }

    // Returns true/false — View layer handles the output message
    public boolean borrowItem(LibraryItem item) {
        if (!item.isAvailable()) return false;
        if (borrowCount >= getBorrowLimit()) return false;
        borrowedItems[borrowCount++] = item;
        item.setAvailable(false);
        return true;
    }

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

    public java.util.List<LibraryItem> searchItem(LibraryItem[] catalog, int catalogSize, String keyword) {
        java.util.List<LibraryItem> results = new java.util.ArrayList<>();
        for (int i = 0; i < catalogSize; i++)
            if (catalog[i] != null
                    && catalog[i].getTitle().toLowerCase().contains(keyword.toLowerCase()))
                results.add(catalog[i]);
        return results;
    }

    public java.util.List<LibraryItem> searchItem(LibraryItem[] catalog, int catalogSize) {
        java.util.List<LibraryItem> results = new java.util.ArrayList<>();
        for (int i = 0; i < catalogSize; i++)
            if (catalog[i] != null && catalog[i].isAvailable())
                results.add(catalog[i]);
        return results;
    }

    @Override
    public String getInfo() {
        return "Member[" + id + "] " + name + " (borrowing: " + borrowCount + " item(s))";
    }

    @Override
    public String toString() { return getInfo(); }

    // Used only during DB loading to restore borrowed state without availability check
    void addBorrowedItem(LibraryItem item) {
        if (borrowCount < borrowedItems.length) borrowedItems[borrowCount++] = item;
    }

    public String getMemberId()             { return id; }
    public LibraryItem[] getBorrowedItems() { return borrowedItems; }
    public int getBorrowCount()             { return borrowCount; }

    public void setMemberId(String memberId) { this.id = memberId; }
}
