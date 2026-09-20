package dao;

import model.Employee;
import model.SalaryHistory;
import model.PromotionHistory;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Adds a new employee (with full profile), OR reactivates a resigned/terminated one
    public boolean addEmployee(Employee emp, String password) {

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int existingUserId = -1;
            String existingStatus = null;

            String checkSql = "SELECT id, status FROM users WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, emp.getEmail());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    existingUserId = rs.getInt("id");
                    existingStatus = rs.getString("status");
                }
            }

            int userId;

            if (existingUserId != -1) {
                if (!"RESIGNED".equalsIgnoreCase(existingStatus) && !"TERMINATED".equalsIgnoreCase(existingStatus)) {
                    conn.rollback();
                    return false;
                }

                String reactivateUser = "UPDATE users SET name = ?, password = ?, status = 'ACTIVE' WHERE id = ?";
                try (PreparedStatement stmt1 = conn.prepareStatement(reactivateUser)) {
                    stmt1.setString(1, emp.getName());
                    stmt1.setString(2, password);
                    stmt1.setInt(3, existingUserId);
                    stmt1.executeUpdate();
                }
                userId = existingUserId;

                String checkEmpSql = "SELECT id FROM employees WHERE user_id = ?";
                boolean hasEmployeeRow;
                try (PreparedStatement checkEmp = conn.prepareStatement(checkEmpSql)) {
                    checkEmp.setInt(1, userId);
                    ResultSet rs = checkEmp.executeQuery();
                    hasEmployeeRow = rs.next();
                }

                if (hasEmployeeRow) {
                    updateEmployeeProfile(conn, userId, emp);
                } else {
                    insertEmployeeProfile(conn, userId, emp);
                }

            } else {
                String insertUser = "INSERT INTO users (name, email, password, role, status) VALUES (?, ?, ?, 'EMPLOYEE', 'ACTIVE')";
                try (PreparedStatement stmt1 = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                    stmt1.setString(1, emp.getName());
                    stmt1.setString(2, emp.getEmail());
                    stmt1.setString(3, password);
                    stmt1.executeUpdate();

                    ResultSet keys = stmt1.getGeneratedKeys();
                    if (keys.next()) {
                        userId = keys.getInt(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
                insertEmployeeProfile(conn, userId, emp);
            }

            // Log initial salary into salary_history
            String empIdSql = "SELECT id FROM employees WHERE user_id = ?";
            int employeeId = -1;
            try (PreparedStatement stmt = conn.prepareStatement(empIdSql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) employeeId = rs.getInt("id");
            }

            if (employeeId != -1 && emp.getFirstSalary() != null && !emp.getFirstSalary().isEmpty()) {
                String insertSalaryHist = "INSERT INTO salary_history (employee_id, salary_amount, hike_percent, effective_date) VALUES (?, ?, NULL, CURDATE())";
                try (PreparedStatement stmt = conn.prepareStatement(insertSalaryHist)) {
                    stmt.setInt(1, employeeId);
                    stmt.setString(2, emp.getFirstSalary());
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void insertEmployeeProfile(Connection conn, int userId, Employee emp) throws SQLException {
        String sql = "INSERT INTO employees (user_id, department, designation, joining_date, employee_code, dob, phone, " +
                     "personal_email, emergency_contact, current_address, permanent_address, employment_type, work_location, " +
                     "current_salary, first_salary, pf_esi_amount, attendance_percentage) " +
                     "VALUES (?, ?, ?, CURDATE(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 100.00)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, emp.getDepartment());
            stmt.setString(3, emp.getDesignation());
            stmt.setString(4, emp.getEmployeeCode());
            stmt.setString(5, emp.getDob());
            stmt.setString(6, emp.getPhone());
            stmt.setString(7, emp.getPersonalEmail());
            stmt.setString(8, emp.getEmergencyContact());
            stmt.setString(9, emp.getCurrentAddress());
            stmt.setString(10, emp.getPermanentAddress());
            stmt.setString(11, emp.getEmploymentType());
            stmt.setString(12, emp.getWorkLocation());
            stmt.setString(13, emp.getFirstSalary());
            stmt.setString(14, emp.getFirstSalary());
            stmt.setString(15, emp.getPfEsiAmount());
            stmt.executeUpdate();
        }
    }

    private void updateEmployeeProfile(Connection conn, int userId, Employee emp) throws SQLException {
        String sql = "UPDATE employees SET department=?, designation=?, joining_date=CURDATE(), employee_code=?, dob=?, " +
                     "phone=?, personal_email=?, emergency_contact=?, current_address=?, permanent_address=?, " +
                     "employment_type=?, work_location=?, current_salary=?, first_salary=?, pf_esi_amount=? WHERE user_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emp.getDepartment());
            stmt.setString(2, emp.getDesignation());
            stmt.setString(3, emp.getEmployeeCode());
            stmt.setString(4, emp.getDob());
            stmt.setString(5, emp.getPhone());
            stmt.setString(6, emp.getPersonalEmail());
            stmt.setString(7, emp.getEmergencyContact());
            stmt.setString(8, emp.getCurrentAddress());
            stmt.setString(9, emp.getPermanentAddress());
            stmt.setString(10, emp.getEmploymentType());
            stmt.setString(11, emp.getWorkLocation());
            stmt.setString(12, emp.getFirstSalary());
            stmt.setString(13, emp.getFirstSalary());
            stmt.setString(14, emp.getPfEsiAmount());
            stmt.setInt(15, userId);
            stmt.executeUpdate();
        }
    }

    // Used for dropdowns (Task/Document assignment, Termination, Hike, Promotion) - ACTIVE only
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
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Full profile for the employee's own dashboard
    public Employee getEmployeeProfile(int employeeId) {
        String sql = "SELECT e.*, u.name, u.email FROM employees e JOIN users u ON e.user_id = u.id WHERE e.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Employee emp = new Employee();
                emp.setEmployeeId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setEmail(rs.getString("email"));
                emp.setDepartment(rs.getString("department"));
                emp.setDesignation(rs.getString("designation"));
                emp.setEmployeeCode(rs.getString("employee_code"));
                emp.setDob(rs.getString("dob"));
                emp.setPhone(rs.getString("phone"));
                emp.setPersonalEmail(rs.getString("personal_email"));
                emp.setEmergencyContact(rs.getString("emergency_contact"));
                emp.setCurrentAddress(rs.getString("current_address"));
                emp.setPermanentAddress(rs.getString("permanent_address"));
                emp.setEmploymentType(rs.getString("employment_type"));
                emp.setWorkLocation(rs.getString("work_location"));
                emp.setCurrentSalary(rs.getString("current_salary"));
                emp.setFirstSalary(rs.getString("first_salary"));
                emp.setPfEsiAmount(rs.getString("pf_esi_amount"));
                emp.setAttendancePercentage(rs.getString("attendance_percentage"));
                emp.setJoiningDate(rs.getString("joining_date"));
                return emp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SalaryHistory> getSalaryHistory(int employeeId) {
        List<SalaryHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM salary_history WHERE employee_id = ? ORDER BY effective_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SalaryHistory s = new SalaryHistory();
                s.setSalaryAmount(rs.getString("salary_amount"));
                s.setHikePercent(rs.getString("hike_percent"));
                s.setEffectiveDate(rs.getString("effective_date"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PromotionHistory> getPromotionHistory(int employeeId) {
        List<PromotionHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM promotion_history WHERE employee_id = ? ORDER BY promotion_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PromotionHistory p = new PromotionHistory();
                p.setPreviousDesignation(rs.getString("previous_designation"));
                p.setNewDesignation(rs.getString("new_designation"));
                p.setPromotionDate(rs.getString("promotion_date"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Applies annual hike - updates current_salary AND logs to salary_history
    public boolean applyHike(int employeeId, double hikePercent) {
        String getCurrent = "SELECT current_salary FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            double currentSalary = 0;
            try (PreparedStatement stmt = conn.prepareStatement(getCurrent)) {
                stmt.setInt(1, employeeId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) currentSalary = rs.getDouble("current_salary");
            }

            double newSalary = currentSalary + (currentSalary * hikePercent / 100.0);

            String updateSql = "UPDATE employees SET current_salary = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                stmt.setDouble(1, newSalary);
                stmt.setInt(2, employeeId);
                stmt.executeUpdate();
            }

            String historySql = "INSERT INTO salary_history (employee_id, salary_amount, hike_percent, effective_date) VALUES (?, ?, ?, CURDATE())";
            try (PreparedStatement stmt = conn.prepareStatement(historySql)) {
                stmt.setInt(1, employeeId);
                stmt.setDouble(2, newSalary);
                stmt.setDouble(3, hikePercent);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Promotes employee - updates designation AND logs to promotion_history
    public boolean promoteEmployee(int employeeId, String newDesignation) {
        String getCurrent = "SELECT designation FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            String previousDesignation = "";
            try (PreparedStatement stmt = conn.prepareStatement(getCurrent)) {
                stmt.setInt(1, employeeId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) previousDesignation = rs.getString("designation");
            }

            String updateSql = "UPDATE employees SET designation = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                stmt.setString(1, newDesignation);
                stmt.setInt(2, employeeId);
                stmt.executeUpdate();
            }

            String historySql = "INSERT INTO promotion_history (employee_id, previous_designation, new_designation, promotion_date) VALUES (?, ?, ?, CURDATE())";
            try (PreparedStatement stmt = conn.prepareStatement(historySql)) {
                stmt.setInt(1, employeeId);
                stmt.setString(2, previousDesignation);
                stmt.setString(3, newDesignation);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
            return false;
        }
    }
}