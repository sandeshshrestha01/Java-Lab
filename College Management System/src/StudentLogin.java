import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentLogin extends JFrame {

    JTextField userField;
    JPasswordField passField;

    public StudentLogin() {

        setTitle("Student Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 255));
        add(panel);

        JLabel title = new JLabel("Student Login");
        title.setBounds(130, 20, 200, 30);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(0, 102, 204));
        panel.add(title);

        JLabel username = new JLabel("Username:");
        username.setBounds(50, 80, 100, 25);
        panel.add(username);

        userField = new JTextField();
        userField.setBounds(150, 80, 180, 25);
        panel.add(userField);

        JLabel password = new JLabel("Password:");
        password.setBounds(50, 120, 100, 25);
        panel.add(password);

        passField = new JPasswordField();
        passField.setBounds(150, 120, 180, 25);
        panel.add(passField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 170, 100, 30);
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        panel.add(loginButton);

        // ===== LOGIN BUTTON ACTION =====
        loginButton.addActionListener(e -> {

            String usernameText = userField.getText();
            String passwordText = new String(passField.getPassword());

            StudentUser studentUser = getAuthenticatedStudent(usernameText, passwordText);

            if (studentUser != null) {

                JOptionPane.showMessageDialog(this,
                        "Welcome " + studentUser.full_name,
                        "Login Success",
                        JOptionPane.INFORMATION_MESSAGE);

                //  OPEN STUDENT DASHBOARD
                new StudentDashboard().initialize(studentUser);

                dispose();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid Username or Password",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    // ================= DATABASE METHOD =================
    private StudentUser getAuthenticatedStudent(String username, String password) {

        StudentUser studentUser = null;

        String sql = "SELECT * FROM student WHERE BINARY username = ? AND BINARY password = ?";

        try (Connection con = DBConfig.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                studentUser = new StudentUser();
                studentUser.student_id = rs.getString("student_id");
                studentUser.full_name = rs.getString("full_name");
                studentUser.course = rs.getString("course");
                studentUser.username = rs.getString("username");
                studentUser.password = rs.getString("password");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentUser;
    }

    public static void main(String[] args) {
        new StudentLogin();
    }
}
