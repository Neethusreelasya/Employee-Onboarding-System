<%@ page import="model.Termination, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Termination Exit Checklist</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Termination Exit Checklist - HR</h2>
    <%
        List<Termination> terminations = (List<Termination>) request.getAttribute("terminations");
    %>
    <% if (terminations.isEmpty()) { %>
        <p>No termination exit processes currently in progress.</p>
    <% } %>
    <% for (Termination t : terminations) { %>
    <div style="background:white; padding:20px; border-radius:8px; margin-bottom:20px; max-width:400px; box-shadow:0 2px 8px rgba(0,0,0,0.08);">
        <h3>Employee: <%= t.getEmployeeName() %></h3>
        <p>Reason: <%= t.getReason() %></p>
        <form action="TerminationChecklistServlet" method="post">
            <input type="hidden" name="terminationId" value="<%= t.getId() %>">

            <label><input type="checkbox" name="assets" <%= t.isAssetsReturned() ? "checked" : "" %>> Assets Returned</label><br>
            <label><input type="checkbox" name="tasks" <%= t.isPendingTasksCompleted() ? "checked" : "" %>> Pending Tasks Completed</label><br>
            <label><input type="checkbox" name="docs" <%= t.isDocumentsCleared() ? "checked" : "" %>> Documents Cleared</label><br>
            <label><input type="checkbox" name="hrReview" <%= t.isHrReviewCompleted() ? "checked" : "" %>> HR Review Completed</label><br>
            <label><input type="checkbox" name="exitInterview" <%= t.isExitInterviewCompleted() ? "checked" : "" %>> Exit Interview Completed</label><br>
            <label><input type="checkbox" name="relievingLetter" <%= t.isRelievingLetterIssued() ? "checked" : "" %>> Relieving Letter Issued</label><br><br>

            <button type="submit">Save / Complete Exit Process</button>
        </form>
    </div>
    <% } %>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>