<%@ page import="model.Resignation, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Resignation Requests</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Resignation Requests Awaiting Your Decision</h2>
    <%
        List<Resignation> pending = (List<Resignation>) request.getAttribute("pending");
    %>
    <table>
        <tr><th>Employee</th><th>Reason</th><th>Last Working Day</th><th>Action</th></tr>
        <% for (Resignation r : pending) { %>
        <tr>
            <td><%= r.getEmployeeName() %></td>
            <td><%= r.getReason() %></td>
            <td><%= r.getLastWorkingDay() %></td>
            <td>
                <form action="ManagerResignationServlet" method="post" style="display:inline;">
                    <input type="hidden" name="resignationId" value="<%= r.getId() %>">
                    <input type="hidden" name="action" value="approve">
                    <button type="submit">Approve</button>
                </form>
                <form action="ManagerResignationServlet" method="post" style="display:inline;">
                    <input type="hidden" name="resignationId" value="<%= r.getId() %>">
                    <input type="hidden" name="action" value="reject">
                    <button type="submit" class="reject-btn">Reject</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
    <br><a href="managerDashboard.jsp">Back to Dashboard</a>
</body>
</html>