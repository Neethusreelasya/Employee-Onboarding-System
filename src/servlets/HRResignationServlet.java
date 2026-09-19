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

@WebServlet("/HRResignationServlet")
public class HRResignationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        ResignationDAO dao = new ResignationDAO();
        request.setAttribute("approvedByManager", dao.getApprovedByManagerForHR());
        request.getRequestDispatcher("hrResignations.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int resignationId = Integer.parseInt(request.getParameter("resignationId"));
        String action = request.getParameter("action");
        boolean accepted = action.equals("accept");

        ResignationDAO dao = new ResignationDAO();
        dao.hrFinalDecision(resignationId, accepted);

        notifyEmployee(resignationId, accepted);

        response.sendRedirect("HRResignationServlet");
    }

    private void notifyEmployee(int resignationId, boolean accepted) {
        String sql = "SELECT u.email AS email, u.name AS name FROM resignations r " +
                     "JOIN employees e ON r.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id WHERE r.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resignationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                EmailUtil.sendEmail(rs.getString("email"),
                        "Resignation " + (accepted ? "Accepted" : "Rejected") + " by HR",
                        "Hi " + rs.getString("name") + ",\n\nYour resignation has been " +
                        (accepted ? "officially accepted. HR will now begin your exit formalities." : "rejected by HR.") +
                        "\n\nRegards,\nHR Team");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
