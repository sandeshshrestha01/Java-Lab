import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Courses extends JFrame {

    JPanel listPanel;
    JTextField courseField;

    public Courses() {

        setTitle("Courses");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🔷 TOP PANEL (ADD COURSE)
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        courseField = new JTextField();
        JButton addButton = new JButton("Add Course");

        addButton.addActionListener(e -> addCourse());

        topPanel.add(new JLabel("Course:"), BorderLayout.WEST);
        topPanel.add(courseField, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 🔷 LIST PANEL
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        add(scrollPane, BorderLayout.CENTER);

        loadCourses();

        setVisible(true);
    }

    // ✅ ADD COURSE
    private void addCourse() {
        String courseName = courseField.getText().trim();

        if (courseName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course name cannot be empty.");
            return;
        }

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO courses (course_name) VALUES (?)")) {

            ps.setString(1, courseName);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Course added!");
            courseField.setText("");
            loadCourses();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ LOAD COURSES
    private void loadCourses() {

        listPanel.removeAll();

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM courses")) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String id = rs.getString("id"); // adjust if different
                String name = rs.getString("course_name");

                JPanel row = new JPanel(new BorderLayout());
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
                row.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                row.setBackground(new Color(245, 245, 245));

                JLabel label = new JLabel(name);
                label.setFont(new Font("Segoe Print", Font.PLAIN, 16));

                // BUTTON PANEL
                JPanel btnPanel = new JPanel();
                JButton editBtn = new JButton("Edit");
                JButton deleteBtn = new JButton("Delete");

                // 🟡 EDIT
                editBtn.addActionListener(e -> {
                    String newName = JOptionPane.showInputDialog(this, "Edit Course:", name);

                    if (newName != null && !newName.trim().isEmpty()) {
                        try (Connection con2 = DBConfig.getConnection();
                             PreparedStatement ps2 = con2.prepareStatement(
                                     "UPDATE courses SET course_name=? WHERE id=?")) {

                            ps2.setString(1, newName);
                            ps2.setString(2, id);
                            ps2.executeUpdate();

                            JOptionPane.showMessageDialog(this, "Updated!");
                            loadCourses();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                // 🔴 DELETE
                deleteBtn.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Delete this course?", "Confirm",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try (Connection con3 = DBConfig.getConnection();
                             PreparedStatement ps3 = con3.prepareStatement(
                                     "DELETE FROM courses WHERE id=?")) {

                            ps3.setString(1, id);
                            ps3.executeUpdate();

                            JOptionPane.showMessageDialog(this, "Deleted!");
                            loadCourses();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                btnPanel.add(editBtn);
                btnPanel.add(deleteBtn);

                row.add(label, BorderLayout.CENTER);
                row.add(btnPanel, BorderLayout.EAST);

                listPanel.add(row);
                listPanel.add(Box.createVerticalStrut(5));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}