import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Payment {
    public Payment(int headcount) { // headcount를 매개변수로 추가
        // JFrame 생성
        JFrame frame = new JFrame("카드 결제");
        frame.setSize(300, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        // JLabel 생성 (결제 수단 선택 안내)
        JLabel label = new JLabel("결제 수단을 선택하세요:");
        label.setBounds(20, 20, 200, 25);
        frame.add(label);

        // JRadioButton 생성 (결제 수단 1과 2)
        JRadioButton paymentOption1 = new JRadioButton("결제 수단 1");
        paymentOption1.setBounds(20, 50, 200, 25);
        JRadioButton paymentOption2 = new JRadioButton("결제 수단 2");
        paymentOption2.setBounds(20, 80, 200, 25);

        // ButtonGroup으로 라디오 버튼 그룹화
        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(paymentOption1);
        paymentGroup.add(paymentOption2);

        frame.add(paymentOption1);
        frame.add(paymentOption2);

        // JLabel 생성 (쿠폰 선택 안내)
        JLabel couponLabel = new JLabel("쿠폰을 선택하세요:");
        couponLabel.setBounds(20, 120, 200, 25);
        frame.add(couponLabel);

        // JComboBox 생성 (쿠폰 옵션)
        String[] coupons = {"쿠폰 사용 안함", "20% 할인"};
        JComboBox<String> couponComboBox = new JComboBox<>(coupons);
        couponComboBox.setBounds(20, 150, 200, 25);
        frame.add(couponComboBox);

        // JLabel 생성 (좌석 가격 표시)
        int totalPrice = 15000 * headcount; // 기본 총 가격 계산
        JLabel priceLabel = new JLabel("결제해야 할 금액: " + totalPrice + "원");
        priceLabel.setBounds(20, 190, 250, 25);
        frame.add(priceLabel);

        // JComboBox 이벤트 리스너 (쿠폰 선택 시 가격 업데이트)
        couponComboBox.addActionListener(e -> {
            String selectedCoupon = (String) couponComboBox.getSelectedItem();
            if ("20% 할인".equals(selectedCoupon)) {
                int discountedPrice = (int) (15000 * headcount * 0.8); // 할인된 가격 계산
                priceLabel.setText("결제해야 할 금액: " + discountedPrice + "원");
            } else {
                priceLabel.setText("결제해야 할 금액: " + (15000 * headcount) + "원");
            }
        });

        // JButton 생성 (결제 버튼)
        JButton saveButton = new JButton("결제");
        saveButton.setBounds(100, 230, 100, 30);
        frame.add(saveButton);

        // JLabel 생성 (결과 표시용)
        JLabel resultLabel = new JLabel("");
        resultLabel.setBounds(20, 270, 250, 25);
        frame.add(resultLabel);

        // 결제 버튼 클릭 이벤트 처리
        saveButton.addActionListener(e -> {
            if (paymentOption1.isSelected() || paymentOption2.isSelected()) {
                String selectedCoupon = (String) couponComboBox.getSelectedItem();
                int finalPrice;
                if ("20% 할인".equals(selectedCoupon)) {
                    finalPrice = (int) (15000 * headcount * 0.8); // 할인 적용
                } else {
                    finalPrice = 15000 * headcount; // 기본 가격
                }

                String paymentMethod = paymentOption1.isSelected() ? "결제 수단 1" : "결제 수단 2";
                JOptionPane.showMessageDialog(frame,
                        "결제가 완료되었습니다!\n결제 금액: " + finalPrice + "원\n선택한 결제 수단: " + paymentMethod);
                frame.dispose(); // 결제 창 닫기
            } else {
                resultLabel.setText("결제 수단을 선택하세요.");
            }
        });

        // 프레임 표시
        frame.setVisible(true);
    }
}