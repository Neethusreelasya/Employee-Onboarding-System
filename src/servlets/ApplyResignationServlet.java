package servlets;

import dao.ResignationDAO;
import util.EmailUtil;
import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;

@WebServlet("/ApplyResignationServlet")
public class ApplyResignationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("applyResignation.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int employeeId = (int) session.getAttribute("employeeId");
        String reason = request.getParameter("reason");
        String lastWorkingDay = request.getParameter("lastWorkingDay");

        ResignationDAO dao = new ResignationDAO();
        dao.applyResignation(employeeId, reason, lastWorkingDay);

        notifyManagers();

        response.sendRedirect("MyResignationServlet");
    }

    private void notifyManagers() {
        String sql = "SELECT email FROM users WHERE role = 'MANAGER'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                EmailUtil.sendEmail(rs.getString("email"),
                        "New Resignation Request",
                        "An employee has submitted a resignation request awaiting your review. Please log in to the Onboarding System.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}