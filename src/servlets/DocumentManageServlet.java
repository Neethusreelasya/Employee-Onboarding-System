package servlets;

import dao.EmployeeDAO;
import dao.DocumentDAO;
import model.Employee;
import util.DBConnection;
import util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/DocumentManageServlet")
public class DocumentManageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmployeeDAO empDAO = new EmployeeDAO();
        List<Employee> employees = empDAO.getAllEmployees();

        request.setAttribute("employees", employees);
        request.getRequestDispatcher("documentManage.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        String documentName = request.getParameter("documentName");

        DocumentDAO documentDAO = new DocumentDAO();
        boolean success = documentDAO.assignDocument(employeeId, documentName);

        if (success) {
            // Notify the employee by email
            String fetchSql = "SELECT u.email AS email, u.name AS name " +
                               "FROM employees e JOIN users u ON e.user_id = u.id " +
                               "WHERE e.id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(fetchSql)) {

                stmt.setInt(1, employeeId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String toEmail = rs.getString("email");
                    String employeeName = rs.getString("name");

                    String subject = "New Document Requirement - " + documentName;
                    String body = "Hi " + employeeName + ",\n\n" +
                            "A new document has been requested: " + documentName + "\n" +
                            "Please log in to the Onboarding System to submit it.\n\n" +
                            "Regards,\nHR Team";

                    EmailUtil.sendEmail(toEmail, subject, body);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("DocumentManageServlet");
    }
}