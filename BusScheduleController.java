import java.util.List;
import java.util.stream.Collectors;

public class BusScheduleController {
    //he controller uses the BusScheduleDAO to access the database 
    private BusScheduleDAO dao;
//When a controller is created, it also creates a BusScheduleDAO instance.
//This means all database operations are handled through this DAO
    public BusScheduleController() {
        dao = new BusScheduleDAO();
    }

    public List<Object[]> getAllSchedules() {
        return dao.getAllSchedules().stream()
                .map(BusSchedule::toObjectArray)
                .collect(Collectors.toList());
    }

    public List<Object[]> getSchedulesByRouteAndDate(String route, String date) {
        return dao.getSchedulesByRouteAndDate(route, date).stream()
                .map(BusSchedule::toObjectArray)
                .collect(Collectors.toList());
    }
//Calls DAO to get all schedules as list  Converts each BusSchedule object into Object[]
    public List<String> getAvailableRoutes() {
        return dao.getAvailableRoutes();
    }
//calls DAO method to filter by route and date.
    public List<String> getAvailableDates() {
        return dao.getAvailableDates();
    }

    public void addSchedule(Object[] scheduleData) {
        BusSchedule schedule = new BusSchedule(
            (String) scheduleData[0], // date
            (String) scheduleData[1], // route_id
            (String) scheduleData[2], // departure_time
            (String) scheduleData[3], // start_location
            (String) scheduleData[4], // end_location
            (String) scheduleData[5], // stops
            (String) scheduleData[6]  // bus_number
        );
        dao.addSchedule(schedule);
    }
}