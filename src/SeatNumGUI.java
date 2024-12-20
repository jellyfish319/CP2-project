import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SeatGUI extends JFrame{
    private int remainingSeatsToSelect;
    public int headcount;
    private int selectedSeatNum = 0;
    private ArrayList<Seat> selectedSeats = new ArrayList<>();

    public SeatGUI(Database database, Show show, Customer user) {

        show.initializeSeats();
        show.initializeResInfo();
        
        int ROWS = show.row;
        int COLS = show.col;
        JFrame frame = new JFrame("Seat Reservation");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Prompt user for the number of people
        String input = JOptionPane.showInputDialog(frame, "예약할 인원수를 입력하세요 (1 ~ " + show.remain + "):", "인원수 선택", JOptionPane.QUESTION_MESSAGE);
        try {
            remainingSeatsToSelect = Integer.parseInt(input);
            headcount = Integer.parseInt(input);
            if (remainingSeatsToSelect < 1 || remainingSeatsToSelect > ROWS * COLS) {
                throw new NumberFormatException();
            } else {
                new SeatFrame(database, show, user);
                dispose();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "유효한 숫자를 입력하세요 (1 ~ " + show.remain + ").", "입력 오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    class SeatFrame extends JFrame {

        public SeatFrame(Database database, Show show, Customer user) {

            int ROWS = show.row;
            int COLS = show.col;

            Seat[][] seats = show.seats;
            JPanel seatPanel = new JPanel();
            seatPanel.setLayout(new GridLayout(ROWS, COLS, 10, 10));

            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    char rowChar = (char) ('A' + i);
                    JButton seatButton = new JButton(rowChar + String.valueOf(j + 1));
                    int finalI = i;
                    int finalJ = j;
                    Seat selectedSeat = seats[finalI][finalJ];

                    if (selectedSeat.isReserved()) {
                        seatButton.setBackground(Color.BLUE);
                    }
                    else {
                        seatButton.setBackground(Color.GREEN);
                    }
                    seatPanel.add(seatButton);
                    seatButton.addActionListener(e -> {
                        if (selectedSeat.isReserved()) {
                            JOptionPane.showMessageDialog(null, "이미 예약된 좌석입니다.", "예매 불가", JOptionPane.WARNING_MESSAGE);
                        } else {
                            if (!selectedSeat.isSelected()) {
                                if (remainingSeatsToSelect > 0) {
                                    selectedSeat.select();
                                    selectedSeat.reserve();
                                    seatButton.setBackground(Color.RED);
                                    selectedSeats.add(selectedSeat);
                                    remainingSeatsToSelect--;
                                } else {
                                    JOptionPane.showMessageDialog(null, "더 이상 좌석을 선택할 수 없습니다.", "제한 초과", JOptionPane.WARNING_MESSAGE);
                                }
                            } else {
                                selectedSeat.cancelSeleection();
                                selectedSeat.cancelReservation();
                                seatButton.setBackground(Color.GREEN);
                                selectedSeats.remove(selectedSeat);
                                remainingSeatsToSelect++;
                            }}
                    });
                }
           }

            JButton closeButton = new JButton("결제");

            closeButton.addActionListener(e -> {
            StringBuilder reservedSeats = new StringBuilder("예약된 좌석:\n");

            // 예약된 좌석 정보를 확인
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    if (show.seats[i][j].isSelected()) {
                        reservedSeats.append(show.seats[i][j].rowNum).append(show.seats[i][j].colNum).append(" ");
                    }
                }
            }

            if (remainingSeatsToSelect == 0) {
                // 인원수만큼 좌석을 선택했을 경우 결제 창 열기
                new PaymentGUI(selectedSeats, headcount, show, user, selectedSeatNum);
                dispose(); // 현재 창 닫기
            } else {
                // 예약된 좌석이 부족할 경우 경고 메시지 표시
                JOptionPane.showMessageDialog(null, "인원수만큼 좌석을 선택하세요.", "결제 불가", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(closeButton);

        add(seatPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    }
}