import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DatabaseGUI extends JFrame {}

class LoginFrame extends JFrame {
    public LoginFrame(Database database) {
        JTextField id_tf = new JTextField(20);
        JPasswordField password_tf = new JPasswordField(20);
        JButton login_button = new JButton("로그인");
        JButton signup_button = new JButton("회원가입");

        setTitle("로그인");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 컴포넌트 간 간격 설정
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID 라벨 및 텍스트 필드
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        add(id_tf, gbc);

        // Password 라벨 및 텍스트 필드
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(password_tf, gbc);

        // 로그인 및 회원가입 버튼
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(login_button);
        buttonPanel.add(signup_button);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // 버튼 액션 리스너
        login_button.addActionListener(e -> {
            String id = id_tf.getText();
            String password = new String(password_tf.getPassword());
            String error_code = database.LoginText(id, password);

            if (id.equals("Admin") && password.equals("Admin")) {
                try {
                    AdminFrame admin = new AdminFrame(database);
                    dispose();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (!database.LoginCheck(id, password)) {
                new EventFrame(error_code);
            } else {
                Customer c = database.LoadUser(id);
                dispose();
                new MainFrame(c, database);
            }
        });

        signup_button.addActionListener(e -> new SignUpFrame(database));

        // 창 위치 중앙
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class EventFrame extends JFrame {
    public EventFrame(String error_code) {
        JLabel label = new JLabel(error_code, SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER); // 수직 정렬 중앙

        setLayout(new BorderLayout());
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(label, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class SignUpFrame extends JFrame {
    public SignUpFrame(Database database) {
        setTitle("회원가입");
        setSize(400, 300);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel id_lb = new JLabel("아이디:");
        JLabel password_lb = new JLabel("비밀번호:");
        JLabel name_lb = new JLabel("이름:");
        JLabel email_lb = new JLabel("이메일 주소:");
        JLabel phone_lb = new JLabel("전화번호:");
        JTextField id_tf = new JTextField(20);
        JPasswordField password_tf = new JPasswordField(20);
        JTextField name_tf = new JTextField(20);
        JTextField email_tf = new JTextField(20);
        JTextField phone_tf = new JTextField(20);
        JButton check_id = new JButton("ID 중복 확인");
        JButton signup = new JButton("회원가입");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(id_lb, gbc);
        gbc.gridx = 1;
        add(id_tf, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(password_lb, gbc);
        gbc.gridx = 1;
        add(password_tf, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(name_lb, gbc);
        gbc.gridx = 1;
        add(name_tf, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(email_lb, gbc);
        gbc.gridx = 1;
        add(email_tf, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(phone_lb, gbc);
        gbc.gridx = 1;
        add(phone_tf, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(check_id, gbc);
        gbc.gridx = 1;
        add(signup, gbc);

        final boolean[] id_check = {false};

        check_id.addActionListener(e -> {
            String id = id_tf.getText();

            if (id.trim().isEmpty()) {
                new EventFrame("아이디가 비어있습니다.");
                id_check[0] = false;
                return;
            }
            if (!database.CheckID(id)) {
                new EventFrame("사용 가능한 아이디입니다.");
                id_check[0] = true;
            } else {
                new EventFrame("이미 사용 중인 아이디입니다.");
                id_check[0] = false;
            }
        });

        signup.addActionListener(e -> {
            String id = id_tf.getText();
            String password = new String(password_tf.getPassword());
            String name = name_tf.getText();
            String email = email_tf.getText();
            String phone = phone_tf.getText();

            if (!id_check[0]) {
                new EventFrame("아이디 중복 확인을 먼저 해주세요.");
                return;
            } else if (id.trim().isEmpty() || password.trim().isEmpty() || name.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty()) {
                new EventFrame("모든 항목을 입력해주세요.");
                return;
            }
            try {
                database.SignUp(id, password, name, email, phone);
                new EventFrame("회원가입이 완료되었습니다!");
                dispose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class MainFrame extends JFrame {
    public MainFrame(Customer user, Database database) {
        setTitle("메인 화면");
        setSize(400, 200);
        setLayout(new FlowLayout());

        JButton reserve = new JButton("예약하기");
        JButton my_info = new JButton("내 정보 확인");

        reserve.addActionListener(e -> new ShowGUI(database));
        my_info.addActionListener(e -> new CustomerGUI(user));

        add(reserve);
        add(my_info);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class AdminFrame extends JFrame {
    public AdminFrame(Database database) throws IOException {
        // 텍스트 필드와 라벨 정의
        JTextField movie_tf = new JTextField(20);
        JTextField director_tf = new JTextField(20);
        JTextField genre_tf = new JTextField(20);
        JTextField runtime_tf = new JTextField(20);
        JTextField imagename_tf = new JTextField(20);
        JTextField totalseats_tf = new JTextField(20);

        JLabel movie_lb = new JLabel("영화 제목:");
        JLabel director_lb = new JLabel("감독명:");
        JLabel genre_lb = new JLabel("장르:");
        JLabel runtime_lb = new JLabel("상영 시간:");
        JLabel imagename_lb = new JLabel("영화 이미지 파일명:");
        JLabel totalseats_lb = new JLabel("총 좌석 수:");

        JButton add_movie = new JButton("영화 추가하기");
        JButton to_login = new JButton("로그인 화면으로");

        // 레이아웃 설정
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 컴포넌트 간 간격 설정
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 영화 제목
        gbc.gridx = 0; // 열
        gbc.gridy = 0; // 행
        add(movie_lb, gbc);
        gbc.gridx = 1;
        add(movie_tf, gbc);

        // 감독명
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(director_lb, gbc);
        gbc.gridx = 1;
        add(director_tf, gbc);

        // 장르
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(genre_lb, gbc);
        gbc.gridx = 1;
        add(genre_tf, gbc);

        // 상영 시간
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(runtime_lb, gbc);
        gbc.gridx = 1;
        add(runtime_tf, gbc);

        // 영화 이미지 파일명
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(imagename_lb, gbc);
        gbc.gridx = 1;
        add(imagename_tf, gbc);

        // 총 좌석 수
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(totalseats_lb, gbc);
        gbc.gridx = 1;
        add(totalseats_tf, gbc);

        // 영화 추가 버튼
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2; // 버튼을 두 칸 크기로 설정
        add(add_movie, gbc);

        // 로그인 화면 버튼
        gbc.gridy = 7;
        add(to_login, gbc);

        // 버튼 액션 리스너 설정
        add_movie.addActionListener(e -> {
            // 텍스트 필드가 비어있거나 공백만 있는지 검사
            if (isAnyFieldEmpty(movie_tf, director_tf, genre_tf, runtime_tf, imagename_tf, totalseats_tf)) {
                JOptionPane.showMessageDialog(this, "모든 필드를 채워주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return; // 실행 중단
            }

            try {
                database.AddShow(
                        movie_tf.getText(),
                        director_tf.getText(),
                        genre_tf.getText(),
                        Integer.parseInt(runtime_tf.getText()),
                        "./src/images/" + imagename_tf.getText(),
                        Integer.parseInt(totalseats_tf.getText())
                );
                JOptionPane.showMessageDialog(this, "영화가 성공적으로 추가되었습니다!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "영화를 추가하는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
            movie_tf.setText("");
            director_tf.setText("");
            genre_tf.setText("");
            runtime_tf.setText("");
            imagename_tf.setText("");
            totalseats_tf.setText("");
        });

        to_login.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame(database);
            loginFrame.setVisible(true);
            dispose();
        });

        // 창 설정
        setTitle("관리자 화면");
        setSize(400, 400); // 고정 크기 설정
        setResizable(false); // 창 크기 조절 불가능하게 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 창 위치
        setVisible(true);
    }

    // 텍스트 필드가 비어있거나 공백만 포함하는지 확인
    private boolean isAnyFieldEmpty(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) { // 공백 제거 후 비어있으면 true 반환
                return true;
            }
        }
        return false;
    }
}