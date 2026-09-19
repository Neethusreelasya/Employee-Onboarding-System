package servlets;

import dao.ResignationDAO;
import model.Resignation;
import util.EmailUtil;
import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/ManagerResignationServlet")
public class ManagerResignationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"MANAGER".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        ResignationDAO dao = new ResignationDAO();
        List<Resignation> pending = dao.getPendingForManager();

        request.setAttribute("pending", pending);
        request.getRequestDispatcher("managerResignations.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int resignationId = Integer.parseInt(request.getParameter("resignationId"));
        String action = request.getParameter("action");
        boolean approved = action.equals("approve");

        ResignationDAO dao = new ResignationDAO();
        dao.managerDecision(resignationId, approved);

        notifyEmployee(resignationId, approved);
        if (approved) notifyHR();

        response.sendRedirect("ManagerResignationServlet");
    }

    private void notifyEmployee(int resignationId, boolean approved) {
        String sql = "SELECT u.email AS email, u.name AS name FROM resignations r " +
                     "JOIN employees e ON r.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id WHERE r.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resignationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                EmailUtil.sendEmail(rs.getString("email"),
                        "Resignation Request " + (approved ? "Approved by Manager" : "Rejected"),
                        "Hi " + rs.getString("name") + ",\n\nYour resignation request has been " +
                        (approved ? "approved by your manager and forwarded to HR for final processing." : "rejected by your manager.") +
                        "\n\nRegards,\nHR Team");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void notifyHR() {
        String sql = "SELECT email FROM users WHERE role = 'ADMIN'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                EmailUtil.sendEmail(rs.getString("email"),
                        "Resignation Approved by Manager - Action Needed",
                        "A resignation request has been approved by the manager. Please log in to give final HR approval.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
