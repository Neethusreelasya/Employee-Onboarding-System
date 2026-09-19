package servlets;

import dao.LeaveDAO;
import model.Leave;
import util.EmailUtil;
import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/HRLeaveServlet")
public class HRLeaveServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        LeaveDAO leaveDAO = new LeaveDAO();
        List<Leave> pendingForHR = leaveDAO.getPendingForHR();
        List<Leave> approvedByManager = leaveDAO.getManagerApprovedForHR();

        request.setAttribute("pendingForHR", pendingForHR);
        request.setAttribute("approvedByManager", approvedByManager);
        request.getRequestDispatcher("hrLeaves.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int leaveId = Integer.parseInt(request.getParameter("leaveId"));
        String action = request.getParameter("action");

        LeaveDAO leaveDAO = new LeaveDAO();

        if (action.equals("forward")) {
            leaveDAO.forwardToManager(leaveId);
            notifyManagers();
        } else if (action.equals("accept")) {
            leaveDAO.hrFinalDecision(leaveId, true);
            notifyEmployee(leaveId, "ACCEPTED");
        } else if (action.equals("reject")) {
            leaveDAO.hrFinalDecision(leaveId, false);
            notifyEmployee(leaveId, "REJECTED");
        }

        response.sendRedirect("HRLeaveServlet");
    }

    private void notifyManagers() {
        String sql = "SELECT email FROM users WHERE role = 'MANAGER'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                EmailUtil.sendEmail(rs.getString("email"),
                        "Leave Request Awaiting Your Approval",
                        "HR has forwarded a leave request for your review. Please log in to the Onboarding System.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void notifyEmployee(int leaveId, String result) {
        String sql = "SELECT u.email AS email, u.name AS name FROM leaves l " +
                     "JOIN employees e ON l.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id WHERE l.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, leaveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                EmailUtil.sendEmail(rs.getString("email"),
                        "Leave Request " + result,
                        "Hi " + rs.getString("name") + ",\n\nYour leave request has been " + result.toLowerCase() + ".\n\nRegards,\nHR Team");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
