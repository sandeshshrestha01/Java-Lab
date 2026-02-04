import javax.swing.*;
import java.awt.*;

public class StudentLogin extends JFrame {

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

        JTextField userField = new JTextField();
        userField.setBounds(150, 80, 180, 25);
        panel.add(userField);

        JLabel password = new JLabel("Password:");
        password.setBounds(50, 120, 100, 25);
        panel.add(password);

        JPasswordField passField = new JPasswordField();
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

            if (usernameText.isEmpty() || passwordText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Username or Password cannot be empty",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                // TODO: Connect to database and validate student
                JOptionPane.showMessageDialog(this,
                        "Login successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        setVisible(true);
    }
}
