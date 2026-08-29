<%@ page import="model.Employee, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Employees</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Employees</h2>
    <a href="addEmployee.html">+ Add New Employee</a>
    <br><br>
    <%
        List<Employee> employees = (List<Employee>) request.getAttribute("employees");
    %>
    <table border="1" cellpadding="8">
        <tr><th>Name</th><th>Email</th><th>Department</th><th>Designation</th><th>Action</th></tr>
        <% for (Employee e : employees) { %>
        <tr>
            <td><%= e.getName() %></td>
            <td><%= e.getEmail() %></td>
            <td><%= e.getDepartment() %></td>
            <td><%= e.getDesignation() %></td>
            <td>
                <form action="RemoveEmployeeServlet" method="post" onsubmit="return confirm('Remove this employee? This cannot be undone.');">
                    <input type="hidden" name="userId" value="<%= e.getUserId() %>">
                    <button type="submit">Remove</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>