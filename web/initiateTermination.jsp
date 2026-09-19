<%@ page import="model.Employee, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Terminate Employee</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Initiate Employee Termination</h2>
    <form action="InitiateTerminationServlet" method="post">
        <label>Employee:</label>
        <select name="employeeId" required>
            <%
                List<Employee> employees = (List<Employee>) request.getAttribute("employees");
                for (Employee e : employees) {
            %>
            <option value="<%= e.getEmployeeId() %>"><%= e.getName() %></option>
            <% } %>
        </select><br><br>

        <label>Reason for Termination:</label>
        <input type="text" name="reason" required placeholder="e.g. Repeated policy violations"><br><br>

        <button type="submit" class="reject-btn">Submit Termination</button>
    </form>
    <br><a href="managerDashboard.jsp">Back to Dashboard</a>
</body>
</html>