<%@ page import="model.Resignation, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Exit Checklist</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Exit Checklist - HR</h2>
    <%
        List<Resignation> resignations = (List<Resignation>) request.getAttribute("resignations");
    %>
    <% if (resignations.isEmpty()) { %>
        <p>No exit processes currently in progress.</p>
    <% } %>
    <% for (Resignation r : resignations) { %>
    <div style="background:white; padding:20px; border-radius:8px; margin-bottom:20px; max-width:400px; box-shadow:0 2px 8px rgba(0,0,0,0.08);">
        <h3>Employee: <%= r.getEmployeeName() %></h3>
        <p>Last Working Day: <%= r.getLastWorkingDay() %></p>
        <form action="ExitChecklistServlet" method="post">
            <input type="hidden" name="resignationId" value="<%= r.getId() %>">

            <label><input type="checkbox" name="assets" <%= r.isAssetsReturned() ? "checked" : "" %>> Assets Returned</label><br>
            <label><input type="checkbox" name="tasks" <%= r.isPendingTasksCompleted() ? "checked" : "" %>> Pending Tasks Completed</label><br>
            <label><input type="checkbox" name="docs" <%= r.isDocumentsCleared() ? "checked" : "" %>> Documents Cleared</label><br>
            <label><input type="checkbox" name="hrReview" <%= r.isHrReviewCompleted() ? "checked" : "" %>> HR Review Completed</label><br>
            <label><input type="checkbox" name="exitInterview" <%= r.isExitInterviewCompleted() ? "checked" : "" %>> Exit Interview Completed</label><br>
            <label><input type="checkbox" name="relievingLetter" <%= r.isRelievingLetterIssued() ? "checked" : "" %>> Relieving Letter Issued</label><br><br>

            <button type="submit">Save / Complete Exit Process</button>
        </form>
    </div>
    <% } %>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>