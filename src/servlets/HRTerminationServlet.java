package servlets;

import dao.TerminationDAO;
import model.Termination;
import util.EmailUtil;
import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/HRTerminationServlet")
public class HRTerminationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        TerminationDAO dao = new TerminationDAO();
        request.setAttribute("pending", dao.getPendingForHR());
        request.getRequestDispatcher("hrTerminations.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int terminationId = Integer.parseInt(request.getParameter("terminationId"));

        TerminationDAO dao = new TerminationDAO();
        dao.hrApprove(terminationId);

        notifyEmployee(terminationId);

        response.sendRedirect("HRTerminationServlet");
    }

    private void notifyEmployee(int terminationId) {
        String sql = "SELECT u.email AS email, u.name AS name FROM terminations t " +
                     "JOIN employees e ON t.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id WHERE t.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, terminationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                EmailUtil.sendEmail(rs.getString("email"),
                        "Termination Notice",
                        "Hi " + rs.getString("name") + ",\n\nYour employment has been terminated. HR will now begin the exit formalities.\n\nRegards,\nHR Team");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
