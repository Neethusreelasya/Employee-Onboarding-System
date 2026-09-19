package servlets;

import dao.TerminationDAO;
import dao.EmployeeDAO;
import model.Employee;
import util.EmailUtil;
import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/InitiateTerminationServlet")
public class InitiateTerminationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"MANAGER".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        EmployeeDAO empDAO = new EmployeeDAO();
        List<Employee> employees = empDAO.getAllEmployees();

        request.setAttribute("employees", employees);
        request.getRequestDispatcher("initiateTermination.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        String reason = request.getParameter("reason");

        TerminationDAO dao = new TerminationDAO();
        dao.initiateTermination(employeeId, reason);

        notifyHR();

        response.sendRedirect("InitiateTerminationServlet");
    }

    private void notifyHR() {
        String sql = "SELECT email FROM users WHERE role = 'ADMIN'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                EmailUtil.sendEmail(rs.getString("email"),
                        "New Termination Initiated",
                        "The manager has initiated a termination request. Please log in to review and approve it.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
