package servlets;

import dao.UserDAO;
import dao.EmployeeDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String expectedRole = request.getParameter("expectedRole"); // "EMPLOYEE" or "ADMIN"

        UserDAO userDAO = new UserDAO();
        User user = userDAO.validateUser(email, password);

        if (user != null) {

            // NEW CHECK: does the actual role match the box they logged in through?
            if (!user.getRole().equalsIgnoreCase(expectedRole)) {
                response.getWriter().println(
                    "This account is not registered as a " + expectedRole.toLowerCase() +
                    ". Please use the correct login box. <a href='login.html'>Go back</a>"
                );
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("name", user.getName());
            session.setAttribute("role", user.getRole());
            session.setAttribute("email", user.getEmail());

            // NEW: look up and store the employee's employees.id (only applies to EMPLOYEE role)
            if (user.getRole().equalsIgnoreCase("EMPLOYEE")) {
                EmployeeDAO employeeDAO = new EmployeeDAO();
                int employeeId = employeeDAO.getEmployeeIdByUserId(user.getUserId());
                session.setAttribute("employeeId", employeeId);
            }

            if (user.getRole().equals("ADMIN")) {
                response.sendRedirect("adminDashboard.jsp");
            } else {
                response.sendRedirect("employeeDashboard.jsp");
            }

        } else {
            response.getWriter().println("Invalid email or password. <a href='login.html'>Try again</a>");
        }
    }
}