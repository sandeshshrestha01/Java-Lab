import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Adminlogin extends JFrame {

    final private Font mainFont = new Font("Segoe Print", Font.BOLD, 18);
    JTextField tfUser;
    JPasswordField pfPassword;

    public void initialize() {

        JLabel lbLoginForm = new JLabel("Login Form", SwingConstants.CENTER);
        lbLoginForm.setFont(mainFont);

        JLabel lbUser = new JLabel("Username:");
        lbUser.setFont(mainFont);

        tfUser = new JTextField();
        tfUser.setFont(mainFont);

        JLabel lbPassword = new JLabel("Password:");
        lbPassword.setFont(mainFont);

        pfPassword = new JPasswordField();
        pfPassword.setFont(mainFont);

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.add(lbLoginForm);
        formPanel.add(lbUser);
        formPanel.add(tfUser);
        formPanel.add(lbPassword);
        formPanel.add(pfPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(mainFont);

        btnLogin.addActionListener(e -> {
            String userName = tfUser.getText();
            String password = String.valueOf(pfPassword.getPassword());

            AdminUser adminUser = getAuthenticatedUser(userName, password);

            if (adminUser != null) {
                MainFrame mainFrame = new MainFrame();
                mainFrame.initialize(adminUser);
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Username or Password Invalid",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(mainFont);
        btnCancel.addActionListener(e -> dispose());

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonsPanel.add(btnLogin);
        buttonsPanel.add(btnCancel);

        add(formPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);

        setTitle("Admin Login");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private AdminUser getAuthenticatedUser(String userName, String password) {

        AdminUser adminUser = null;

        String sql = "SELECT * FROM admin WHERE userName = ? AND password = ?";

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

    public static void main(String[] args) {
        new Adminlogin().initialize();
    }
}
