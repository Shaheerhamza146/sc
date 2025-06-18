import java.sql.*;
import java.util.ArrayList;
import java.util.List;


 // Data Access Object (DAO) class for handling database operations related to bus reservations.
 
 
public class BusReservationDAO {

    /**
     * Creates a new reservation record in the database.
     * @param reservation The reservation object containing all required details
     * @return true if creation was successful, false otherwise
     * @throws SQLException if any database error occurs
     */
    public boolean createReservation(Reservation reservation) throws SQLException {
        // SQL query with parameterized values to prevent SQL injection
        String sql = "INSERT INTO reservations (bus_no, route, passenger_name, date, time, " +
                    "start_location, end_location, purpose, passenger_count, vehicle_type) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Set all parameters for the prepared statement
            stmt.setString(1, reservation.getBusNo());
            stmt.setString(2, reservation.getRoute());
            stmt.setString(3, reservation.getReserveName());
            stmt.setString(4, reservation.getDate());
            stmt.setString(5, reservation.getTime());
            stmt.setString(6, reservation.getStartLocation());
            stmt.setString(7, reservation.getEndLocation());
            stmt.setString(8, reservation.getPurpose());
            stmt.setInt(9, reservation.getNoOfPassengers());
            stmt.setString(10, reservation.getVehicleType());

            // Execute the insert operation
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;  // No rows were inserted
            }

            // Retrieve the auto-generated reservation ID
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    reservation.setId(rs.getInt(1));  // Set the generated ID back to the reservation object
                }
            }
            return true;
        }
    }

    /**
     * Retrieves all reservations from the database.
     * @return List of Reservation objects containing all reservation records
     * @throws SQLException if any database error occurs
     */
    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";  // Query to get all reservations

        try (Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            // Process each row in the result set
            while (rs.next()) {
                Reservation reservation = new Reservation();
                // Map database columns to reservation object properties
                reservation.setId(rs.getInt("id"));
                reservation.setBusNo(rs.getString("bus_no"));
                reservation.setRoute(rs.getString("route"));
                reservation.setReserveName(rs.getString("passenger_name"));
                reservation.setDate(rs.getString("date"));
                reservation.setTime(rs.getString("time"));
                reservation.setStartLocation(rs.getString("start_location"));
                reservation.setEndLocation(rs.getString("end_location"));
                reservation.setPurpose(rs.getString("purpose"));
                reservation.setNoOfPassengers(rs.getInt("passenger_count"));
                reservation.setVehicleType(rs.getString("vehicle_type"));
                
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    /**
     * Updates an existing reservation record in the database.
     * @param reservation The reservation object with updated values
     * @return true if update was successful, false otherwise
     * @throws SQLException if any database error occurs
     */
    public boolean updateReservation(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservations SET bus_no=?, route=?, passenger_name=?, date=?, time=?, " +
                    "start_location=?, end_location=?, purpose=?, passenger_count=?, vehicle_type=? " +
                    "WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set all parameters for the update query
            stmt.setString(1, reservation.getBusNo());
            stmt.setString(2, reservation.getRoute());
            stmt.setString(3, reservation.getReserveName());
            stmt.setString(4, reservation.getDate());
            stmt.setString(5, reservation.getTime());
            stmt.setString(6, reservation.getStartLocation());
            stmt.setString(7, reservation.getEndLocation());
            stmt.setString(8, reservation.getPurpose());
            stmt.setInt(9, reservation.getNoOfPassengers());
            stmt.setString(10, reservation.getVehicleType());
            stmt.setInt(11, reservation.getId());

            // Execute update and check if any rows were affected
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Deletes a reservation record from the database.
     * @param id The ID of the reservation to delete
     * @return true if deletion was successful, false otherwise
     * @throws SQLException if any database error occurs
     */
    public boolean deleteReservation(int id) throws SQLException {
        String sql = "DELETE FROM reservations WHERE id=?";  // Delete query with parameter

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);  // Set the ID parameter
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;  // Return true if at least one row was deleted
        }
    }
}