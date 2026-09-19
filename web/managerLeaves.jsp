<%@ page import="model.Leave, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manager - Leave Approvals</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Leave Requests Awaiting Your Decision</h2>
    <%
        List<Leave> pending = (List<Leave>) request.getAttribute("pending");
    %>
    <table>
        <tr><th>Employee</th><th>Type</th><th>Dates</th><th>Days</th><th>Reason</th><th>Action</th></tr>
        <% for (Leave l : pending) { %>
        <tr>
            <td><%= l.getEmployeeName() %></td>
            <td><%= l.getLeaveType() %></td>
            <td><%= l.getStartDate() %> to <%= l.getEndDate() %></td>
            <td><%= l.getDaysRequested() %></td>
            <td><%= l.getReason() %></td>
            <td>
                <form action="ManagerLeaveServlet" method="post" style="display:inline;">
                    <input type="hidden" name="leaveId" value="<%= l.getId() %>">
                    <input type="hidden" name="action" value="approve">
                    <button type="submit">Approve</button>
                </form>
                <form action="ManagerLeaveServlet" method="post" style="display:inline;">
                    <input type="hidden" name="leaveId" value="<%= l.getId() %>">
                    <input type="hidden" name="action" value="reject">
                    <button type="submit" class="reject-btn">Reject</button>
                </form>
                <a href="EmployeeLeaveHistoryServlet?employeeId=<%= l.getEmployeeId() %>">View History</a>
            </td>
        </tr>
        <% } %>
    </table>
    <br><a href="managerDashboard.jsp">Back to Dashboard</a>
</body>
</html>