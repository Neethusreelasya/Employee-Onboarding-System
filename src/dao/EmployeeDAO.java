package dao;

import model.Employee;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Adds a new employee, OR reactivates a resigned one if the email already exists
    public boolean addEmployee(String name, String email, String password, String department, String designation) {

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Check if this email already exists
            int existingUserId = -1;
            String existingStatus = null;

            String checkSql = "SELECT id, status FROM users WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    existingUserId = rs.getInt("id");
                    existingStatus = rs.getString("status");
                }
            }

            if (existingUserId != -1) {
                // Email already exists
                if (!"RESIGNED".equalsIgnoreCase(existingStatus) && !"TERMINATED".equalsIgnoreCase(existingStatus)) {
                    // Active employee already using this email - genuine conflict, reject
                    conn.rollback();
                    return false;
                }

                // Reactivate the resigned/terminated account
                String reactivateUser = "UPDATE users SET name = ?, password = ?, status = 'ACTIVE' WHERE id = ?";
                try (PreparedStatement stmt1 = conn.prepareStatement(reactivateUser)) {
                    stmt1.setString(1, name);
                    stmt1.setString(2, password);
                    stmt1.setInt(3, existingUserId);
                    stmt1.executeUpdate();
                }

                // Update or insert their employees record
                String checkEmpSql = "SELECT id FROM employees WHERE user_id = ?";
                boolean hasEmployeeRow = false;
                try (PreparedStatement checkEmp = conn.prepareStatement(checkEmpSql)) {
                    checkEmp.setInt(1, existingUserId);
                    ResultSet rs = checkEmp.executeQuery();
                    hasEmployeeRow = rs.next();
                }

                if (hasEmployeeRow) {
                    String updateEmp = "UPDATE employees SET department = ?, designation = ?, joining_date = CURDATE() WHERE user_id = ?";
                    try (PreparedStatement stmt2 = conn.prepareStatement(updateEmp)) {
                        stmt2.setString(1, department);
                        stmt2.setString(2, designation);
                        stmt2.setInt(3, existingUserId);
                        stmt2.executeUpdate();
                    }
                } else {
                    String insertEmp = "INSERT INTO employees (user_id, department, designation, joining_date) VALUES (?, ?, ?, CURDATE())";
                    try (PreparedStatement stmt2 = conn.prepareStatement(insertEmp)) {
                        stmt2.setInt(1, existingUserId);
                        stmt2.setString(2, department);
                        stmt2.setString(3, designation);
                        stmt2.executeUpdate();
                    }
                }

                conn.commit();
                return true;
            }

            // Email doesn't exist yet - normal new employee flow
            String insertUser = "INSERT INTO users (name, email, password, role, status) VALUES (?, ?, ?, 'EMPLOYEE', 'ACTIVE')";
            int newUserId;
            try (PreparedStatement stmt1 = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                stmt1.setString(1, name);
                stmt1.setString(2, email);
                stmt1.setString(3, password);
                stmt1.executeUpdate();

                ResultSet keys = stmt1.getGeneratedKeys();
                if (keys.next()) {
                    newUserId = keys.getInt(1);
                } else {
                    conn.rollback();
                    return false;
                }
            }

            String insertEmployee = "INSERT INTO employees (user_id, department, designation, joining_date) VALUES (?, ?, ?, CURDATE())";
            try (PreparedStatement stmt2 = conn.prepareStatement(insertEmployee)) {
                stmt2.setInt(1, newUserId);
                stmt2.setString(2, department);
                stmt2.setString(3, designation);
                stmt2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Only returns ACTIVE employees (excludes RESIGNED / TERMINATED)
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT e.id AS emp_id, u.id AS user_id, u.name, u.email, e.department, e.designation " +
                     "FROM employees e JOIN users u ON e.user_id = u.id " +
                     "WHERE u.status = 'ACTIVE'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee emp = new Employee();
                emp.setEmployeeId(rs.getInt("emp_id"));
                emp.setUserId(rs.getInt("user_id"));
                emp.setName(rs.getString("name"));
                emp.setEmail(rs.getString("email"));
                emp.setDepartment(rs.getString("department"));
                emp.setDesignation(rs.getString("designation"));
                list.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getEmployeeIdByUserId(int userId) {
        String sql = "SELECT id FROM employees WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean deleteEmployee(int userId) {
        String deleteDocuments = "DELETE FROM documents WHERE employee_id = ?";
        String deleteTasks = "DELETE FROM employee_tasks WHERE employee_id = ?";
        String deleteEmployee = "DELETE FROM employees WHERE user_id = ?";
        String deleteUser = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement(deleteDocuments)) {
                stmt1.setInt(1, userId);
                stmt1.executeUpdate();
            }

            try (PreparedStatement stmt2 = conn.prepareStatement(deleteTasks)) {
                stmt2.setInt(1, userId);
                stmt2.executeUpdate();
            }

            try (PreparedStatement stmt3 = conn.prepareStatement(deleteEmployee)) {
                stmt3.setInt(1, userId);
                stmt3.executeUpdate();
            }

            try (PreparedStatement stmt4 = conn.prepareStatement(deleteUser)) {
                stmt4.setInt(1, userId);
                stmt4.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}