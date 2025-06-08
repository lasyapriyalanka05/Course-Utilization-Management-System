package coders_course_utilization;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class Coders_courseutilization {
    private static final String DATABASE_URL = "jdbc:sqlite:C:/Users/dell/OneDrive/Desktop/APPS/javaapp.db";
    private Connection conn;
    private JFrame frame;
    private JTextField[] textFields;
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;

    public Coders_courseutilization() {
        connect();
        initializeUI();
    }

    private void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DATABASE_URL);
            System.out.println("✅ Connected to SQLite database successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Database Connection Failed: " + e.getMessage());
        }
    }

    private void initializeUI() {
        frame = new JFrame("Course Utilization Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout(10, 10));
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        String[] labels = {"Course ID", "Course Util Code", "Unit No", "Unit Name", "Required Contact Hours", "Course Out ID", "Reference ID"};
        textFields = new JTextField[labels.length];
        
        for (int i = 0; i < labels.length; i++) {
            inputPanel.add(new JLabel(labels[i] + ":"));
            textFields[i] = new JTextField();
            inputPanel.add(textFields[i]);
        }

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Retrieve");
        searchButton.addActionListener(this::retrieveRecords);
        searchPanel.add(new JLabel("Search by Course ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        
        addButton.addActionListener(e -> AP22110010356_course_utilization_add());
        updateButton.addActionListener(e -> AP22110010356_course_utilization_update());
        deleteButton.addActionListener(e -> AP22110010356_course_utilization_delete());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        tableModel = new DefaultTableModel(new String[]{"ID", "Course ID", "Course Util Code", "Unit No", "Unit Name", "Contact Hours", "Course Out ID", "Reference ID"}, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setVisible(true);
    }
    
    private void retrieveRecords(ActionEvent e) {
        tableModel.setRowCount(0);
        String courseId = searchField.getText().trim();
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM course_utilization WHERE cour_id LIKE ?")) {
            pstmt.setString(1, "%" + courseId + "%");
            ResultSet rs = pstmt.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                tableModel.addRow(new Object[]{rs.getInt("ID"), rs.getString("cour_id"), rs.getString("cour_util_code"), rs.getInt("unit_no"), rs.getString("unit_name"), rs.getInt("require_contact_hr"), rs.getString("cour_out_id"), rs.getString("ref_id")});
            }
            if (!found) {
                JOptionPane.showMessageDialog(null, "No records found for Course ID: " + courseId);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Search error: " + ex.getMessage());
        }
    }

    private void AP22110010356_course_utilization_add() {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO course_utilization (cour_id, cour_util_code, unit_no, unit_name, require_contact_hr, cour_out_id, ref_id) VALUES (?, ?, ?, ?, ?, ?, ?)");) {
            for (int i = 0; i < textFields.length; i++) {
                pstmt.setString(i + 1, textFields[i].getText());
            }
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Record added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Insert failed: " + e.getMessage());
        }
    }

    private void AP22110010356_course_utilization_update() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Select a record to update");
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE course_utilization SET cour_id=?, cour_util_code=?, unit_no=?, unit_name=?, require_contact_hr=?, cour_out_id=?, ref_id=? WHERE ID=?")) {
            for (int i = 0; i < textFields.length; i++) {
                pstmt.setString(i + 1, textFields[i].getText());
            }
            pstmt.setInt(8, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Record updated successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Update failed: " + e.getMessage());
        }
    }

    private void AP22110010356_course_utilization_delete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Select a record to delete");
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM course_utilization WHERE ID=?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Record deleted successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Deletion failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Coders_courseutilization::new);
    }
}
