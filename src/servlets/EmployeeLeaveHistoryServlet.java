package servlets;

import dao.LeaveDAO;
import model.Leave;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet("/EmployeeLeaveHistoryServlet")
public class EmployeeLeaveHistoryServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || (!"ADMIN".equals(session.getAttribute("role")) && !"MANAGER".equals(session.getAttribute("role")))) {
            response.sendRedirect("login.html");
            return;
        }

        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        LeaveDAO leaveDAO = new LeaveDAO();
        List<Leave> history = leaveDAO.getLeaveHistoryForEmployee(employeeId);

        request.setAttribute("history", history);
        request.getRequestDispatcher("employeeLeaveHistory.jsp").forward(request, response);
    }
}
