
package servlets;

import dao.DocumentDAO;
import model.Document;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet("/MyDocumentsServlet")
public class MyDocumentsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int employeeId = (int) session.getAttribute("employeeId");
        DocumentDAO documentDAO = new DocumentDAO();
        List<Document> docs = documentDAO.getDocumentsByEmployee(employeeId);

        request.setAttribute("docs", docs);
        request.getRequestDispatcher("myDocuments.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int documentId = Integer.parseInt(request.getParameter("documentId"));
        DocumentDAO documentDAO = new DocumentDAO();
        documentDAO.markSubmitted(documentId);
        response.sendRedirect("MyDocumentsServlet");
    }
}