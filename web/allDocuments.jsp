<%@ page import="model.Document, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>All Documents</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>All Documents</h2>
    <%
        List<Document> allDocs = (List<Document>) request.getAttribute("allDocs");
    %>
    <% if (allDocs.isEmpty()) { %>
        <p>No documents found.</p>
    <% } else { %>
    <table border="1" cellpadding="8">
        <tr><th>Document</th><th>Employee</th><th>Status</th><th>File</th><th>Action</th></tr>
        <% for (Document d : allDocs) { %>
        <tr>
            <td><%= d.getDocumentName() %></td>
            <td><%= d.getEmployeeName() %></td>
            <td><span class="status-<%= d.getStatus().toLowerCase() %>"><%= d.getStatus() %></span></td>
            <td>
                <% if (d.getFileName() != null) { %>
                    <a href="DownloadDocumentServlet?documentId=<%= d.getDocumentId() %>" target="_blank">View File</a>
                <% } else { %>
                    No file
                <% } %>
            </td>
            <td>
                <form action="RemoveDocumentServlet" method="post" onsubmit="return confirm('Delete this document permanently? This cannot be undone.');">
                    <input type="hidden" name="documentId" value="<%= d.getDocumentId() %>">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
    <% } %>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>