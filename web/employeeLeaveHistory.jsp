<%@ page import="model.Leave, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Leave History</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Leave History</h2>
    <%
        List<Leave> history = (List<Leave>) request.getAttribute("history");
    %>
    <table>
        <tr><th>Type</th><th>Dates</th><th>Days</th><th>Reason</th><th>Status</th></tr>
        <% for (Leave l : history) { %>
        <tr>
            <td><%= l.getLeaveType() %></td>
            <td><%= l.getStartDate() %> to <%= l.getEndDate() %></td>
            <td><%= l.getDaysRequested() %></td>
            <td><%= l.getReason() %></td>
            <td><%= l.getStatus() %></td>
        </tr>
        <% } %>
    </table>
    <br><a href="javascript:history.back()">Back</a>
</body>
</html>