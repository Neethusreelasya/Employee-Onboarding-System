<%@ page import="model.Employee, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Documents</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Request Document from Employee</h2>
    <form action="DocumentManageServlet" method="post">
        <label>Employee:</label>
        <select name="employeeId">
            <%
                List<Employee> employees = (List<Employee>) request.getAttribute("employees");
                for (Employee e : employees) {
            %>
            <option value="<%= e.getEmployeeId() %>"><%= e.getName() %></option>
            <% } %>
        </select><br><br>

        <label>Document Name:</label>
        <input type="text" name="documentName" required placeholder="e.g. Aadhar Card Copy"><br><br>

        <button type="submit">Request Document</button>
    </form>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>