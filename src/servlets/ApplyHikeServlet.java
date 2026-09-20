package servlets;

import dao.EmployeeDAO;
import model.Employee;
import util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet("/ApplyHikeServlet")
public class ApplyHikeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmployeeDAO dao = new EmployeeDAO();
        List<Employee> employees = dao.getAllEmployees();
        request.setAttribute("employees", employees);
        request.getRequestDispatcher("applyHike.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        double hikePercent = Double.parseDouble(request.getParameter("hikePercent"));

        EmployeeDAO dao = new EmployeeDAO();
        dao.applyHike(employeeId, hikePercent);

        notifyEmployee(employeeId, hikePercent);

        response.sendRedirect("ApplyHikeServlet");
    }

    private void notifyEmployee(int employeeId, double hikePercent) {
        Employee emp = new EmployeeDAO().getEmployeeProfile(employeeId);
        if (emp != null) {
            EmailUtil.sendEmail(emp.getEmail(),
                    "Annual Hike Applied",
                    "Hi " + emp.getName() + ",\n\nA salary hike of " + hikePercent +
                    "% has been applied to your account. Your updated salary is reflected in your profile.\n\nRegards,\nHR Team");
        }
    }
}
