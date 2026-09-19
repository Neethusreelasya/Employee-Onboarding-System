package servlets;

import dao.LeaveDAO;
import model.Leave;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet("/MyLeavesServlet")
public class MyLeavesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int employeeId = (int) session.getAttribute("employeeId");
        LeaveDAO leaveDAO = new LeaveDAO();

        List<Leave> leaves = leaveDAO.getLeavesByEmployee(employeeId);

        int sickUsed = leaveDAO.getUsedLeaveDays(employeeId, "Sick Leave");
        int casualUsed = leaveDAO.getUsedLeaveDays(employeeId, "Casual Leave");
        int earnedUsed = leaveDAO.getUsedLeaveDays(employeeId, "Earned Leave");

        request.setAttribute("leaves", leaves);
        request.setAttribute("sickUsed", sickUsed);
        request.setAttribute("sickLeft", 6 - sickUsed);
        request.setAttribute("casualUsed", casualUsed);
        request.setAttribute("casualLeft", 12 - casualUsed);
        request.setAttribute("earnedUsed", earnedUsed);
        request.setAttribute("earnedLeft", 15 - earnedUsed);

        request.getRequestDispatcher("myLeaves.jsp").forward(request, response);
    }
}
