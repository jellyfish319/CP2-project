import java.io.*;
import java.util.ArrayList;

class Customer implements Serializable {
    private String name;
    private String customerID;
    private String password;
    private String phone;
    private String email;
    private InfoSeat mySeat;
    private ArrayList<String> coupons;

    public Customer(String id, String password, String name, String email, String phoneNumber) {
        this.customerID = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public InfoSeat getMySeat() {
        return mySeat;
    }

    public void reserveSeat(InfoSeat seat) {
        this.mySeat = seat;
    }

    public void cancelSeatReservation() {
        this.mySeat = null;
    }

    public ArrayList<String> getCoupons() {
        return coupons;
    }

    public void addCoupon(String coupon) {
        coupons.add(coupon);
    }
}

class InfoSeat {
    private String seatNumber;

    public InfoSeat(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return seatNumber;
    }
}