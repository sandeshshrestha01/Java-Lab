import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentListView extends JFrame {

    JTable table;
    DefaultTableModel tableModel;

    public StudentListView() {
        setTitle("All Students");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // HEADER
        JLabel header = new JLabel("Student List", SwingConstants.CENTER);
        header.setFont(new Font("Segoe Print", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(header, BorderLayout.NORTH);

        // TABLE MODEL WITH BUTTON COLUMNS
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Address", "Course", "Edit", "Delete"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 4; // Only Edit/Delete clickable
            }
        };

        // TABLE
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe Print", Font.PLAIN, 16));

        // RENDER BUTTONS
        table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        table.getColumn("Delete").setCellRenderer(new ButtonRenderer());

        // ADD BUTTON EDITORS
        table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), true));
        table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), false));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        loadStudents();

        setVisible(true);
    }

    private void loadStudents() {
        tableModel.setRowCount(0); // clear table

        try (Connection con = DBConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT student_id, full_name, address, course FROM student ORDER BY student_id")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("student_id");
                String name = rs.getString("full_name");
                String address = rs.getString("address");
                String course = rs.getString("course");

                tableModel.addRow(new Object[]{id, name, address, course, "Edit", "Delete"});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // BUTTON RENDERER
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // BUTTON EDITOR
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean editButton;
        private boolean clicked;
        private int row;

        public ButtonEditor(JCheckBox checkBox, boolean isEdit) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            editButton = isEdit;

            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            button.setText((value == null) ? "" : value.toString());
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                String id = table.getValueAt(row, 0).toString();
                String name = table.getValueAt(row, 1).toString();
                String address = table.getValueAt(row, 2).toString();
                String course = table.getValueAt(row, 3).toString();

                if (editButton) {
                    // EDIT STUDENT
                    JTextField nameField = new JTextField(name);
                    JTextField addressField = new JTextField(address);
                    JTextField courseField = new JTextField(course);

                    JPanel panel = new JPanel(new GridLayout(3, 2));
                    panel.add(new JLabel("Name:"));
                    panel.add(nameField);
                    panel.add(new JLabel("Address:"));
                    panel.add(addressField);
                    panel.add(new JLabel("Course:"));
                    panel.add(courseField);

                    int result = JOptionPane.showConfirmDialog(null, panel, "Edit Student", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        try (Connection con = DBConfig.getConnection();
                             PreparedStatement ps = con.prepareStatement(
                                     "UPDATE student SET full_name=?, address=?, course=? WHERE student_id=?")) {
                            ps.setString(1, nameField.getText());
                            ps.setString(2, addressField.getText());
                            ps.setString(3, courseField.getText());
                            ps.setString(4, id);
                            ps.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Updated!");
                            loadStudents();
                        } catch (Exception ex) { ex.printStackTrace(); }
                    }
                } else {
                    // DELETE STUDENT
                    int confirm = JOptionPane.showConfirmDialog(null, "Delete this student?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try (Connection con = DBConfig.getConnection();
                             PreparedStatement ps = con.prepareStatement("DELETE FROM student WHERE student_id=?")) {
                            ps.setString(1, id);
                            ps.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Deleted!");
                            loadStudents();
                        } catch (Exception ex) { ex.printStackTrace(); }
                    }
                }
            }
            clicked = false;
            return editButton ? "Edit" : "Delete";
        }

        @Override
        public boolean stopCellEditing() { clicked = false; return super.stopCellEditing(); }
    }

}