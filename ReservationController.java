import java.util.List;

public class ReservationController {
    private ReservationDAO dao;

    public ReservationController() {
        this.dao = new ReservationDAO();
    }

    public List<Reservation> getAllReservations() {
        return dao.fetchReservationsFromDB();
    }
}