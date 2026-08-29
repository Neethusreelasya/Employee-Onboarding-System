package servlets;

import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;

@WebServlet("/MyProgressServlet")
public class MyProgressServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int employeeId = (int) session.getAttribute("userId");
        int total = 0, completed = 0;

        String sql = "SELECT status FROM employee_tasks WHERE employee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                total++;
                if (rs.getString("status").equals("COMPLETED")) completed++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int percent = (total == 0) ? 0 : (completed * 100 / total);

        request.setAttribute("total", total);
        request.setAttribute("completed", completed);
        request.setAttribute("percent", percent);
        request.getRequestDispatcher("myProgress.jsp").forward(request, response);
    }
}
