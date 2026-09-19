package dao;

import model.Termination;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TerminationDAO {

    // Manager initiates termination
    public boolean initiateTermination(int employeeId, String reason) {
        String sql = "INSERT INTO terminations (employee_id, reason, status) VALUES (?, ?, 'PENDING_HR')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setString(2, reason);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // HR's queue - awaiting approval
    public List<Termination> getPendingForHR() {
        return getByStatus("PENDING_HR");
    }

    // HR's exit checklist queue - already approved, exit process in progress
    public List<Termination> getApprovedInProgress() {
        List<Termination> list = new ArrayList<>();
        String sql = "SELECT t.*, u.name AS employee_name FROM terminations t " +
                     "JOIN employees e ON t.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id " +
                     "WHERE t.status = 'HR_APPROVED' AND t.exit_completed = FALSE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Termination> getByStatus(String status) {
        List<Termination> list = new ArrayList<>();
        String sql = "SELECT t.*, u.name AS employee_name FROM terminations t " +
                     "JOIN employees e ON t.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id " +
                     "WHERE t.status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // HR approves the termination
    public boolean hrApprove(int terminationId) {
        String sql = "UPDATE terminations SET status = 'HR_APPROVED' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, terminationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // HR updates the exit checklist; auto-finalizes if all 6 are checked
    public boolean updateChecklist(int terminationId, boolean assets, boolean tasks, boolean docs,
                                    boolean hrReview, boolean exitInterview, boolean relievingLetter) {
        boolean allDone = assets && tasks && docs && hrReview && exitInterview && relievingLetter;

        String sql = "UPDATE terminations SET assets_returned=?, pending_tasks_completed=?, documents_cleared=?, " +
                     "hr_review_completed=?, exit_interview_completed=?, relieving_letter_issued=?, exit_completed=? " +
                     "WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, assets);
            stmt.setBoolean(2, tasks);
            stmt.setBoolean(3, docs);
            stmt.setBoolean(4, hrReview);
            stmt.setBoolean(5, exitInterview);
            stmt.setBoolean(6, relievingLetter);
            stmt.setBoolean(7, allDone);
            stmt.setInt(8, terminationId);
            stmt.executeUpdate();

            if (allDone) {
                markUserTerminated(terminationId);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void markUserTerminated(int terminationId) {
        String sql = "UPDATE users u JOIN employees e ON u.id = e.user_id " +
                     "JOIN terminations t ON e.id = t.employee_id " +
                     "SET u.status = 'TERMINATED' WHERE t.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, terminationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Termination mapRow(ResultSet rs) throws SQLException {
        Termination t = new Termination();
        t.setId(rs.getInt("id"));
        t.setEmployeeId(rs.getInt("employee_id"));
        t.setReason(rs.getString("reason"));
        t.setStatus(rs.getString("status"));
        t.setInitiatedDate(rs.getString("initiated_date"));
        t.setAssetsReturned(rs.getBoolean("assets_returned"));
        t.setPendingTasksCompleted(rs.getBoolean("pending_tasks_completed"));
        t.setDocumentsCleared(rs.getBoolean("documents_cleared"));
        t.setHrReviewCompleted(rs.getBoolean("hr_review_completed"));
        t.setExitInterviewCompleted(rs.getBoolean("exit_interview_completed"));
        t.setRelievingLetterIssued(rs.getBoolean("relieving_letter_issued"));
        t.setExitCompleted(rs.getBoolean("exit_completed"));
        try { t.setEmployeeName(rs.getString("employee_name")); } catch (SQLException ignored) {}
        return t;
    }
}
