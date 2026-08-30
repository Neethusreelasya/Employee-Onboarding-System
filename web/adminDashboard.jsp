<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<%
    HttpSession adminSession = request.getSession(false);
    if (adminSession == null || adminSession.getAttribute("userId") == null
        || !"ADMIN".equals(adminSession.getAttribute("role"))) {
        response.sendRedirect("login.html");
        return;
    }
    String name = (String) adminSession.getAttribute("name");
%>
    <div class="welcome-box">
        <h2>Welcome, <%= name %> (Admin)</h2>
    </div>
    <ul>
        <li><a href="EmployeeListServlet">Employees</a></li>
        <li><a href="TaskManageServlet">Tasks</a></li>
        <li><a href="ReportsServlet">Reports</a></li>
        <li><a href="VerifyTasksServlet">Verify Tasks</a></li>
        <li><a href="VerifyDocumentsServlet">Verify Documents</a></li>
        <li><a href="AllDocumentsServlet">All Documents</a></li>
        <li><a href="DocumentManageServlet">Manage Documents</a></li>
        <li><a href="AddAdminServlet">Add HR/Admin</a></li>
        <li><a href="ListAdminsServlet">Manage Admins</a></li>
    </ul>
    <a href="LogoutServlet">Logout</a>
</body>
</html>