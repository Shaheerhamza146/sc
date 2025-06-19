import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class BusScheduleDAO {
    private Connection conn;

    public BusScheduleDAO() {
        try {
            // Connect to your existing database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/transport 2025", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Database connection failed: " + e.getMessage(), 
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
//Retrieves all bus schedules from the database.Returns a list of BusSchedule objects.
    public List<BusSchedule> getAllSchedules() {
        List<BusSchedule> schedules = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT date, route_id, departure_time, start_location, end_location, stops, bus_number FROM bus_schedule")) {
            
            while (rs.next()) {
                schedules.add(new BusSchedule(
                    rs.getString("date"),
                    rs.getString("route_id"),
                    rs.getString("departure_time"),
                    rs.getString("start_location"),
                    rs.getString("end_location"),
                    rs.getString("stops"),
                    rs.getString("bus_number")
                ));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return schedules;
    }
//Returns filtered schedules based on route and date.
    public List<BusSchedule> getSchedulesByRouteAndDate(String route, String date) {
        List<BusSchedule> schedules = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT date, route_id, departure_time, start_location, end_location, stops, bus_number " +
                "FROM bus_schedule WHERE 1=1");
            
            if (!route.equals("All Routes")) {
                sql.append(" AND route_id = ?");
            }
            if (!date.equals("All Dates")) {
                sql.append(" AND date = ?");
            }

            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            if (!route.equals("All Routes")) stmt.setString(paramIndex++, route);
            if (!date.equals("All Dates")) stmt.setString(paramIndex++, date);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                schedules.add(new BusSchedule(
                    rs.getString("date"),
                    rs.getString("route_id"),
                    rs.getString("departure_time"),
                    rs.getString("start_location"),
                    rs.getString("end_location"),
                    rs.getString("stops"),
                    rs.getString("bus_number")
                ));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return schedules;
    }
//Fetches distinct route IDs from the database. Adds "All Routes" at the beginning of the list for filter options in the UI.
    public List<String> getAvailableRoutes() {
        List<String> routes = new ArrayList<>();
        routes.add("All Routes");
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT route_id FROM bus_schedule")) {
            
            while (rs.next()) {
                routes.add(rs.getString("route_id"));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return routes;
    }
//Gets all distinct available dates from the database.
    public List<String> getAvailableDates() {
        List<String> dates = new ArrayList<>();
        dates.add("All Dates");
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT date FROM bus_schedule ORDER BY date")) {
            
            while (rs.next()) {
                dates.add(rs.getString("date"));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return dates;
    }
//Adds a new bus schedule record to the bus_schedule table.

//Takes a BusSchedule object and inserts its values into the DB.


    public void addSchedule(BusSchedule schedule) {
        try (PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO bus_schedule (date, route_id, departure_time, start_location, end_location, stops, bus_number) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            
            stmt.setString(1, schedule.getDate());
            stmt.setString(2, schedule.getRouteId());
            stmt.setString(3, schedule.getDepartureTime());
            stmt.setString(4, schedule.getStartLocation());
            stmt.setString(5, schedule.getEndLocation());
            stmt.setString(6, schedule.getStops());
            stmt.setString(7, schedule.getBusNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
//Common method for handling SQL errors.
    private void handleSQLException(SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Database error: " + e.getMessage(), 
            "SQL Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}