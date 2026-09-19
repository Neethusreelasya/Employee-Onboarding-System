<%@ page import="model.Termination, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Termination Approvals</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Terminations Initiated by Manager</h2>
    <%
        List<Termination> pending = (List<Termination>) request.getAttribute("pending");
    %>
    <table>
        <tr><th>Employee</th><th>Reason</th><th>Date</th><th>Action</th></tr>
        <% for (Termination t : pending) { %>
        <tr>
            <td><%= t.getEmployeeName() %></td>
            <td><%= t.getReason() %></td>
            <td><%= t.getInitiatedDate() %></td>
            <td>
                <form action="HRTerminationServlet" method="post" style="display:inline;">
                    <input type="hidden" name="terminationId" value="<%= t.getId() %>">
                    <button type="submit">Approve Termination</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>