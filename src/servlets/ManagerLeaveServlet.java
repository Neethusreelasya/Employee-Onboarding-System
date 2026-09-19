package servlets;

import dao.LeaveDAO;
import util.EmailUtil;
import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/ManagerLeaveServlet")
public class ManagerLeaveServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"MANAGER".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        LeaveDAO leaveDAO = new LeaveDAO();
        request.setAttribute("pending", leaveDAO.getPendingForManager());
        request.getRequestDispatcher("managerLeaves.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int leaveId = Integer.parseInt(request.getParameter("leaveId"));
        String action = request.getParameter("action");

        LeaveDAO leaveDAO = new LeaveDAO();
        leaveDAO.managerDecision(leaveId, action.equals("approve"));

        notifyHR(action.equals("approve"));

        response.sendRedirect("ManagerLeaveServlet");
    }

    private void notifyHR(boolean approved) {
        String sql = "SELECT email FROM users WHERE role = 'ADMIN'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                EmailUtil.sendEmail(rs.getString("email"),
                        "Manager " + (approved ? "Approved" : "Rejected") + " a Leave Request",
                        "The manager has " + (approved ? "approved" : "rejected") + " a leave request. Please log in to finalize it.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
