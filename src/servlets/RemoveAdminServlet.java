package servlets;

import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;

@WebServlet("/RemoveAdminServlet")
public class RemoveAdminServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null
                || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        int userId = Integer.parseInt(request.getParameter("userId"));
        int currentAdminId = (int) session.getAttribute("userId");

        // Safety check: don't allow an admin to delete their own account
        if (userId == currentAdminId) {
            response.getWriter().println("You cannot remove your own account while logged in. <a href='ListAdminsServlet'>Go back</a>");
            return;
        }

        String sql = "DELETE FROM users WHERE id = ? AND role = 'ADMIN'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("ListAdminsServlet");
    }
}
