package servlets;

import dao.DocumentDAO;
import model.Document;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet("/AllDocumentsServlet")
public class AllDocumentsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        DocumentDAO documentDAO = new DocumentDAO();
        List<Document> allDocs = documentDAO.getAllDocuments();

        request.setAttribute("allDocs", allDocs);
        request.getRequestDispatcher("allDocuments.jsp").forward(request, response);
    }
}
