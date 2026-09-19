<!DOCTYPE html>
<html>
<head>
    <title>Submit Resignation</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Submit Resignation</h2>
    <form action="ApplyResignationServlet" method="post">
        <label>Reason:</label>
        <input type="text" name="reason" required placeholder="Reason for resignation"><br><br>

        <label>Proposed Last Working Day:</label>
        <input type="date" name="lastWorkingDay" required><br><br>

        <button type="submit">Submit Resignation</button>
    </form>
    <br><a href="employeeDashboard.jsp">Back to Dashboard</a>
</body>
</html>