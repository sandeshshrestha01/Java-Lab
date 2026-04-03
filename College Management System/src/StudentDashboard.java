import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentDashboard extends JFrame {

    final Font mainFont = new Font("Segoe Print", Font.PLAIN, 16);
    JPanel contentPanel;

    public void initialize(StudentUser studentUser) {

        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 245, 245));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        JLabel lbWelcome = new JLabel("Welcome, " + studentUser.full_name);
        lbWelcome.setFont(new Font("Segoe Print", Font.BOLD, 18));

        JLabel logo = new JLabel("🎓 STUDENT");
        logo.setFont(new Font("Segoe Print", Font.BOLD, 18));

        header.add(lbWelcome, BorderLayout.WEST);
        header.add(logo, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // ================= LEFT MENU =================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        leftPanel.setBackground(Color.WHITE);

        JButton btnCourses = new JButton("Courses");
        JButton btnNotices = new JButton("Notices");
        JButton btnLogout = new JButton("Logout");

        JButton[] buttons = {btnCourses, btnNotices, btnLogout};

        for (JButton btn : buttons) {
            btn.setFont(mainFont);
            btn.setMaximumSize(new Dimension(180, 40));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(btn);
            leftPanel.add(Box.createVerticalStrut(10));
        }

        add(leftPanel, BorderLayout.WEST);

        // ================= RIGHT PANEL =================
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // ================= BUTTON ACTIONS =================
btnCourses.addActionListener(e -> loadCourses(studentUser));
        btnNotices.addActionListener(e -> loadNotices());

        btnLogout.addActionListener(e -> {
            dispose();
            Login.main(null);
        });

        // Load notices by default
        loadNotices();

        setTitle("Student Dashboard");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

   // ================= LOAD STUDENT'S COURSE =================
public void loadCourses(StudentUser studentUser) {
    contentPanel.removeAll();

    try (Connection con = DBConfig.getConnection();
         PreparedStatement ps = con.prepareStatement(
                 "SELECT course_name FROM courses WHERE course_name = ?")) {

        ps.setString(1, studentUser.course); // Only the course chosen by student

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String name = rs.getString("course_name");
            JPanel box = createBox("your course is: " + name);
            contentPanel.add(box);
            contentPanel.add(Box.createVerticalStrut(10));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    refreshPanel();
}

    // ================= LOAD SUBJECTS =================
    public void loadSubjects() {
        contentPanel.removeAll();

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM subjects")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("subject_name");
                JPanel box = createBox(name);
                contentPanel.add(box);
                contentPanel.add(Box.createVerticalStrut(10));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshPanel();
    }

    // ================= LOAD NOTICES (CLICKABLE) =================
    public void loadNotices() {
        contentPanel.removeAll();

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id, title FROM notice ORDER BY id DESC")) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");

                JPanel box = new JPanel(new BorderLayout());
                box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                box.setBackground(new Color(245, 245, 245));
                box.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));

                JLabel label = new JLabel(title);
                label.setFont(mainFont);

                box.add(label, BorderLayout.CENTER);
                box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                // Click to open notice
                box.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        new Notice(StudentDashboard.this).viewNotice(id);
                    }
                });

                contentPanel.add(box);
                contentPanel.add(Box.createVerticalStrut(10));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshPanel();
    }

    // ================= COMMON BOX DESIGN =================
    public JPanel createBox(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel label = new JLabel(text);
        label.setFont(mainFont);

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    public void refreshPanel() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}