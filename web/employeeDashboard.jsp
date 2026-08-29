<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Dashboard</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<%
    HttpSession userSession = request.getSession(false);
    if (userSession == null || userSession.getAttribute("userId") == null) {
        response.sendRedirect("login.html");
        return;
    }
    String name = (String) userSession.getAttribute("name");
%>
    <div class="welcome-box">
        <h2>Welcome, <%= name %></h2>
    </div>
    <ul>
        <li><a href="MyTasksServlet">My Tasks</a></li>
        <li><a href="MyDocumentsServlet">Documents</a></li>
        <li><a href="MyProgressServlet">Progress</a></li>
    </ul>
    <a href="LogoutServlet">Logout</a>
</body>
</html>