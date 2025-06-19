import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


 // Controller class for managing bus reservation operations.

public class BusReservationController {
    private BusReservationDAO reservationDAO;  // Data Access Object for database operations

    /**
     * Constructor for dependency injection
     * @param reservationDAO The DAO implementation to be used for database operations
     */
    public BusReservationController(BusReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    /**
     * Validates if the bus number contains only digits
     * @param busNo The bus number to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidBusNo(String busNo) {
        return busNo.matches("\\d+");
    }

    /**
     * Validates if passenger count is a positive number
     * @param count The passenger count to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidPassengerCount(String count) {
        return count.matches("\\d+");
    }

    /**
     * Creates a new reservation after validation
     * @param reservation The reservation object to create
     * @return Success/failure message with details
     */
    public String createReservation(Reservation reservation) {
        try {
            // Validate bus number format
            if (!isValidBusNo(reservation.getBusNo())) {
                return "Bus No must contain only numbers.";
            }

            // Validate passenger count
            if (reservation.getNoOfPassengers() <= 0) {
                return "Number of passengers must be greater than 0.";
            }

            // Attempt to create reservation in database
            boolean success = reservationDAO.createReservation(reservation);
            
            if (success) {
                return String.format("Reservation #%d successful! Bus No: %s, Route: %s", 
                    reservation.getId(), reservation.getBusNo(), reservation.getRoute());
            } else {
                return "Reservation failed!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Reservation failed due to database error.";
        }
    }

    /**
     * Retrieves all reservations from the database
     * @return List of reservations, or empty list on error
     */
    public List<Reservation> getAllReservations() {
        try {
            return reservationDAO.getAllReservations();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // Fail gracefully by returning empty list
        }
    }

    /**
     * Updates an existing reservation
     * @param reservation The reservation with updated values
     * @return true if update succeeded, false otherwise
     */
    public boolean updateReservation(Reservation reservation) {
        try {
            // Validate inputs before update
            if (!isValidBusNo(reservation.getBusNo())) {
                return false;
            }

            if (reservation.getNoOfPassengers() <= 0) {
                return false;
            }

            return reservationDAO.updateReservation(reservation);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cancels/deletes a reservation
     * @param id The ID of the reservation to cancel
     * @return true if deletion succeeded, false otherwise
     */
    public boolean cancelReservation(int id) {
        try {
            return reservationDAO.deleteReservation(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets current date in ISO format (YYYY-MM-DD)
     * @return Current date as string
     */
    public String getCurrentDate() {
        return LocalDate.now().toString();
    }

    /**
     * Gets current time without nanoseconds
     * @return Current time in HH:MM:SS format
     */
    public String getCurrentTime() {
        return LocalTime.now().withNano(0).toString();
    }
}