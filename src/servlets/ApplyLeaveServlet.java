package servlets;

import dao.LeaveDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet("/ApplyLeaveServlet")
public class ApplyLeaveServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("applyLeave.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int employeeId = (int) session.getAttribute("employeeId");
        String leaveType = request.getParameter("leaveType");
        String reason = request.getParameter("reason");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        int days = (int) ChronoUnit.DAYS.between(start, end) + 1;

        LeaveDAO leaveDAO = new LeaveDAO();
        leaveDAO.applyLeave(employeeId, leaveType, reason, startDate, endDate, days);

        response.sendRedirect("MyLeavesServlet");
    }
}
