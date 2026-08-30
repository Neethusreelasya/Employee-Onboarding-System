package dao;

import model.Task;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    // Get all tasks assigned to a specific employee
    public List<Task> getTasksByEmployee(int employeeId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT et.id AS et_id, t.title, t.description, et.status " +
                     "FROM employee_tasks et JOIN tasks t ON et.task_id = t.id " +
                     "WHERE et.employee_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Task t = new Task();
                t.setTaskId(rs.getInt("et_id"));
                t.setTitle(rs.getString("title"));
                t.setDescription(rs.getString("description"));
                t.setStatus(rs.getString("status"));
                tasks.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Mark a specific employee_task row as SUBMITTED (waiting for HR verification)
    public boolean markTaskComplete(int employeeTaskId) {
        String sql = "UPDATE employee_tasks SET status = 'SUBMITTED' WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeTaskId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all tasks with status SUBMITTED (awaiting HR verification)
    public List<Task> getSubmittedTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT et.id AS et_id, u.name AS employee_name, t.title, t.description, et.status " +
                     "FROM employee_tasks et " +
                     "JOIN tasks t ON et.task_id = t.id " +
                     "JOIN users u ON et.employee_id = u.id " +
                     "WHERE et.status = 'SUBMITTED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Task t = new Task();
                t.setTaskId(rs.getInt("et_id"));
                t.setTitle(rs.getString("title") + " (" + rs.getString("employee_name") + ")");
                t.setDescription(rs.getString("description"));
                t.setStatus(rs.getString("status"));
                tasks.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // HR approves or rejects a submitted task
    public boolean updateTaskVerification(int employeeTaskId, String newStatus) {
        String sql = "UPDATE employee_tasks SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, employeeTaskId);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a task and any employee_tasks rows linked to it
    public boolean deleteTask(int taskId) {
        String deleteLinks = "DELETE FROM employee_tasks WHERE task_id = ?";
        String deleteTaskSql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement stmt1 = conn.prepareStatement(deleteLinks)) {
                stmt1.setInt(1, taskId);
                stmt1.executeUpdate();
            }

            try (PreparedStatement stmt2 = conn.prepareStatement(deleteTaskSql)) {
                stmt2.setInt(1, taskId);
                int rows = stmt2.executeUpdate();
                return rows > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}