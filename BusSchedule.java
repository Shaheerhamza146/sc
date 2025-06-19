//These private fields represent the columns in your bus_schedule database table:
public class BusSchedule {
    private String date;
    private String routeId;
    private String departureTime;
    private String startLocation;
    private String endLocation;
    private String stops;
    private String busNumber;
//Initializes all 7 fields when a new BusSchedule object is created.
//used when reading from the database (in DAO) or inserting a new schedule.

    public BusSchedule(String date, String routeId, String departureTime, 
                      String startLocation, String endLocation, 
                      String stops, String busNumber) {
        this.date = date;
        this.routeId = routeId;
        this.departureTime = departureTime;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.stops = stops;
        this.busNumber = busNumber;
    }
//extract values for database insertion.
    // Getters
    public String getDate() { return date; }
    public String getRouteId() { return routeId; }
    public String getDepartureTime() { return departureTime; }
    public String getStartLocation() { return startLocation; }
    public String getEndLocation() { return endLocation; }
    public String getStops() { return stops; }
    public String getBusNumber() { return busNumber; }
//Converts all the fields of the BusSchedule object into an Object[] array.
    public Object[] toObjectArray() {
        return new Object[]{date, routeId, departureTime, startLocation, endLocation, stops, busNumber};
    }
}