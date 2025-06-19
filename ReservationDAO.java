import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    public List<Reservation> fetchReservationsFromDB() {
        List<Reservation> reservations = new ArrayList<>();
        
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM reservations")) {
            
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setBusNo(rs.getString("bus_no"));
                r.setRoute(rs.getString("route"));
                r.setReserveName(rs.getString("passenger_name"));
                r.setDate(rs.getString("date"));
                r.setTime(rs.getString("time"));
                r.setStartLocation(rs.getString("start_location"));
                r.setEndLocation(rs.getString("end_location"));
                r.setPurpose(rs.getString("purpose"));
                r.setNoOfPassengers(rs.getInt("passenger_count"));
                r.setVehicleType(rs.getString("vehicle_type"));
                reservations.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reservations: " + e.getMessage());
        }
        return reservations;
    }
}