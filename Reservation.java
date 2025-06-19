public class Reservation {
    private int id;
    private String busNo;
    private String route;
    private String reserveName;
    private String date;
    private String time;
    private String startLocation;
    private String endLocation;
    private String purpose;
    private int noOfPassengers;
    private String vehicleType;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getBusNo() { return busNo; }
    public void setBusNo(String busNo) { this.busNo = busNo; }
    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }
    public String getReserveName() { return reserveName; }
    public void setReserveName(String reserveName) { this.reserveName = reserveName; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }
    public String getEndLocation() { return endLocation; }
    public void setEndLocation(String endLocation) { this.endLocation = endLocation; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public int getNoOfPassengers() { return noOfPassengers; }
    public void setNoOfPassengers(int noOfPassengers) { this.noOfPassengers = noOfPassengers; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
}