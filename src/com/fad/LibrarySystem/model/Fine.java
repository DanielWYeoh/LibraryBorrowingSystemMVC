package com.fad.LibrarySystem.model;

public class Fine {

    private String      fineId;
    private Member      member;
    private LibraryItem item;
    private int         daysLate;
    private int         fineAmount;

    public Fine(String fineId, Member member, LibraryItem item, int daysLate) {
        this.fineId     = fineId;
        this.member     = member;
        this.item       = item;
        this.daysLate   = daysLate;
        this.fineAmount = daysLate * 2000;
    }

    public static int calculateFine(int daysLate) {
        return daysLate * 2000;
    }

    public String getInfo() {
        return "Fine for: " + member.getName()
                + " | Item: " + item.getTitle()
                + " | Days Late: " + daysLate
                + "\nFine: Rp" + fineAmount;
    }

    public String      getFineId()     { return fineId; }
    public int         getFineAmount() { return fineAmount; }
    public Member      getMember()     { return member; }
    public LibraryItem getItem()       { return item; }
    public int         getDaysLate()   { return daysLate; }

    /** @deprecated Use {@link #getFineId()} */
    @Deprecated
    public String getRecordId() { return fineId; }

    @Override
    public String toString() { return getInfo(); }
}
