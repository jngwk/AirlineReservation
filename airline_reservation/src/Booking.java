public class Booking implements Queryable{
    private String bookingId; // primary key
    private int flightId; // foriegn key
    private String memberId;
    
    public Booking(String bookingId, int flightId, String memberId) {
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.memberId = memberId;
    }

    public String getbookingId() {
        return bookingId;
    }

    public void setbookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String toString(){
        return String.format("%17s\t| %9d\t| %10s", bookingId, flightId, memberId);
    }
    
}
