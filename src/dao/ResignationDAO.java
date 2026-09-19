package dao;

import model.Resignation;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResignationDAO {

    // Employee submits
    public boolean applyResignation(int employeeId, String reason, String lastWorkingDay) {
        String sql = "INSERT INTO resignations (employee_id, reason, last_working_day, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setString(2, reason);
            stmt.setString(3, lastWorkingDay);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Manager's queue - awaiting first decision
    public List<Resignation> getPendingForManager() {
        return getByStatus("PENDING");
    }

    // HR's queue - manager already approved, awaiting HR's final acceptance
    public List<Resignation> getApprovedByManagerForHR() {
        return getByStatus("MANAGER_APPROVED");
    }

    // HR's exit checklist queue - HR already accepted, exit process in progress
    public List<Resignation> getAcceptedInProgress() {
        List<Resignation> list = new ArrayList<>();
        String sql = "SELECT r.*, u.name AS employee_name FROM resignations r " +
                     "JOIN employees e ON r.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id " +
                     "WHERE r.status = 'ACCEPTED' AND r.exit_completed = FALSE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Resignation> getByStatus(String status) {
        List<Resignation> list = new ArrayList<>();
        String sql = "SELECT r.*, u.name AS employee_name FROM resignations r " +
                     "JOIN employees e ON r.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id " +
                     "WHERE r.status = ?";
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

    // Manager's decision (first step)
    public boolean managerDecision(int resignationId, boolean approved) {
        String sql = "UPDATE resignations SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, approved ? "MANAGER_APPROVED" : "MANAGER_REJECTED");
            stmt.setInt(2, resignationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // HR's final decision (second step, only after manager approved)
    public boolean hrFinalDecision(int resignationId, boolean accepted) {
        String sql = "UPDATE resignations SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accepted ? "ACCEPTED" : "REJECTED");
            stmt.setInt(2, resignationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // HR updates the exit checklist; auto-finalizes if all 6 are checked
    public boolean updateChecklist(int resignationId, boolean assets, boolean tasks, boolean docs,
                                    boolean hrReview, boolean exitInterview, boolean relievingLetter) {
        boolean allDone = assets && tasks && docs && hrReview && exitInterview && relievingLetter;

        String sql = "UPDATE resignations SET assets_returned=?, pending_tasks_completed=?, documents_cleared=?, " +
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
            stmt.setInt(8, resignationId);
            stmt.executeUpdate();

            if (allDone) {
                markUserResigned(resignationId);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void markUserResigned(int resignationId) {
        String sql = "UPDATE users u JOIN employees e ON u.id = e.user_id " +
                     "JOIN resignations r ON e.id = r.employee_id " +
                     "SET u.status = 'RESIGNED' WHERE r.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resignationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Resignation getLatestForEmployee(int employeeId) {
        String sql = "SELECT * FROM resignations WHERE employee_id = ? ORDER BY applied_date DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Resignation mapRow(ResultSet rs) throws SQLException {
        Resignation r = new Resignation();
        r.setId(rs.getInt("id"));
        r.setEmployeeId(rs.getInt("employee_id"));
        r.setReason(rs.getString("reason"));
        r.setLastWorkingDay(rs.getString("last_working_day"));
        r.setStatus(rs.getString("status"));
        r.setAppliedDate(rs.getString("applied_date"));
        r.setAssetsReturned(rs.getBoolean("assets_returned"));
        r.setPendingTasksCompleted(rs.getBoolean("pending_tasks_completed"));
        r.setDocumentsCleared(rs.getBoolean("documents_cleared"));
        r.setHrReviewCompleted(rs.getBoolean("hr_review_completed"));
        r.setExitInterviewCompleted(rs.getBoolean("exit_interview_completed"));
        r.setRelievingLetterIssued(rs.getBoolean("relieving_letter_issued"));
        r.setExitCompleted(rs.getBoolean("exit_completed"));
        try { r.setEmployeeName(rs.getString("employee_name")); } catch (SQLException ignored) {}
        return r;
    }
}