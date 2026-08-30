package servlets;

import dao.DocumentDAO;
import model.Document;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet("/VerifyDocumentsServlet")
public class VerifyDocumentsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        DocumentDAO documentDAO = new DocumentDAO();
        List<Document> submittedDocs = documentDAO.getSubmittedDocuments();

        request.setAttribute("submittedDocs", submittedDocs);
        request.getRequestDispatcher("verifyDocuments.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int documentId = Integer.parseInt(request.getParameter("documentId"));
        String action = request.getParameter("action");   // "approve" or "reject"

        DocumentDAO documentDAO = new DocumentDAO();
        String newStatus = action.equals("approve") ? "VERIFIED" : "PENDING";
        documentDAO.updateDocumentStatus(documentId, newStatus);

        response.sendRedirect("VerifyDocumentsServlet");
    }
}