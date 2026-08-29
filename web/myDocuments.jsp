<%@ page import="model.Document, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Documents</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>My Documents</h2>
    <%
        List<Document> docs = (List<Document>) request.getAttribute("docs");
    %>
    <table border="1" cellpadding="8">
        <tr><th>Document</th><th>Status</th><th>Action</th></tr>
        <% for (Document d : docs) { %>
        <tr>
            <td><%= d.getDocumentName() %></td>
            <td><span class="status-<%= d.getStatus().toLowerCase() %>"><%= d.getStatus() %></span></td>
            <td>
                <% if (d.getStatus().equals("PENDING") || d.getStatus().equals("REJECTED")) { %>
<form action="MyDocumentsServlet" method="post">
    <input type="hidden" name="documentId" value="<%= d.getDocumentId() %>">
    <button type="submit">Submit</button>
</form>
<% } %>
            </td>
        </tr>
        <% } %>
    </table>
    <br><a href="employeeDashboard.jsp">Back to Dashboard</a>
</body>
</html>