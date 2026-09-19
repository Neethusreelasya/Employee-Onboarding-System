package dao;

import model.Leave;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveDAO {

    public boolean applyLeave(int employeeId, String leaveType, String reason, String startDate, String endDate, int days) {
        String sql = "INSERT INTO leaves (employee_id, leave_type, reason, start_date, end_date, days_requested, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'PENDING')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setString(2, leaveType);
            stmt.setString(3, reason);
            stmt.setString(4, startDate);
            stmt.setString(5, endDate);
            stmt.setInt(6, days);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Leave> getLeavesByEmployee(int employeeId) {
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT * FROM leaves WHERE employee_id = ? ORDER BY applied_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                leaves.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaves;
    }

    public int getUsedLeaveDays(int employeeId, String leaveType) {
        String sql = "SELECT COALESCE(SUM(days_requested), 0) AS used FROM leaves " +
                     "WHERE employee_id = ? AND leave_type = ? AND status = 'ACCEPTED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setString(2, leaveType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("used");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Leave> getPendingForHR() {
        return getLeavesByStatus("PENDING");
    }

    public List<Leave> getPendingForManager() {
        return getLeavesByStatus("FORWARDED_TO_MANAGER");
    }

    public List<Leave> getManagerApprovedForHR() {
        return getLeavesByStatus("MANAGER_APPROVED");
    }

    private List<Leave> getLeavesByStatus(String status) {
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT l.*, u.name AS employee_name FROM leaves l " +
                     "JOIN employees e ON l.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id " +
                     "WHERE l.status = ? ORDER BY l.applied_date ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Leave l = mapRow(rs);
                l.setEmployeeName(rs.getString("employee_name"));
                leaves.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaves;
    }

    public boolean forwardToManager(int leaveId) {
        return updateStatus(leaveId, "FORWARDED_TO_MANAGER");
    }

    public boolean managerDecision(int leaveId, boolean approved) {
        return updateStatus(leaveId, approved ? "MANAGER_APPROVED" : "MANAGER_REJECTED");
    }

    public boolean hrFinalDecision(int leaveId, boolean accepted) {
        return updateStatus(leaveId, accepted ? "ACCEPTED" : "REJECTED");
    }

    private boolean updateStatus(int leaveId, String status) {
        String sql = "UPDATE leaves SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, leaveId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Leave> getLeaveHistoryForEmployee(int employeeId) {
        return getLeavesByEmployee(employeeId);
    }

    private Leave mapRow(ResultSet rs) throws SQLException {
        Leave l = new Leave();
        l.setId(rs.getInt("id"));
        l.setEmployeeId(rs.getInt("employee_id"));
        l.setLeaveType(rs.getString("leave_type"));
        l.setReason(rs.getString("reason"));
        l.setStartDate(rs.getString("start_date"));
        l.setEndDate(rs.getString("end_date"));
        l.setDaysRequested(rs.getInt("days_requested"));
        l.setStatus(rs.getString("status"));
        l.setAppliedDate(rs.getString("applied_date"));
        return l;
    }
}
