<%@ page import="model.Employee, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Apply Salary Hike</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Apply Annual Hike</h2>
    <form action="ApplyHikeServlet" method="post">
        <label>Employee:</label>
        <select name="employeeId" required>
            <%
                List<Employee> employees = (List<Employee>) request.getAttribute("employees");
                for (Employee e : employees) {
            %>
            <option value="<%= e.getEmployeeId() %>"><%= e.getName() %></option>
            <% } %>
        </select><br><br>

        <label>Hike Percentage:</label>
        <input type="number" step="0.01" name="hikePercent" required placeholder="e.g. 10"><br><br>

        <button type="submit">Apply Hike</button>
    </form>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>