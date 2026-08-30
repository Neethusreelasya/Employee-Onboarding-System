package servlets;

import dao.DocumentDAO;
import model.Document;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/MyDocumentsServlet")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024) // 5MB limit
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
        Part filePart = request.getPart("file");

        DocumentDAO documentDAO = new DocumentDAO();

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            String fileType = filePart.getContentType();

            try (InputStream is = filePart.getInputStream()) {
                byte[] fileBytes = is.readAllBytes();
                documentDAO.saveDocumentFile(documentId, fileName, fileType, fileBytes);
            }
        } else {
            documentDAO.markSubmitted(documentId);
        }

        response.sendRedirect("MyDocumentsServlet");
    }
}