package servlets;

import dao.EmployeeDAO;
import model.Employee;
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

        Employee emp = new Employee();
        emp.setName(name);
        emp.setEmail(email);
        emp.setDepartment(request.getParameter("department"));
        emp.setDesignation(request.getParameter("designation"));
        emp.setEmployeeCode(request.getParameter("employeeCode"));
        emp.setDob(request.getParameter("dob"));
        emp.setPhone(request.getParameter("phone"));
        emp.setPersonalEmail(request.getParameter("personalEmail"));
        emp.setEmergencyContact(request.getParameter("emergencyContact"));
        emp.setCurrentAddress(request.getParameter("currentAddress"));
        emp.setPermanentAddress(request.getParameter("permanentAddress"));
        emp.setEmploymentType(request.getParameter("employmentType"));
        emp.setWorkLocation(request.getParameter("workLocation"));
        emp.setFirstSalary(request.getParameter("firstSalary"));
        emp.setPfEsiAmount(request.getParameter("pfEsiAmount"));

        EmployeeDAO dao = new EmployeeDAO();
        boolean success = dao.addEmployee(emp, password);

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
            response.getWriter().println("Failed to add employee (email may already be active). <a href='addEmployee.html'>Try again</a>");
        }
    }
}
