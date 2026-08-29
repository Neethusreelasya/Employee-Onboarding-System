package servlets;

import dao.EmployeeDAO;
import model.Employee;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet("/EmployeeListServlet")
public class EmployeeListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EmployeeDAO dao = new EmployeeDAO();
        List<Employee> employees = dao.getAllEmployees();
        request.setAttribute("employees", employees);
        request.getRequestDispatcher("employeeList.jsp").forward(request, response);
    }
}
