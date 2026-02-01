import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainFrame extends JFrame {

    final Font mainFont = new Font("Segoe Print", Font.BOLD, 16);
    JPanel noticeListPanel;

    public void initialize(AdminUser adminUser) {

        setLayout(new BorderLayout());

     JLabel lbWelcome = new JLabel("Welcome, " + adminUser.userName);
     lbWelcome.setFont(mainFont);

     JButton btnAddNotice = new JButton("Add Notice");
     btnAddNotice.setFont(mainFont);

     btnAddNotice.addActionListener(e -> new Notice(this).initialize());
     
     //  VERTICAL PANEL
     JPanel leftPanel = new JPanel();
     leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
     
     lbWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);
     btnAddNotice.setAlignmentX(Component.LEFT_ALIGNMENT);
     leftPanel.add(lbWelcome);
     leftPanel.add(Box.createVerticalStrut(8)); // spacing
     leftPanel.add(btnAddNotice);
     
     JPanel topPanel = new JPanel(new BorderLayout());
     topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
     topPanel.add(leftPanel, BorderLayout.WEST);


        noticeListPanel = new JPanel();
        noticeListPanel.setLayout(new BoxLayout(noticeListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(noticeListPanel);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadNoticeTitles();

        setTitle("Dashboard");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void loadNoticeTitles() {
    noticeListPanel.removeAll();

    try (Connection con = DBConfig.getConnection();
         PreparedStatement ps = con.prepareStatement(
                 "SELECT id, title FROM notice ORDER BY id DESC")) {

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");

            JButton titleBtn = new JButton(title);
            titleBtn.setFont(mainFont);
            titleBtn.setHorizontalAlignment(SwingConstants.LEFT);

            // ✅ CLICK TO OPEN NOTICE
            titleBtn.addActionListener(e ->
                    new Notice(this).viewNotice(id)
            );

            noticeListPanel.add(titleBtn);
            noticeListPanel.add(Box.createVerticalStrut(5));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    noticeListPanel.revalidate();
    noticeListPanel.repaint();
 }
}