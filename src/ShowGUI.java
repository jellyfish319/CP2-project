import javax.swing.*;
import java.awt.*;

public class ShowGUI extends JFrame{
    static JPanel contentPane;

    public ShowGUI(Database database) {
        setTitle("영화 예매 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600); // 화면 크기 조정
        contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);

        JLabel titleLabel = new JLabel("현재 상영중인 영화");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints titleGbc = new GridBagConstraints();
        titleGbc.gridx = 0;
        titleGbc.gridy = 0;
        titleGbc.gridwidth = 3;
        titleGbc.insets = new Insets(10, 10, 20, 10);
        contentPane.add(titleLabel, titleGbc);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0 ; i < database.ShowSize() ; i++) {
            addMovie(gbc, database.ShowList(i), i+1);
        }

        setVisible(true);
    }

    private void addMovie(GridBagConstraints gbc, Show show, int position) {
        JLabel movieIcon = new JLabel();
        movieIcon.setIcon(show.icon);
        gbc.gridx = 0;
        gbc.gridy = position;
        contentPane.add(movieIcon, gbc);

        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(createStyledLabel("영화 제목: " + show.name));
        infoPanel.add(createStyledLabel("감독: " + show.director));
        infoPanel.add(createStyledLabel("장르: " + show.genre));
        infoPanel.add(createStyledLabel("상영 시간: " + show.runtime + "분"));
        gbc.gridx = 1;
        gbc.gridy = position;
        contentPane.add(infoPanel, gbc);

        JButton buyButton = new JButton("예매하기");
        buyButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        buyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 2;
        gbc.gridy = position;
        contentPane.add(buyButton, gbc);

        buyButton.addActionListener(e -> {
            contentPane.removeAll();
            contentPane.revalidate();
            contentPane.repaint();
            new NextFrame(show.name, show);
        });
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18)); // 글씨 크기 키움
        return label;
    }

    private class NextFrame extends JFrame {
        public NextFrame(String movieName, Show show) {
            setTitle(movieName + " 예매");
            setSize(600, 600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());
            String[] region = {"서울", "경기", "인천", "강원", "대전/충청", "대구", "부산/울산", "경상", "광주/전라/제주"};
            String[] date = {"12월 9일 (월)", "12월 10일 (화)", "12월 11일 (수)", "12월 12일 (목)", "12월 13일 (금)", "12월 14일 (토)", "12월 15일 (토)"};
            String[] theater = {"2D(자막) 1관 B3층", "2D(자막) 3관 B2층", "2D(자막) 7관 3층", "2D(더빙) 8관 3층"};
            String[] time = {"10:20", "11:50", "13:00", "14:15", "16:40", "17:45", "19:30", "21:00"};

            JLabel label = new JLabel(movieName);
            label.setFont(new Font("SansSerif", Font.BOLD, 20));
            label.setHorizontalAlignment(JLabel.CENTER);
            add(label, BorderLayout.NORTH);

            JPanel reservation = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridx = 0;
            gbc.gridy = 0;
            reservation.add(new JLabel("지역"), gbc);

            gbc.gridx = 1;
            reservation.add(new JComboBox(region), gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            reservation.add(new JLabel("날짜"), gbc);

            gbc.gridx = 1;
            reservation.add(new JComboBox(date), gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            reservation.add(new JLabel("극장"), gbc);

            gbc.gridx = 1;
            reservation.add(new JComboBox(theater), gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            reservation.add(new JLabel("시간"), gbc);

            gbc.gridx = 1;
            reservation.add(new JComboBox(time), gbc);

            add(reservation, BorderLayout.CENTER);

            JPanel buttonpanel = new JPanel(new FlowLayout());
            add(buttonpanel, BorderLayout.SOUTH);
            JButton backButton = new JButton("뒤로 가기");
            backButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
            backButton.addActionListener(e -> {
                dispose();
                ShowGUI.contentPane.removeAll();
                ShowGUI.contentPane.revalidate();
                ShowGUI.contentPane.repaint();
                ;
            });
            JButton seatButton = new JButton("좌석 선택하기");
            seatButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
            seatButton.addActionListener(e -> {
                new SeatReservationGUI();
                dispose();
            });

            buttonpanel.add(backButton);
            buttonpanel.add(seatButton);
            setVisible(true);
        }
    }
}
