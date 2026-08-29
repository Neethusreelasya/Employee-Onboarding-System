package servlets;

import dao.EmployeeDAO;
import util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet("/AddEmployeeServlet")
public class AddEmployeeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String department = request.getParameter("department");
        String designation = request.getParameter("designation");

        EmployeeDAO dao = new EmployeeDAO();
        boolean success = dao.addEmployee(name, email, password, department, designation);

        if (success) {
            String subject = "Your Onboarding System Account Credentials";
            String body = "Hi " + name + ",\n\n" +
                    "HR has created your onboarding account.\n" +
                    "Login Email: " + email + "\n" +
                    "Password: " + password + "\n\n" +
                    "Please log in and change your password after first login.\n\n" +
                    "Regards,\nHR Team";

            EmailUtil.sendEmail(email, subject, body);

            response.sendRedirect("EmployeeListServlet");
        } else {
            response.getWriter().println("Failed to add employee. <a href='addEmployee.html'>Try again</a>");
        }
    }
}
