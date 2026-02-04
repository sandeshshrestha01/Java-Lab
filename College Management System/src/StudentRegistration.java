import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentRegistration extends JFrame {

    // Declare courseBox at class level
    private JComboBox<String> courseBox;

    public StudentRegistration() {

        setTitle("Student Registration");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JPanel panel = new JPanel(null);
        panel.setBounds(0, 0, 600, 500);
        panel.setBackground(new Color(245, 250, 255));
        add(panel);

        JLabel title = new JLabel("Student Registration Form");
        title.setBounds(140, 15, 350, 30);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(0, 102, 204));
        panel.add(title);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(50, 70, 100, 25);
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(180, 70, 300, 25);
        panel.add(nameField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(50, 105, 100, 25);
        panel.add(addressLabel);

        JTextField addressField = new JTextField();
        addressField.setBounds(180, 105, 300, 25);
        panel.add(addressField);

        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setBounds(50, 140, 120, 25);
        panel.add(dobLabel);

        JTextField dobField = new JTextField("YYYY-MM-DD");
        dobField.setBounds(180, 140, 300, 25);
        panel.add(dobField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(50, 175, 100, 25);
        panel.add(genderLabel);

        JRadioButton male = new JRadioButton("Male");
        JRadioButton female = new JRadioButton("Female");
        JRadioButton other = new JRadioButton("Other");

        male.setBounds(180, 175, 70, 25);
        female.setBounds(260, 175, 80, 25);
        other.setBounds(350, 175, 70, 25);

        ButtonGroup bg = new ButtonGroup();
        bg.add(male);
        bg.add(female);
        bg.add(other);

        panel.add(male);
        panel.add(female);
        panel.add(other);

        JLabel nationLabel = new JLabel("Nationality:");
        nationLabel.setBounds(50, 210, 100, 25);
        panel.add(nationLabel);

        JTextField nationField = new JTextField();
        nationField.setBounds(180, 210, 300, 25);
        panel.add(nationField);

        // ===== COURSE DROPDOWN =====
        JLabel courseLabel = new JLabel("Course:");
        courseLabel.setBounds(50, 245, 100, 25);
        panel.add(courseLabel);

        courseBox = new JComboBox<>();
        courseBox.setBounds(180, 245, 300, 25);
        panel.add(courseBox);

        // Load courses from database
        loadCourses();

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 280, 100, 25);
        panel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(180, 280, 300, 25);
        panel.add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 315, 100, 25);
        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(180, 315, 300, 25);
        panel.add(passField);

        JButton submitBtn = new JButton("Submit");
        submitBtn.setBounds(240, 370, 120, 35);
        panel.add(submitBtn);

        // Add data in database on submit
        submitBtn.addActionListener(e -> {

            String name = nameField.getText();
            String address = addressField.getText();
            String dob = dobField.getText();
            String nationality = nationField.getText();
            String course = courseBox.getSelectedItem().toString();
            String username = userField.getText();
            String password = String.valueOf(passField.getPassword());

            String gender =
                    male.isSelected() ? "Male" :
                    female.isSelected() ? "Female" :
                    other.isSelected() ? "Other" : "";

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields");
                return;
            }

            try (Connection con = DBConfig.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO student (full_name, address, date_of_birth, gender, nationality, course, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

                ps.setString(1, name);
                ps.setString(2, address);
                ps.setDate(3, Date.valueOf(dob));
                ps.setString(4, gender);
                ps.setString(5, nationality);
                ps.setString(6, course);
                ps.setString(7, username);
                ps.setString(8, password); // hash later

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Student Registered Successfully!");
                dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error!");
            }
        });

        setVisible(true);
    }

    // ================= LOAD COURSES FROM DATABASE =================
    private void loadCourses() {
        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT course_name FROM courses");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                courseBox.addItem(rs.getString("course_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load courses");
        }
    }
}
