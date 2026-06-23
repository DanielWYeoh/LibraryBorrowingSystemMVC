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
 * Represents a hold placed by a member on a library item that is currently unavailable.
 *
 * A Reservation is created when a Member requests an item that is currently
 * borrowed by someone else. When the item becomes available, the reservation
 * indicates that this member should be notified first. Each Reservation maps
 * to one row in the reservations table in the database.
 *
 * Attributes:
 *   - reservationId   : unique identifier for this reservation (e.g. "RES-4F2A8B1C")
 *   - member          : the Member who placed the hold
 *   - item            : the LibraryItem being reserved
 *   - reservationDate : the date the hold was placed (ISO format: yyyy-MM-dd)
 *   - active          : true if the hold is still active; false if cancelled or fulfilled
 */
public class Reservation {

    private String      reservationId;
    private Member      member;
    private LibraryItem item;
    private String      reservationDate;
    private boolean     active;

    public Reservation(String reservationId, Member member, LibraryItem item, String reservationDate) {
        this.reservationId   = reservationId;
        this.member          = member;
        this.item            = item;
        this.reservationDate = reservationDate;
        this.active          = true;
    }

    /** Cancels this reservation. Once cancelled it cannot be reactivated. */
    public void cancelReservation() { this.active = false; }

    public String getInfo() {
        return "[Reservation] ID: " + reservationId
                + " | Member: " + member.getName()
                + " | Item: "   + item.getTitle()
                + " | Date: "   + reservationDate
                + " | Status: " + (active ? "Active" : "Cancelled");
    }

    public String      getReservationId()   { return reservationId; }
    public Member      getMember()          { return member; }
    public LibraryItem getItem()            { return item; }
    public String      getReservationDate() { return reservationDate; }
    public boolean     isActive()           { return active; }

    @Override
    public String toString() { return getInfo(); }
}
