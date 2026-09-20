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
    <table>
        <tr><th>Name</th><th>Email</th></tr>
        <% for (Employee e : employees) { %>
        <tr>
            <td><%= e.getName() %></td>
            <td><%= e.getEmail() %></td>
        </tr>
        <% } %>
    </table>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>
