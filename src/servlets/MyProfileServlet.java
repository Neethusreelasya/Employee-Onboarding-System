package servlets;

import dao.EmployeeDAO;
import model.Employee;
import model.SalaryHistory;
import model.PromotionHistory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@WebServlet("/MyProfileServlet")
public class MyProfileServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int employeeId = (int) session.getAttribute("employeeId");
        EmployeeDAO dao = new EmployeeDAO();

        Employee profile = dao.getEmployeeProfile(employeeId);
        List<SalaryHistory> salaryHistory = dao.getSalaryHistory(employeeId);
        List<PromotionHistory> promotionHistory = dao.getPromotionHistory(employeeId);

        String tenure = "N/A";
        if (profile != null && profile.getJoiningDate() != null) {
            try {
                LocalDate joining = LocalDate.parse(profile.getJoiningDate());
                Period period = Period.between(joining, LocalDate.now());
                tenure = period.getYears() + " years, " + period.getMonths() + " months";
            } catch (Exception e) {
                tenure = "N/A";
            }
        }

        request.setAttribute("profile", profile);
        request.setAttribute("salaryHistory", salaryHistory);
        request.setAttribute("promotionHistory", promotionHistory);
        request.setAttribute("tenure", tenure);

        request.getRequestDispatcher("myProfile.jsp").forward(request, response);
    }
}
