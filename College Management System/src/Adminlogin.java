import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Adminlogin extends JFrame {

    JTextField tfUser;
    JPasswordField pfPassword;

    public Adminlogin() {

        setTitle("Admin Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 255));
        add(panel);

        // ===== TITLE =====
        JLabel title = new JLabel("Admin Login");
        title.setBounds(130, 20, 200, 30);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(0, 102, 204));
        panel.add(title);

        // ===== USERNAME =====
        JLabel lbUser = new JLabel("Username:");
        lbUser.setBounds(50, 80, 100, 25);
        panel.add(lbUser);

        tfUser = new JTextField();
        tfUser.setBounds(150, 80, 180, 25);
        panel.add(tfUser);

        // ===== PASSWORD =====
        JLabel lbPassword = new JLabel("Password:");
        lbPassword.setBounds(50, 120, 100, 25);
        panel.add(lbPassword);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(150, 120, 180, 25);
        panel.add(pfPassword);

        // ===== LOGIN BUTTON =====
        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(80, 170, 100, 30);
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        panel.add(btnLogin);

        // ===== CANCEL BUTTON =====
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(200, 170, 100, 30);
        btnCancel.setBackground(Color.GRAY);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        panel.add(btnCancel);

        // ===== LOGIN ACTION =====
        btnLogin.addActionListener(e -> {

            String userName = tfUser.getText();
            String password = String.valueOf(pfPassword.getPassword());

            AdminUser adminUser = getAuthenticatedUser(userName, password);

            if (adminUser != null) {

                // ===== AUTO-CLOSE MESSAGE (5 seconds) =====
                JOptionPane optionPane = new JOptionPane(
                        "Welcome " + adminUser.userName,
                        JOptionPane.INFORMATION_MESSAGE);

                JDialog dialog = optionPane.createDialog(this, "Login Success");

                Timer timer = new Timer(2000, ev -> {
                    dialog.dispose();

                    // Open MainFrame after 5 sec
                   new MainFrame().initialize(adminUser);
                    dispose();
                });

                timer.setRepeats(false);
                timer.start();

                dialog.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this,
                        "Username or Password Invalid",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    // ===== DATABASE METHOD =====
    private AdminUser getAuthenticatedUser(String userName, String password) {

        AdminUser adminUser = null;

        String sql = "SELECT * FROM admin WHERE BINARY userName = ? AND BINARY password = ?";

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userName);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                adminUser = new AdminUser();
                adminUser.userName = rs.getString("userName");
                adminUser.password = rs.getString("password");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return adminUser;
    }

 
}
