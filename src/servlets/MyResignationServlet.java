package servlets;

import dao.ResignationDAO;
import model.Resignation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet("/MyResignationServlet")
public class MyResignationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int employeeId = (int) session.getAttribute("employeeId");
        ResignationDAO dao = new ResignationDAO();
        Resignation resignation = dao.getLatestForEmployee(employeeId);

        request.setAttribute("resignation", resignation);
        request.getRequestDispatcher("myResignation.jsp").forward(request, response);
    }
}