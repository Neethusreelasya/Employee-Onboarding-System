<%@ page import="model.Employee, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Tasks</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Task Templates</h2>
    <table border="1" cellpadding="8">
        <tr><th>Title</th><th>Department</th><th>Action</th></tr>
        <%
            List<String[]> tasks = (List<String[]>) request.getAttribute("tasks");
            for (String[] t : tasks) {
        %>
        <tr>
            <td><%= t[1] %></td>
            <td><%= t[2] %></td>
            <td>
                <form action="RemoveTaskServlet" method="post" onsubmit="return confirm('Remove this task? This cannot be undone.');">
                    <input type="hidden" name="taskId" value="<%= t[0] %>">
                    <button type="submit">Remove</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>

    <h3>Create New Task</h3>
    <form action="TaskManageServlet" method="post">
        <label>Title:</label>
        <input type="text" name="title" required><br><br>
        <label>Description:</label>
        <input type="text" name="description" required><br><br>
        <label>Department:</label>
        <input type="text" name="department" required><br><br>
        <button type="submit">Create Task</button>
    </form>

    <h3>Assign Task to Employee(s)</h3>
    <form action="AssignTaskServlet" method="post">
        <label>Task:</label>
        <select name="taskId" required>
            <% for (String[] t : tasks) { %>
            <option value="<%= t[0] %>"><%= t[1] %></option>
            <% } %>
        </select><br><br>

        <label>Select Employee(s):</label>
        <div style="background:white; border:1px solid #ddd; border-radius:6px; padding:12px; max-width:300px; max-height:200px; overflow-y:auto; margin-top:6px;">
            <label style="display:block; font-weight:normal; margin-bottom:8px;">
                <input type="checkbox" id="selectAllEmployees" onclick="toggleAllEmployees(this)"> <strong>Select All</strong>
            </label>
            <hr style="margin:6px 0;">
            <%
                List<Employee> employees = (List<Employee>) request.getAttribute("employees");
                for (Employee e : employees) {
            %>
            <label style="display:block; font-weight:normal; margin-bottom:6px;">
                <input type="checkbox" name="employeeUserIds" value="<%= e.getUserId() %>" class="employeeCheckbox">
                <%= e.getName() %>
            </label>
            <% } %>
        </div>
        <br>

        <button type="submit">Assign Task to Selected</button>
    </form>

    <script>
        function toggleAllEmployees(source) {
            var checkboxes = document.getElementsByClassName('employeeCheckbox');
            for (var i = 0; i < checkboxes.length; i++) {
                checkboxes[i].checked = source.checked;
            }
        }
    </script>

    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>