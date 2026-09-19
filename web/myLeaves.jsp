<%@ page import="model.Leave, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Leaves</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>My Leave History</h2>
    <a href="ApplyLeaveServlet" class="btn">+ Apply for Leave</a>

    <h3>Balance</h3>
    <table>
        <tr><th>Type</th><th>Used</th><th>Left</th></tr>
        <tr><td>Sick Leave</td><td><%= request.getAttribute("sickUsed") %></td><td><%= request.getAttribute("sickLeft") %></td></tr>
        <tr><td>Casual Leave</td><td><%= request.getAttribute("casualUsed") %></td><td><%= request.getAttribute("casualLeft") %></td></tr>
        <tr><td>Earned Leave</td><td><%= request.getAttribute("earnedUsed") %></td><td><%= request.getAttribute("earnedLeft") %></td></tr>
    </table>

    <h3>History</h3>
    <%
        List<Leave> leaves = (List<Leave>) request.getAttribute("leaves");
    %>
    <table>
        <tr><th>Type</th><th>Dates</th><th>Days</th><th>Reason</th><th>Status</th></tr>
        <% for (Leave l : leaves) { %>
        <tr>
            <td><%= l.getLeaveType() %></td>
            <td><%= l.getStartDate() %> to <%= l.getEndDate() %></td>
            <td><%= l.getDaysRequested() %></td>
            <td><%= l.getReason() %></td>
            <td><%= l.getStatus() %></td>
        </tr>
        <% } %>
    </table>
    <br><a href="employeeDashboard.jsp">Back to Dashboard</a>
</body>
</html>