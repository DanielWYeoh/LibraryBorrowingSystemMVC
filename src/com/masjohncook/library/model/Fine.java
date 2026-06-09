package com.masjohncook.library.model;

public class Fine {

    private String recordId;
    private Member member;
    private LibraryItem item;
    private int daysLate;
    private int fineAmount;

    public Fine(String recordId, Member member, LibraryItem item, int daysLate) {
        this.recordId   = recordId;
        this.member     = member;
        this.item       = item;
        this.daysLate   = daysLate;
        this.fineAmount = calculateFine(daysLate);
    }

    public int calculateFine(int daysLate) {
        this.fineAmount = daysLate * 2000;
        return this.fineAmount;
    }

    public String getInfo() {
        return "Fine for: " + member.getName()
                + " | Item: " + item.getTitle()
                + " | Days Late: " + daysLate
                + "\nFine: Rp" + fineAmount;
    }

    public int    getFineAmount() { return fineAmount; }
    public Member getMember()     { return member; }
    public String getRecordId()   { return recordId; }
    public int    getDaysLate()   { return daysLate; }

    public String toString() { return getInfo(); }
}
