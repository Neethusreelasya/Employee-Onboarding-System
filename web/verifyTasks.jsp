<%@ page import="model.Task, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Verify Tasks</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Pending Task Verifications</h2>
    <%
        List<Task> submittedTasks = (List<Task>) request.getAttribute("submittedTasks");
    %>
    <% if (submittedTasks.isEmpty()) { %>
        <p>No tasks awaiting verification.</p>
    <% } else { %>
    <table border="1" cellpadding="8">
        <tr><th>Task (Employee)</th><th>Description</th><th>Status</th><th>Action</th></tr>
        <% for (Task t : submittedTasks) { %>
        <tr>
            <td><%= t.getTitle() %></td>
            <td><%= t.getDescription() %></td>
            <td><span class="status-<%= t.getStatus().toLowerCase() %>"><%= t.getStatus() %></span></td>
            <td>
                <form action="VerifyTasksServlet" method="post" style="display:inline;">
                    <input type="hidden" name="employeeTaskId" value="<%= t.getTaskId() %>">
                    <input type="hidden" name="action" value="approve">
                    <button type="submit">Approve</button>
                </form>
                <form action="VerifyTasksServlet" method="post" style="display:inline;">
                    <input type="hidden" name="employeeTaskId" value="<%= t.getTaskId() %>">
                    <input type="hidden" name="action" value="reject">
                    <button type="submit">Reject</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
    <% } %>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>