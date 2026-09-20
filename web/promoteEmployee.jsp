<%@ page import="model.Employee, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Promote Employee</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Promote Employee</h2>
    <form action="PromoteEmployeeServlet" method="post">
        <label>Employee:</label>
        <select name="employeeId" required>
            <%
                List<Employee> employees = (List<Employee>) request.getAttribute("employees");
                for (Employee e : employees) {
            %>
            <option value="<%= e.getEmployeeId() %>"><%= e.getName() %> (<%= e.getDesignation() %>)</option>
            <% } %>
        </select><br><br>

        <label>New Designation:</label>
        <input type="text" name="newDesignation" required placeholder="e.g. Senior Developer"><br><br>

        <button type="submit">Promote</button>
    </form>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>