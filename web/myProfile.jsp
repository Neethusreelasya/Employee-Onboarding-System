<%@ page import="model.Employee, model.SalaryHistory, model.PromotionHistory, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Profile</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>My Profile</h2>
    <%
        Employee p = (Employee) request.getAttribute("profile");
        List<SalaryHistory> salaryHistory = (List<SalaryHistory>) request.getAttribute("salaryHistory");
        List<PromotionHistory> promotionHistory = (List<PromotionHistory>) request.getAttribute("promotionHistory");
        String tenure = (String) request.getAttribute("tenure");
    %>
    <% if (p == null) { %>
        <p>Profile not found.</p>
    <% } else { %>

    <div class="welcome-box">
        <h2><%= p.getName() %> — <%= p.getDesignation() %></h2>
        <p>Employee Code: <%= p.getEmployeeCode() %> | Tenure: <%= tenure %></p>
    </div>

    <h3>Personal & Employment Details</h3>
    <table>
        <tr><th>Date of Birth</th><td><%= p.getDob() %></td></tr>
        <tr><th>Phone</th><td><%= p.getPhone() %></td></tr>
        <tr><th>Personal Email</th><td><%= p.getPersonalEmail() %></td></tr>
        <tr><th>Emergency Contact</th><td><%= p.getEmergencyContact() %></td></tr>
        <tr><th>Current Address</th><td><%= p.getCurrentAddress() %></td></tr>
        <tr><th>Permanent Address</th><td><%= p.getPermanentAddress() %></td></tr>
        <tr><th>Date of Joining</th><td><%= p.getJoiningDate() %></td></tr>
        <tr><th>Department</th><td><%= p.getDepartment() %></td></tr>
        <tr><th>Designation</th><td><%= p.getDesignation() %></td></tr>
        <tr><th>Employment Type</th><td><%= p.getEmploymentType() %></td></tr>
        <tr><th>Work Location</th><td><%= p.getWorkLocation() %></td></tr>
    </table>

    <h3>Compensation & Career Progression</h3>
    <table>
        <tr><th>Current Salary (CTC)</th><td><%= p.getCurrentSalary() %></td></tr>
        <tr><th>First Salary</th><td><%= p.getFirstSalary() %></td></tr>
    </table>

    <h4>Salary History</h4>
    <table>
        <tr><th>Salary</th><th>Hike %</th><th>Effective Date</th></tr>
        <% for (SalaryHistory s : salaryHistory) { %>
        <tr>
            <td><%= s.getSalaryAmount() %></td>
            <td><%= s.getHikePercent() == null ? "-" : s.getHikePercent() %></td>
            <td><%= s.getEffectiveDate() %></td>
        </tr>
        <% } %>
    </table>

    <h4>Promotion History</h4>
    <table>
        <tr><th>Previous Designation</th><th>New Designation</th><th>Date</th></tr>
        <% for (PromotionHistory pr : promotionHistory) { %>
        <tr>
            <td><%= pr.getPreviousDesignation() %></td>
            <td><%= pr.getNewDesignation() %></td>
            <td><%= pr.getPromotionDate() %></td>
        </tr>
        <% } %>
        <% if (promotionHistory.isEmpty()) { %>
        <tr><td colspan="3">No promotions yet</td></tr>
        <% } %>
    </table>

    <h3>Attendance</h3>
    <table>
        <tr><th>Attendance Percentage</th><td><%= p.getAttendancePercentage() %>%</td></tr>
    </table>

    <h3>PF / ESI</h3>
    <table>
        <tr><th>Contribution Amount</th><td><%= p.getPfEsiAmount() %></td></tr>
    </table>

    <% } %>
    <br><a href="employeeDashboard.jsp">Back to Dashboard</a>
</body>
</html>