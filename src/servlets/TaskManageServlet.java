package servlets;

import dao.EmployeeDAO;
import model.Employee;
import util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/TaskManageServlet")
public class TaskManageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get all task templates
        List<String[]> tasks = new ArrayList<>();
        String sql = "SELECT id, title, department FROM tasks";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tasks.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("title"),
                    rs.getString("department")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Get all employees (to populate assignment dropdown)
        EmployeeDAO empDAO = new EmployeeDAO();
        List<Employee> employees = empDAO.getAllEmployees();

        request.setAttribute("tasks", tasks);
        request.setAttribute("employees", employees);
        request.getRequestDispatcher("taskManage.jsp").forward(request, response);
    }

    // Create a new task template
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String department = request.getParameter("department");

        String sql = "INSERT INTO tasks (title, description, department) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, department);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("TaskManageServlet");
    }
}