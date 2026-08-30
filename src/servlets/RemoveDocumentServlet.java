package servlets;

import dao.DocumentDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet("/RemoveDocumentServlet")
public class RemoveDocumentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        int documentId = Integer.parseInt(request.getParameter("documentId"));

        DocumentDAO documentDAO = new DocumentDAO();
        documentDAO.deleteDocument(documentId);

        response.sendRedirect("AllDocumentsServlet");
    }
}
