import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerGUI extends JFrame {
    private JTextField txtName, txtPhone;
    private JLabel lblSeatInfo;
    private JComboBox<String> couponComboBox;
    private JButton btnEditInfo, btnCancelReservation, btnDeleteAccount, btnManagePayment;

    public CustomerGUI(Customer customer) {
        // 창 기본 설정
        setTitle("Customer Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // 화면 중앙에 표시
        setLayout(new BorderLayout());
        setResizable(false);

        // 상단: 고객 정보 영역
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(5, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("고객 정보"));
        infoPanel.setBackground(new Color(240, 248, 255)); // 연한 파랑 배경

        txtName = new JTextField(customer.getName());
        txtName.setEditable(false); // 이름은 수정 불가
        txtPhone = new JTextField(customer.getPhone());
        txtPhone.setEditable(false); // 초기에는 수정 불가

        lblSeatInfo = new JLabel(
                customer.getMySeat() != null ? customer.getMySeat().toString() : "예약된 좌석 없음"
        );
        lblSeatInfo.setForeground(Color.DARK_GRAY);
        lblSeatInfo.setHorizontalAlignment(SwingConstants.CENTER);

        couponComboBox = new JComboBox<>();
        if (customer.getCoupons() == null) {
            couponComboBox.addItem("사용 가능한 쿠폰 없음");
        } else {
            for (String coupon : customer.getCoupons()) {
                couponComboBox.addItem(coupon);
            }
        }

        infoPanel.add(new JLabel("이름:"));
        infoPanel.add(txtName);
        infoPanel.add(new JLabel("연락처:"));
        infoPanel.add(txtPhone);
        infoPanel.add(new JLabel("예약 좌석:"));
        infoPanel.add(lblSeatInfo);
        infoPanel.add(new JLabel("보유 쿠폰:"));
        infoPanel.add(couponComboBox);

        // 하단: 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Color buttonColor = new Color(100, 149, 237); // 통일된 색상
        Font buttonFont = new Font("SansSerif", Font.BOLD, 14);

        btnEditInfo = createButton("정보 수정", buttonColor, buttonFont);
        btnEditInfo.addActionListener(e -> openEditInfoDialog(customer));

        btnCancelReservation = createButton("예약 취소", buttonColor, buttonFont);
        btnCancelReservation.addActionListener(e -> {
            customer.cancelSeatReservation();
            lblSeatInfo.setText("예약된 좌석 없음");
            JOptionPane.showMessageDialog(this, "좌석 예약이 취소되었습니다!");
        });

        btnDeleteAccount = createButton("계정 삭제", buttonColor, buttonFont);
        btnDeleteAccount.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this, "정말 계정을 삭제하시겠습니까?", "계정 삭제", JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "계정이 삭제되었습니다.");
                dispose(); // 창 닫기
            }
        });

        btnManagePayment = createButton("결제수단 관리", buttonColor, buttonFont);
        btnManagePayment.addActionListener(e -> JOptionPane.showMessageDialog(this, "결제수단 관리 기능은 현재 구현 중입니다!"));

        buttonPanel.add(btnEditInfo);
        buttonPanel.add(btnCancelReservation);
        buttonPanel.add(btnDeleteAccount);
        buttonPanel.add(btnManagePayment);
        JButton closeButton = createButton("닫기", buttonColor, buttonFont);
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        // 메인 컨테이너에 패널 추가
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createButton(String text, Color color, Font font) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(font);
        return button;
    }

    private void openEditInfoDialog(Customer customer) {
        JDialog editDialog = new JDialog(this, "정보 수정", true);
        editDialog.setSize(300, 250);
        editDialog.setLayout(new GridLayout(4, 2, 10, 10));
        editDialog.setLocationRelativeTo(this);

        // 기존 정보 표시 및 수정 가능한 텍스트 필드
        JTextField txtName = new JTextField(customer.getName());
        JTextField txtPassword = new JTextField(customer.getPassword());
        JTextField txtPhone = new JTextField(customer.getPhone());

        // 저장 버튼
        JButton btnSave = new JButton("저장");
        btnSave.addActionListener(e -> {
            customer.setName(txtName.getText());
            customer.setPassword(txtPassword.getText());
            customer.setPhone(txtPhone.getText());

            // 메인 화면의 이름과 연락처 반영
            txtName.setText(customer.getName());
            txtPhone.setText(customer.getPhone());

            JOptionPane.showMessageDialog(editDialog, "정보가 수정되었습니다!");
            editDialog.dispose();
        });

        // 취소 버튼
        JButton btnCancel = new JButton("취소");
        btnCancel.addActionListener(e -> editDialog.dispose());

        // 다이얼로그 구성
        editDialog.add(new JLabel("새 이름:"));
        editDialog.add(txtName);
        editDialog.add(new JLabel("새 비밀번호:"));
        editDialog.add(txtPassword);
        editDialog.add(new JLabel("새 연락처:"));
        editDialog.add(txtPhone);
        editDialog.add(btnSave);
        editDialog.add(btnCancel);

        editDialog.setVisible(true);
    }
}
