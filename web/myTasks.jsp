<%@ page import="model.Task, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Tasks</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>My Tasks</h2>
    <%
        List<Task> tasks = (List<Task>) request.getAttribute("tasks");
    %>
    <table border="1" cellpadding="8">
        <tr><th>Title</th><th>Description</th><th>Status</th><th>Action</th></tr>
        <% for (Task t : tasks) { %>
        <tr>
            <td><%= t.getTitle() %></td>
            <td><%= t.getDescription() %></td>
            <td><span class="status-<%= t.getStatus().toLowerCase() %>"><%= t.getStatus() %></span></td>
            <td>
                <% if (t.getStatus().equals("PENDING") || t.getStatus().equals("REJECTED")) { %>
<form action="MyTasksServlet" method="post">
    <input type="hidden" name="employeeTaskId" value="<%= t.getTaskId() %>">
    <button type="submit">Submit</button>
</form>
<% } %>
            </td>
        </tr>
        <% } %>
    </table>
    <br><a href="employeeDashboard.jsp">Back to Dashboard</a>
</body>
</html>