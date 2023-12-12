package group2.ptdacntt.footballviet.Models;

public class BookingStadium {
    private String bookingId;
    private String stadiumId;
    private String userId;
    private String startTime;
    private String endTime;

    public BookingStadium(String bookingId, String stadiumId, String userId, String startTime, String endTime) {
        this.bookingId = bookingId;
        this.stadiumId = stadiumId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public BookingStadium() {
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
