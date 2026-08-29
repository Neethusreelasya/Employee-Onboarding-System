package servlets;

import dao.TaskDAO;
import model.Task;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet("/VerifyTasksServlet")
public class VerifyTasksServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        TaskDAO taskDAO = new TaskDAO();
        List<Task> submittedTasks = taskDAO.getSubmittedTasks();

        request.setAttribute("submittedTasks", submittedTasks);
        request.getRequestDispatcher("verifyTasks.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int employeeTaskId = Integer.parseInt(request.getParameter("employeeTaskId"));
        String action = request.getParameter("action");   // "approve" or "reject"

        TaskDAO taskDAO = new TaskDAO();
        String newStatus = action.equals("approve") ? "VERIFIED" : "PENDING";
        taskDAO.updateTaskVerification(employeeTaskId, newStatus);

        response.sendRedirect("VerifyTasksServlet");
    }
}
