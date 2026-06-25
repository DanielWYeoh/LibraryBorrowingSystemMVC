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
 * Represents a single borrow transaction in the library system.
 *
 * A BorrowRecord is created when a Member borrows a LibraryItem.
 * It records who borrowed what, when it was borrowed, and when
 * (if ever) it was returned. Each record maps to one row in the
 * borrow_records table in the database.
 *
 * Attributes:
 *   - recordId   : unique identifier for this transaction (e.g. "REC-EA828CE5")
 *   - member     : the Member who borrowed the item
 *   - item       : the LibraryItem that was borrowed
 *   - borrowDate : the date the item was borrowed (ISO format: yyyy-MM-dd)
 *   - returnDate : the date the item was returned ("-" if not yet returned)
 *   - returned   : true if the item has been returned; false if still on loan
 */
public class BorrowRecord {

    private String      recordId;
    private Member      member;
    private LibraryItem item;
    private String      borrowDate;
    private String      returnDate;
    private boolean     returned;

    public BorrowRecord(String recordId, Member member, LibraryItem item, String borrowDate) {
        this.recordId   = recordId;
        this.member     = member;
        this.item       = item;
        this.borrowDate = borrowDate;
        this.returnDate = "-";
        this.returned   = false;
    }

    public String getInfo() {
        return "Record[" + recordId + "]"
                + " Member: "     + member.getName()
                + " | Item: "     + item.getTitle()
                + " | Borrowed: " + borrowDate
                + " | Returned: " + returnDate;
    }

    public String      getRecordId()   { return recordId; }
    public Member      getMember()     { return member; }
    public LibraryItem getItem()       { return item; }
    public String      getBorrowDate() { return borrowDate; }
    public String      getReturnDate() { return returnDate; }
    public boolean     isReturned()    { return returned; }

    public void setRecordId(String recordId)     { this.recordId   = recordId; }
    public void setMember(Member member)         { this.member     = member; }
    public void setItem(LibraryItem item)        { this.item       = item; }
    public void setBorrowDate(String borrowDate) { this.borrowDate = borrowDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public void setReturned(boolean returned)    { this.returned   = returned; }

    @Override
    public String toString() { return getInfo(); }
}
