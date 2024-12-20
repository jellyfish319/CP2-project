import javax.swing.*;
import java.io.*;
import java.util.*;

public class Database {
    public Database() throws IOException, ClassNotFoundException {
        customerDatabase = new HashMap<>();
        showList = new ArrayList<>();
        // 유저 정보 초기화
        File userDirectory = new File("./src/data/user");
        if (userDirectory.exists() && userDirectory.isDirectory()) {
            File[] userFiles = userDirectory.listFiles((dir, name) -> name.endsWith(".txt"));
            if (userFiles != null) {
                for (File userFile : userFiles) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
                        String line = reader.readLine();
                        if (line != null) {
                            String[] data = line.split(" ");
                            if (data.length == 5) {
                                String id = data[0];
                                String password = data[1];
                                String name = data[2];
                                String email = data[3];
                                String phone = data[4];
                                Customer customer = new Customer(id, password, name, email, phone);
                                customerDatabase.put(id, customer);
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("Error reading user file: " + userFile.getName());
                    }
                }
            }
        } else {
            System.err.println("User directory not found: " + userDirectory.getPath());
        }

        // 영화 정보 초기화
        File movieDirectory = new File("./src/data/movie");
        if (movieDirectory.exists() && movieDirectory.isDirectory()) {
            File[] movieFiles = movieDirectory.listFiles((dir, name) -> name.endsWith(".txt"));
            if (movieFiles != null) {
                for (File movieFile : movieFiles) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(movieFile))) {
                        String line = reader.readLine();
                        if (line != null) {
                            String[] data = line.split(" ");
                            if (data.length >= 6) {
                                String name = data[0];
                                String director = data[1];
                                String genre = data[2];
                                int runtime = Integer.parseInt(data[3]);
                                String imagePath = data[4];
                                int totalSeats = Integer.parseInt(data[5]);
                                ImageIcon icon = new ImageIcon(imagePath);
                                Show show = new Show(name, director, genre, runtime, icon, totalSeats);
                                showList.add(show);
                            }
                        }
                    } catch (IOException | NumberFormatException e) {
                        System.err.println("Error reading movie file: " + movieFile.getName());
                    }
                }
            }
        } else {
            System.err.println("Movie directory not found: " + movieDirectory.getPath());
        }
    }

    public void SignUp(String id, String password, String name, String email, String phone) throws IOException {
        if (customerDatabase.containsKey(id)) {
            throw new IllegalArgumentException("ID가 이미 존재합니다.");
        }
        Customer customer = new Customer(id, password, name, email, phone);
        customerDatabase.put(id, customer);

        // 유저 정보를 개별 텍스트 파일로 저장
        File userFile = new File("./src/data/user/" + id + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
            writer.write(id + " " + password + " " + name + " " + email + " " + phone);
        }
    }

    public Customer LoadUser(String id) {
        return customerDatabase.get(id);
    }

    public boolean CheckID(String id) {
        return customerDatabase.containsKey(id);
    }

    public String LoginText(String id, String password) {
        if (customerDatabase.containsKey(id)) {
            Customer c = customerDatabase.get(id);
            if (c.getPassword().equals(password)) {
                return null;
            } else {
                return "비밀번호가 틀렸습니다.";
            }
        } else {
            return "없는 아이디입니다.";
        }
    }

    public boolean LoginCheck(String id, String password) {
        if (customerDatabase.containsKey(id)) {
            Customer c = customerDatabase.get(id);
            return c.getPassword().equals(password);
        }
        return false;
    }

    public void AddShow(String name, String director, String genre, int runtime, String path, int totalseats) throws IOException {
        ImageIcon icon = new ImageIcon(path);
        Show show = new Show(name, director, genre, runtime, icon, totalseats);
        showList.add(show);

        // 영화 정보를 개별 텍스트 파일로 저장
        File movieFile = new File("./src/data/movie/" + name.replaceAll("\\s+", "_") + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(movieFile))) {
            writer.write(name + " " + director + " " + genre + " " + runtime + " " + path + " " + totalseats);
        }
    }

    public Show ShowList(int i) {
        return showList.get(i);
    }

    public int ShowSize() {
        return showList.size();
    }

    private HashMap<String, Customer> customerDatabase;
    private List<Show> showList;
}

// 기존 파일 헤더를 덮어쓰지 않는 ObjectOutputStream 구현
class AppendableObjectOutputStream extends ObjectOutputStream {
    public AppendableObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // 기존 파일의 헤더를 덮어쓰지 않음
        reset();
    }
}
