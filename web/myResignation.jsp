<%@ page import="model.Resignation" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Resignation</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>My Resignation Status</h2>
    <%
        Resignation r = (Resignation) request.getAttribute("resignation");
    %>
    <% if (r == null) { %>
        <p>You have not submitted a resignation request.</p>
        <a href="ApplyResignationServlet" class="btn">Submit Resignation</a>
    <% } else { %>
        <table>
            <tr><th>Reason</th><td><%= r.getReason() %></td></tr>
            <tr><th>Last Working Day</th><td><%= r.getLastWorkingDay() %></td></tr>
            <tr><th>Status</th><td><%= r.getStatus() %></td></tr>
            <% if (r.isExitCompleted()) { %>
            <tr><th>Exit Process</th><td>Completed ✓</td></tr>
            <% } %>
        </table>
    <% } %>
    <br><a href="employeeDashboard.jsp">Back to Dashboard</a>
</body>
</html>