package servlets;

import dao.UserDAO;
import model.User;
import util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

@WebServlet("/AddAdminServlet")
public class AddAdminServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("addAdmin.html").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.html");
            return;
        }

        response.setContentType("text/html");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("ADMIN");

        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.registerUser(user);

        if (success) {
            String subject = "Your HR/Admin Account Credentials";
            String body = "Hi " + name + ",\n\n" +
                    "An HR/Admin account has been created for you.\n" +
                    "Login Email: " + email + "\n" +
                    "Password: " + password + "\n\n" +
                    "Please log in and change your password after first login.\n\n" +
                    "Regards,\nOnboarding System";

            EmailUtil.sendEmail(email, subject, body);

            List<User> admins = userDAO.getAllAdmins();

            StringBuilder html = new StringBuilder();
            html.append("<html><head><style>")
                .append("body{font-family:Arial,sans-serif;margin:40px;}")
                .append("table{border-collapse:collapse;width:60%;margin-top:20px;}")
                .append("th,td{border:1px solid #ccc;padding:8px 12px;text-align:left;}")
                .append("th{background:#6a0dad;color:white;}")
                .append("tr:nth-child(even){background:#f9f9f9;}")
                .append("</style></head><body>");

            html.append("<p style='color:green;font-weight:bold;'>HR account created successfully.</p>");
            html.append("<a href='adminDashboard.jsp'>Back to Dashboard</a>");

            html.append("<h3>Current HR/Admin Accounts</h3>");
            html.append("<table><tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th></tr>");

            for (User a : admins) {
                html.append("<tr>")
                    .append("<td>").append(a.getUserId()).append("</td>")
                    .append("<td>").append(a.getName()).append("</td>")
                    .append("<td>").append(a.getEmail()).append("</td>")
                    .append("<td>").append(a.getRole()).append("</td>")
                    .append("</tr>");
            }

            html.append("</table></body></html>");

            response.getWriter().println(html.toString());
        } else {
            response.getWriter().println("Failed to create account (email may already exist). <a href='AddAdminServlet'>Try again</a>");
        }
    }
}