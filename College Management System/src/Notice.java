import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Notice extends JFrame {

    JTextField tfTitle;
    JTextArea taMessage;
    MainFrame dashboard;

    // For ADD notice
    public Notice(MainFrame dashboard) {
        this.dashboard = dashboard;
    }

    // ================= ADD NOTICE =================
    public void initialize() {

        setLayout(new BorderLayout(10, 10));

        tfTitle = new JTextField();
        taMessage = new JTextArea(10, 30);
        taMessage.setLineWrap(true);
        taMessage.setWrapStyleWord(true);

        JButton btnSave = new JButton("Save Notice");
        btnSave.addActionListener(e -> saveNotice());

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Title"));
        panel.add(tfTitle);
        panel.add(new JLabel("Message"));
        panel.add(new JScrollPane(taMessage));
        panel.add(btnSave);

        add(panel);

        setTitle("Add Notice");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void saveNotice() {

        String title = tfTitle.getText();
        String message = taMessage.getText();

        if (title.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required");
            return;
        }

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO notice (title, message) VALUES (?, ?)")) {

            ps.setString(1, title);
            ps.setString(2, message);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Notice Uploaded");

            if (dashboard != null) {
                dashboard.loadNoticeTitles();
            }

            dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= VIEW NOTICE =================
    public void viewNotice(int noticeId) {

        setLayout(new BorderLayout(10, 10));

        JLabel lbTitle = new JLabel();
        lbTitle.setFont(new Font("Segoe Print", Font.BOLD, 18));

        JTextArea taView = new JTextArea();
        taView.setEditable(false);
        taView.setLineWrap(true);
        taView.setWrapStyleWord(true);

        JButton btnDelete = new JButton("Delete Notice");
        btnDelete.setForeground(Color.RED);

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT title, message FROM notice WHERE id = ?")) {

            ps.setInt(1, noticeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lbTitle.setText(rs.getString("title"));
                taView.setText(rs.getString("message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        btnDelete.addActionListener(e -> deleteNotice(noticeId));

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        top.add(lbTitle, BorderLayout.CENTER);
        top.add(btnDelete, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(taView), BorderLayout.CENTER);

        setTitle("View Notice");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ================= DELETE =================
    private void deleteNotice(int id) {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this notice?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM notice WHERE id = ?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Notice Deleted");

            if (dashboard != null) {
                dashboard.loadNoticeTitles();
            }

            dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
