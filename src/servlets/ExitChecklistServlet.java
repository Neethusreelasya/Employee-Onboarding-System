package servlets;

import dao.ResignationDAO;
import model.Resignation;
import util.EmailUtil;
import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/ExitChecklistServlet")
public class ExitChecklistServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        ResignationDAO dao = new ResignationDAO();
        List<Resignation> list = dao.getAcceptedInProgress();

        request.setAttribute("resignations", list);
        request.getRequestDispatcher("exitChecklist.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int resignationId = Integer.parseInt(request.getParameter("resignationId"));
        boolean assets = request.getParameter("assets") != null;
        boolean tasks = request.getParameter("tasks") != null;
        boolean docs = request.getParameter("docs") != null;
        boolean hrReview = request.getParameter("hrReview") != null;
        boolean exitInterview = request.getParameter("exitInterview") != null;
        boolean relievingLetter = request.getParameter("relievingLetter") != null;

        ResignationDAO dao = new ResignationDAO();
        dao.updateChecklist(resignationId, assets, tasks, docs, hrReview, exitInterview, relievingLetter);

        if (assets && tasks && docs && hrReview && exitInterview && relievingLetter) {
            notifyEmployeeFinal(resignationId);
            sendRelievingLetter(resignationId);
        }

        response.sendRedirect("ExitChecklistServlet");
    }

    private void notifyEmployeeFinal(int resignationId) {
        String sql = "SELECT u.email AS email, u.name AS name FROM resignations r " +
                     "JOIN employees e ON r.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id WHERE r.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resignationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                EmailUtil.sendEmail(rs.getString("email"),
                        "Exit Process Completed",
                        "Hi " + rs.getString("name") + ",\n\nYour exit process has been completed. " +
                        "We wish you the best in your future endeavors.\n\nRegards,\nHR Team");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendRelievingLetter(int resignationId) {
        String sql = "SELECT u.email AS email, u.name AS name, r.last_working_day AS lwd " +
                     "FROM resignations r " +
                     "JOIN employees e ON r.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id WHERE r.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resignationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String lastWorkingDay = rs.getString("lwd");

                String subject = "Relieving Letter";
                String body = "Dear " + name + ",\n\n" +
                        "This is to certify that you were relieved from your duties at our organization, " +
                        "with your last working day being " + lastWorkingDay + ".\n\n" +
                        "We confirm that all exit formalities, including asset return, task handover, " +
                        "document clearance, HR review, and exit interview, have been successfully completed.\n\n" +
                        "We thank you for your contributions and wish you success in your future endeavors.\n\n" +
                        "Regards,\nHR Team";

                EmailUtil.sendEmail(email, subject, body);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}