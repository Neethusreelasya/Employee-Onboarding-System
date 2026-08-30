package servlets;

import dao.DocumentDAO;
import model.Document;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet("/DownloadDocumentServlet")
public class DownloadDocumentServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("login.html");
            return;
        }

        int documentId = Integer.parseInt(request.getParameter("documentId"));
        DocumentDAO documentDAO = new DocumentDAO();
        Document doc = documentDAO.getDocumentFile(documentId);

        if (doc == null || doc.getFileData() == null) {
            response.getWriter().println("No file found for this document.");
            return;
        }

        response.setContentType(doc.getFileType() != null ? doc.getFileType() : "application/octet-stream");
        response.setHeader("Content-Disposition", "inline; filename=\"" + doc.getFileName() + "\"");
        response.getOutputStream().write(doc.getFileData());
        response.getOutputStream().flush();
    }
}
