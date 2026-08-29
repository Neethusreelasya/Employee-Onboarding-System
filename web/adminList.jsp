<%@ page import="model.User, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Admins</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Admin Accounts</h2>
    <a href="addAdmin.html">+ Add New Admin</a>
    <br><br>
    <%
        List<User> admins = (List<User>) request.getAttribute("admins");
    %>
    <table border="1" cellpadding="8">
        <tr><th>Name</th><th>Email</th><th>Action</th></tr>
        <% for (User u : admins) { %>
        <tr>
            <td><%= u.getName() %></td>
            <td><%= u.getEmail() %></td>
            <td>
                <form action="RemoveAdminServlet" method="post" onsubmit="return confirm('Remove this admin? This cannot be undone.');">
                    <input type="hidden" name="userId" value="<%= u.getUserId() %>">
                    <button type="submit">Remove</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>