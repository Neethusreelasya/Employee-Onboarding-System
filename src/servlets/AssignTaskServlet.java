package servlets;

import util.DBConnection;
import util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;

@WebServlet("/AssignTaskServlet")
public class AssignTaskServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int taskId = Integer.parseInt(request.getParameter("taskId"));
        String[] employeeUserIds = request.getParameterValues("employeeUserIds");

        if (employeeUserIds != null) {
            for (String idStr : employeeUserIds) {
                int employeeUserId = Integer.parseInt(idStr);
                assignAndNotify(taskId, employeeUserId);
            }
        }

        response.sendRedirect("TaskManageServlet");
    }

    private void assignAndNotify(int taskId, int employeeUserId) {

        String sql = "INSERT INTO employee_tasks (employee_id, task_id, status) VALUES (?, ?, 'PENDING')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeUserId);
            stmt.setInt(2, taskId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String fetchSql = "SELECT u.email AS email, u.name AS name, t.title AS title " +
                           "FROM users u, tasks t WHERE u.id = ? AND t.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(fetchSql)) {

            stmt.setInt(1, employeeUserId);
            stmt.setInt(2, taskId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String toEmail = rs.getString("email");
                String employeeName = rs.getString("name");
                String taskTitle = rs.getString("title");

                String subject = "New Task Assigned - " + taskTitle;
                String body = "Hi " + employeeName + ",\n\n" +
                        "A new onboarding task has been assigned to you: " + taskTitle + "\n" +
                        "Please log in to the Onboarding System to view details and complete it.\n\n" +
                        "Regards,\nHR Team";

                EmailUtil.sendEmail(toEmail, subject, body);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}