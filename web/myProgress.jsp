<!DOCTYPE html>
<html>
<head>
    <title>My Progress</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>My Onboarding Progress</h2>
    <%
        Object percentObj = request.getAttribute("percent");
        String percent = (percentObj != null) ? percentObj.toString() : "0";
    %>
    <p>Completed: <%= request.getAttribute("completed") %> / <%= request.getAttribute("total") %></p>
    <div class="progress-bar-container">
        <div class="progress-bar-fill" id="progressFill">
            <%= percent %>%
        </div>
    </div>
    <br><a href="employeeDashboard.jsp">Back to Dashboard</a>

    <script>
        document.getElementById('progressFill').style.width = '<%= percent %>%';
    </script>
</body>
</html>