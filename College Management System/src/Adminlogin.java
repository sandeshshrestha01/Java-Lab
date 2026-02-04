import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Adminlogin extends JFrame {

    final private Font mainFont = new Font("Segoe Print", Font.BOLD, 18);
    JTextField tfUser;
    JPasswordField pfPassword;

    public void initialize() {

        // ---------- TITLE ----------
        JLabel lbLoginForm = new JLabel("Admin Login", SwingConstants.CENTER);
        lbLoginForm.setFont(mainFont);

        JLabel lbUser = new JLabel("Username:");
        lbUser.setFont(mainFont);

        tfUser = new JTextField(15); // column size controls width
        tfUser.setFont(mainFont);

        JLabel lbPassword = new JLabel("Password:");
        lbPassword.setFont(mainFont);

        pfPassword = new JPasswordField(15);
        pfPassword.setFont(mainFont);

        // ---------- FORM PANEL ----------
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.add(lbLoginForm);
        formPanel.add(lbUser);
        formPanel.add(tfUser);
        formPanel.add(lbPassword);
        formPanel.add(pfPassword);

        // ---------- BUTTONS ----------
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(mainFont);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(mainFont);

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

        btnCancel.addActionListener(e -> dispose());

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        buttonsPanel.add(btnLogin);
        buttonsPanel.add(btnCancel);

        // ---------- FRAME ----------
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setTitle("Admin Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();                     //  AUTO RESIZE TO CONTENT
        setLocationRelativeTo(null); // CENTER ON SCREEN
        setVisible(true);
    }

    // ---------- DATABASE ----------
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
