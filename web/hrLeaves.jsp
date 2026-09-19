<%@ page import="model.Leave, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Leaves</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Leave Requests - HR</h2>

    <h3>New Requests (forward to Manager)</h3>
    <%
        List<Leave> pendingForHR = (List<Leave>) request.getAttribute("pendingForHR");
    %>
    <table>
        <tr><th>Employee</th><th>Type</th><th>Dates</th><th>Days</th><th>Reason</th><th>Action</th></tr>
        <% for (Leave l : pendingForHR) { %>
        <tr>
            <td><%= l.getEmployeeName() %></td>
            <td><%= l.getLeaveType() %></td>
            <td><%= l.getStartDate() %> to <%= l.getEndDate() %></td>
            <td><%= l.getDaysRequested() %></td>
            <td><%= l.getReason() %></td>
            <td>
                <form action="HRLeaveServlet" method="post" style="display:inline;">
                    <input type="hidden" name="leaveId" value="<%= l.getId() %>">
                    <input type="hidden" name="action" value="forward">
                    <button type="submit">Forward to Manager</button>
                </form>
                <a href="EmployeeLeaveHistoryServlet?employeeId=<%= l.getEmployeeId() %>">View History</a>
            </td>
        </tr>
        <% } %>
    </table>

    <h3>Approved by Manager (final HR decision)</h3>
    <%
        List<Leave> approvedByManager = (List<Leave>) request.getAttribute("approvedByManager");
    %>
    <table>
        <tr><th>Employee</th><th>Type</th><th>Dates</th><th>Days</th><th>Action</th></tr>
        <% for (Leave l : approvedByManager) { %>
        <tr>
            <td><%= l.getEmployeeName() %></td>
            <td><%= l.getLeaveType() %></td>
            <td><%= l.getStartDate() %> to <%= l.getEndDate() %></td>
            <td><%= l.getDaysRequested() %></td>
            <td>
                <form action="HRLeaveServlet" method="post" style="display:inline;">
                    <input type="hidden" name="leaveId" value="<%= l.getId() %>">
                    <input type="hidden" name="action" value="accept">
                    <button type="submit">Accept</button>
                </form>
                <form action="HRLeaveServlet" method="post" style="display:inline;">
                    <input type="hidden" name="leaveId" value="<%= l.getId() %>">
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