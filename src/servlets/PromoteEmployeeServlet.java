package servlets;

import dao.EmployeeDAO;
import model.Employee;
import util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet("/PromoteEmployeeServlet")
public class PromoteEmployeeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmployeeDAO dao = new EmployeeDAO();
        List<Employee> employees = dao.getAllEmployees();
        request.setAttribute("employees", employees);
        request.getRequestDispatcher("promoteEmployee.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        String newDesignation = request.getParameter("newDesignation");

        EmployeeDAO dao = new EmployeeDAO();
        dao.promoteEmployee(employeeId, newDesignation);

        notifyEmployee(employeeId, newDesignation);

        response.sendRedirect("PromoteEmployeeServlet");
    }

    private void notifyEmployee(int employeeId, String newDesignation) {
        Employee emp = new EmployeeDAO().getEmployeeProfile(employeeId);
        if (emp != null) {
            EmailUtil.sendEmail(emp.getEmail(),
                    "Congratulations on Your Promotion!",
                    "Hi " + emp.getName() + ",\n\nYou have been promoted to " + newDesignation +
                    ". Congratulations!\n\nRegards,\nHR Team");
        }
    }
}
