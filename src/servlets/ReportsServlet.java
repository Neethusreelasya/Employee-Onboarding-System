package servlets;

import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;

@WebServlet("/ReportsServlet")
public class ReportsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int totalEmployees = 0, pendingTasks = 0, completedTasks = 0;

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement s1 = conn.prepareStatement("SELECT COUNT(*) FROM employees")) {
                ResultSet rs1 = s1.executeQuery();
                if (rs1.next()) totalEmployees = rs1.getInt(1);
            }

            try (PreparedStatement s2 = conn.prepareStatement("SELECT COUNT(*) FROM employee_tasks WHERE status = 'PENDING'")) {
                ResultSet rs2 = s2.executeQuery();
                if (rs2.next()) pendingTasks = rs2.getInt(1);
            }

            try (PreparedStatement s3 = conn.prepareStatement("SELECT COUNT(*) FROM employee_tasks WHERE status = 'COMPLETED'")) {
                ResultSet rs3 = s3.executeQuery();
                if (rs3.next()) completedTasks = rs3.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        int totalTasks = pendingTasks + completedTasks;
        int avgProgress = (totalTasks == 0) ? 0 : (completedTasks * 100 / totalTasks);

        request.setAttribute("totalEmployees", totalEmployees);
        request.setAttribute("pendingTasks", pendingTasks);
        request.setAttribute("completedTasks", completedTasks);
        request.setAttribute("avgProgress", avgProgress);

        request.getRequestDispatcher("reports.jsp").forward(request, response);
    }
}
