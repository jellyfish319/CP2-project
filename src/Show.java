import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

// 공연정보 열람 및 공연 예약을 수행할 수 있는 클래스

public class Show implements Serializable {
    String name;
    int totalSeats;
    ArrayList<Seat> seats;
    String director;
    String genre;
    int runtime;
    ImageIcon icon;

    public Show(String name, String director, String genre, int runtime, ImageIcon icon, int totalSeats) {
        this.name = name;
        this.totalSeats = totalSeats;
        this.seats = new ArrayList<>();
        initializeSeats();
        this.director = director;
        this.runtime = runtime;
        this.genre = genre;
        this.icon = icon;
    }

    private void initializeSeats() {
        for (char row = 'A'; row < 'A' + totalSeats / 15; row++) {
            for (int col = 1; col <= 15; col++) {
                seats.add(new Seat(row, col, 'S')); // 모든 좌석은 기본적으로 S 타입
            }
        }
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> availableSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (!seat.isReserved()) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }

    public void printSeatAvailability() {
        for (Seat seat : seats) {
            System.out.println(seat.rowNum + seat.colNum + " 예약 여부: " + (seat.isReserved() ? "예약됨" : "사용 가능"));
        }
    }
}
