<!DOCTYPE html>
<html>
<head>
    <title>Reports</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Onboarding Reports</h2>
    <%
        Object avgObj = request.getAttribute("avgProgress");
        String avgProgress = (avgObj != null) ? avgObj.toString() : "0";
    %>
    <p>Total Employees: <%= request.getAttribute("totalEmployees") %></p>
    <p>Pending Tasks: <%= request.getAttribute("pendingTasks") %></p>
   
   <p>Submitted Tasks (awaiting verification): <%= request.getAttribute("submittedTasks") %></p>
    <p>Average Progress:</p>
    <div class="progress-bar-container">
        <div class="progress-bar-fill" id="avgProgressFill">
            <%= avgProgress %>%
        </div>
    </div>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>

    <script>
        document.getElementById('avgProgressFill').style.width = '<%= avgProgress %>%';
    </script>
</body>
</html>