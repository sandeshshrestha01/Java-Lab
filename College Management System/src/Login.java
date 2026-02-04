
import javax.swing.JButton;
import javax.swing.JFrame;

public class Login {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Login");
        frame.setSize(300, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton studBtn = new JButton("Student Login");
        studBtn.setBounds(80, 50, 140, 40);
        frame.add(studBtn);

        JButton adminBtn = new JButton("Admin Login");
        adminBtn.setBounds(80, 120, 140, 40);
        frame.add(adminBtn);

        //  CONNECT ADMIN LOGIN
        adminBtn.addActionListener(e -> {
            new Adminlogin().initialize(); // open admin login
            frame.dispose();               // close current window
        });
        
        studBtn.addActionListener(e -> {
            new StudentLogin(); // open student login
            frame.dispose();    // close current window
        });
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
