<!DOCTYPE html>
<html>
<head>
    <title>Apply for Leave</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Apply for Leave</h2>
    <form action="ApplyLeaveServlet" method="post">
        <label>Leave Type:</label>
        <select name="leaveType" required>
            <option value="Sick Leave">Sick Leave</option>
            <option value="Casual Leave">Casual Leave</option>
            <option value="Earned Leave">Earned Leave</option>
        </select><br><br>

        <label>Reason:</label>
        <input type="text" name="reason" required placeholder="Brief reason"><br><br>

        <label>Start Date:</label>
        <input type="date" name="startDate" required><br><br>

        <label>End Date:</label>
        <input type="date" name="endDate" required><br><br>

        <button type="submit">Submit Request</button>
    </form>
    <br><a href="employeeDashboard.jsp">Back to Dashboard</a>
</body>
</html>