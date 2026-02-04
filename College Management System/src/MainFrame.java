import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainFrame extends JFrame {

    final Font mainFont = new Font("Segoe Print", Font.PLAIN, 16);
    JPanel noticeListPanel;

    public void initialize(AdminUser adminUser) {

        setLayout(new BorderLayout());

        // ================= TOP HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 245, 245));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        JLabel lbWelcome = new JLabel("Welcome, " + adminUser.userName);
        lbWelcome.setFont(new Font("Segoe Print", Font.BOLD, 18));

        JLabel logo = new JLabel("🛡 LOGO");
        logo.setFont(new Font("Segoe Print", Font.BOLD, 18));

        header.add(lbWelcome, BorderLayout.WEST);
        header.add(logo, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ================= LEFT MENU =================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        leftPanel.setBackground(Color.WHITE);

        JButton btnAddNotice = new JButton("+ Add Notice");
        JButton btnStudents = new JButton("+ Add Students");
        JButton btnStudentview = new JButton(" Students");
        JButton btnSubjects = new JButton("+ Add Subjects");
        JButton btnCourses = new JButton(" Courses");
        JButton btnLogout = new JButton("Logout");

        JButton[] buttons = {
                btnAddNotice, btnStudents, btnStudentview, btnSubjects, btnCourses, btnLogout
        };

        for (JButton btn : buttons) {
            btn.setFont(mainFont);
            btn.setMaximumSize(new Dimension(180, 40));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(btn);
            leftPanel.add(Box.createVerticalStrut(10));
        }

        btnAddNotice.addActionListener(e -> new Notice(this).initialize());
        btnCourses.addActionListener(e -> {
            // Example action for Courses button
            JOptionPane.showMessageDialog(this, "Courses functionality is not implemented yet.");
        });
       btnStudents.addActionListener(e -> {
        new StudentRegistration();
        });

        btnLogout.addActionListener(e -> {
       dispose();          // close dashboard
       Login.main(null);   // open Login screen
     });

        add(leftPanel, BorderLayout.WEST);

        // ================= RIGHT NOTICE CONTAINER =================
JPanel rightPanel = new JPanel(new BorderLayout());
rightPanel.setBackground(Color.WHITE);

// ===== HEADER (ALWAYS VISIBLE) =====
JLabel headerLabel = new JLabel("Notices");
headerLabel.setFont(new Font("Segoe Print", Font.BOLD, 20));
headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
rightPanel.add(headerLabel, BorderLayout.NORTH);
headerLabel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(15, 20, 10, 20)
));

// ===== NOTICE LIST (SCROLLABLE) =====
noticeListPanel = new JPanel();
noticeListPanel.setLayout(new BoxLayout(noticeListPanel, BoxLayout.Y_AXIS));
noticeListPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));
noticeListPanel.setBackground(Color.WHITE);

JScrollPane scrollPane = new JScrollPane(noticeListPanel);
scrollPane.getVerticalScrollBar().setUnitIncrement(16);
scrollPane.setBorder(null);

rightPanel.add(scrollPane, BorderLayout.CENTER);

// ===== ADD TO FRAME =====
add(rightPanel, BorderLayout.CENTER);

// LOAD DATA
loadNoticeTitles();

        setTitle("Admin Dashboard");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    // ================= LOAD ONLY NOTICE TITLES =================
    public void loadNoticeTitles() {

        noticeListPanel.removeAll();

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT id, title FROM notice ORDER BY id DESC")) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");
                String title = rs.getString("title");

                JPanel noticeBox = new JPanel(new BorderLayout());
                noticeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                noticeBox.setBackground(new Color(245, 245, 245));
                noticeBox.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));

                JLabel titleLabel = new JLabel(title);
                titleLabel.setFont(mainFont);

                noticeBox.add(titleLabel, BorderLayout.CENTER);
                noticeBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                noticeBox.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        new Notice(MainFrame.this).viewNotice(id);
                    }
                });

                noticeListPanel.add(noticeBox);
                noticeListPanel.add(Box.createVerticalStrut(8));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        noticeListPanel.revalidate();
        noticeListPanel.repaint();
    }
}
