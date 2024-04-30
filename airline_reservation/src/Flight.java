public class Flight implements Queryable{
    private int id; // primary key
    private String flightNum; 
    private String origin;
    private String destination;
    private String dptDate;
    private String arrDate;
    private int seatsLeft;
    
    public Flight(int id, String flightNum, String origin, String destination, String dptDate, String arrDate,
            int seatsLeft) {
        this.id = id;
        this.flightNum = flightNum;
        this.origin = origin;
        this.destination = destination;
        this.dptDate = dptDate;
        this.arrDate = arrDate;
        this.seatsLeft = seatsLeft;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNum() {
        return flightNum;
    }

    public void setFlightNum(String flightNum) {
        this.flightNum = flightNum;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDptDate() {
        return dptDate;
    }

    public void setDptDate(String dptDate) {
        this.dptDate = dptDate;
    }

    public String getArrDate() {
        return arrDate;
    }

    public void setArrDate(String arrDate) {
        this.arrDate = arrDate;
    }

    public int getSeatsLeft() {
        return seatsLeft;
    }

    public void setSeatsLeft(int seatsLeft) {
        this.seatsLeft = seatsLeft;
    }

    public void updateSeatsLeft(int updateSeatsNum){
        this.seatsLeft += updateSeatsNum;
    }

    public String toString(){
        return String.format("%2d | %6s\t| %3s  \t---> %-3s\t  | %s\t| %s\t| %3d", 
            id, flightNum, origin, destination, dptDate, arrDate, seatsLeft);
    }
}
