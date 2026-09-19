package servlets;

import dao.TerminationDAO;
import model.Termination;
import util.EmailUtil;
import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/TerminationChecklistServlet")
public class TerminationChecklistServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        TerminationDAO dao = new TerminationDAO();
        List<Termination> list = dao.getApprovedInProgress();

        request.setAttribute("terminations", list);
        request.getRequestDispatcher("terminationChecklist.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int terminationId = Integer.parseInt(request.getParameter("terminationId"));
        boolean assets = request.getParameter("assets") != null;
        boolean tasks = request.getParameter("tasks") != null;
        boolean docs = request.getParameter("docs") != null;
        boolean hrReview = request.getParameter("hrReview") != null;
        boolean exitInterview = request.getParameter("exitInterview") != null;
        boolean relievingLetter = request.getParameter("relievingLetter") != null;

        TerminationDAO dao = new TerminationDAO();
        dao.updateChecklist(terminationId, assets, tasks, docs, hrReview, exitInterview, relievingLetter);

        if (assets && tasks && docs && hrReview && exitInterview && relievingLetter) {
            sendTerminationLetter(terminationId);
        }

        response.sendRedirect("TerminationChecklistServlet");
    }

    private void sendTerminationLetter(int terminationId) {
        String sql = "SELECT u.email AS email, u.name AS name, t.reason AS reason FROM terminations t " +
                     "JOIN employees e ON t.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id WHERE t.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, terminationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String reason = rs.getString("reason");

                String subject = "Termination Letter";
                String body = "Dear " + name + ",\n\n" +
                        "This letter is to formally confirm the termination of your employment with our organization, " +
                        "effective immediately, due to the following reason:\n\n" +
                        reason + "\n\n" +
                        "We confirm that all exit formalities, including asset return, task handover, " +
                        "document clearance, HR review, and exit interview, have been completed.\n\n" +
                        "Regards,\nHR Team";

                EmailUtil.sendEmail(email, subject, body);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
