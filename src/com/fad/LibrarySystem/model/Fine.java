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
 * Represents a financial penalty issued to a member for returning an item late.
 *
 * A Fine is created when a borrowed item is returned past its due date.
 * The fine amount is calculated at creation time using a fixed rate of
 * Rp 2,000 per day late. Each Fine maps to one row in the fines table
 * in the database.
 *
 * The static {@link #calculateFine(int)} method is provided as a utility
 * so the fine amount can be previewed before creating a Fine object.
 *
 * Attributes:
 *   - fineId     : unique identifier for this fine (e.g. "FINE-3A9C1F2B")
 *   - member     : the Member who owes the fine
 *   - item       : the LibraryItem that was returned late
 *   - daysLate   : number of days past the due date
 *   - fineAmount : total penalty in Rupiah (daysLate × 2,000)
 */
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

    /**
     * Calculates the fine amount for a given number of days late.
     * Pure function — does not modify any state.
     *
     * @param daysLate number of days past the due date
     * @return fine amount in Rupiah
     */
    public static int calculateFine(int daysLate) {
        return daysLate * 2000;
    }

    public String getInfo() {
        return "Fine for: " + member.getName()
                + " | Item: "       + item.getTitle()
                + " | Days Late: "  + daysLate
                + "\nFine: Rp"      + fineAmount;
    }

    public String      getFineId()     { return fineId; }
    public int         getFineAmount() { return fineAmount; }
    public Member      getMember()     { return member; }
    public LibraryItem getItem()       { return item; }
    public int         getDaysLate()   { return daysLate; }

    /** @deprecated Use {@link #getFineId()} instead. */
    @Deprecated
    public String getRecordId() { return fineId; }

    @Override
    public String toString() { return getInfo(); }
}
