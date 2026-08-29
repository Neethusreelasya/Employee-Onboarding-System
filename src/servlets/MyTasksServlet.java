package servlets;

import dao.TaskDAO;
import model.Task;
import util.DBConnection;
import util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/MyTasksServlet")
public class MyTasksServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int employeeId = (int) session.getAttribute("userId");

        TaskDAO taskDAO = new TaskDAO();
        List<Task> tasks = taskDAO.getTasksByEmployee(employeeId);

        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("myTasks.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int employeeTaskId = Integer.parseInt(request.getParameter("employeeTaskId"));
        TaskDAO taskDAO = new TaskDAO();
        taskDAO.markTaskComplete(employeeTaskId);

        // Notify all HR/Admin users by email
        HttpSession session = request.getSession(false);
        String employeeName = (session != null) ? (String) session.getAttribute("name") : "An employee";

        String sql = "SELECT email FROM users WHERE role = 'ADMIN'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String adminEmail = rs.getString("email");
                String subject = "Task Submitted - Verification Needed";
                String body = employeeName + " has submitted a task for your review.\n\n" +
                        "Please log in to the Onboarding System to verify it.\n\n" +
                        "Regards,\nOnboarding System";
                EmailUtil.sendEmail(adminEmail, subject, body);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("MyTasksServlet");
    }
}
