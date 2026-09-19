<%@ page import="model.Resignation, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Resignations - Final HR Decision</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Resignations Approved by Manager (Final HR Decision)</h2>
    <%
        List<Resignation> approvedByManager = (List<Resignation>) request.getAttribute("approvedByManager");
    %>
    <table>
        <tr><th>Employee</th><th>Reason</th><th>Last Working Day</th><th>Action</th></tr>
        <% for (Resignation r : approvedByManager) { %>
        <tr>
            <td><%= r.getEmployeeName() %></td>
            <td><%= r.getReason() %></td>
            <td><%= r.getLastWorkingDay() %></td>
            <td>
                <form action="HRResignationServlet" method="post" style="display:inline;">
                    <input type="hidden" name="resignationId" value="<%= r.getId() %>">
                    <input type="hidden" name="action" value="accept">
                    <button type="submit">Accept</button>
                </form>
                <form action="HRResignationServlet" method="post" style="display:inline;">
                    <input type="hidden" name="resignationId" value="<%= r.getId() %>">
                    <input type="hidden" name="action" value="reject">
                    <button type="submit" class="reject-btn">Reject</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>