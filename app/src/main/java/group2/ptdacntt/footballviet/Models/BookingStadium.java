package group2.ptdacntt.footballviet.Models;

public class BookingStadium {
    private String bookingId;
    private String stadiumId;
    private String userId;
    private String timeSlot;
    private String dateBooking;
    private boolean active;

    public BookingStadium() {
    }

    public BookingStadium(String bookingId, String stadiumId, String userId, String timeSlot, String dateBooking, boolean active) {
        this.bookingId = bookingId;
        this.stadiumId = stadiumId;
        this.userId = userId;
        this.timeSlot = timeSlot;
        this.dateBooking = dateBooking;
        this.active = active;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getStadiumId() {
        return stadiumId;
    }

    public void setStadiumId(String stadiumId) {
        this.stadiumId = stadiumId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getDateBooking() {
        return dateBooking;
    }

    public void setDateBooking(String dateBooking) {
        this.dateBooking = dateBooking;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
