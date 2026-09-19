<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manager Dashboard</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<%
    HttpSession mgrSession = request.getSession(false);
    if (mgrSession == null || !"MANAGER".equals(mgrSession.getAttribute("role"))) {
        response.sendRedirect("login.html");
        return;
    }
    String name = (String) mgrSession.getAttribute("name");
%>
    <div class="welcome-box">
        <h2>Welcome, <%= name %> (Manager)</h2>
    </div>
    <ul>
        <li><a href="ManagerLeaveServlet">Leave Approvals</a></li>
    <li><a href="ManagerResignationServlet">Resignation Approvals</a></li>
    <li><a href="InitiateTerminationServlet">Terminate Employee</a></li>
    </ul>
    <a href="LogoutServlet">Logout</a>
</body>
</html>