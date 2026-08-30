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
        <tr><th>Document</th><th>Status</th><th>File</th><th>Action</th></tr>
        <% for (Document d : docs) { %>
        <tr>
            <td><%= d.getDocumentName() %></td>
            <td><span class="status-<%= d.getStatus().toLowerCase() %>"><%= d.getStatus() %></span></td>
            <td>
                <% if (d.getFileName() != null) { %>
                    <a href="DownloadDocumentServlet?documentId=<%= d.getDocumentId() %>" target="_blank"><%= d.getFileName() %></a>
                <% } else { %>
                    Not uploaded
                <% } %>
            </td>
            <td>
                <% if (d.getStatus().equals("PENDING") || d.getStatus().equals("REJECTED")) { %>
                <form action="MyDocumentsServlet" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="documentId" value="<%= d.getDocumentId() %>">
                    <input type="file" name="file" required><br><br>
                    <button type="submit">Upload & Submit</button>
                </form>
                <% } %>
            </td>
        </tr>
        <% } %>
    </table>
    <br><a href="employeeDashboard.jsp">Back to Dashboard</a>
</body>
</html>