package servlets;

import util.DBConnection;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ListAdminsServlet")
public class ListAdminsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        List<User> admins = new ArrayList<>();
        String sql = "SELECT id, name, email FROM users WHERE role = 'ADMIN'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                admins.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("admins", admins);
        request.getRequestDispatcher("adminList.jsp").forward(request, response);
    }
}
