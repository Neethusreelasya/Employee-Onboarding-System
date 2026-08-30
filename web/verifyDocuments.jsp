<%@ page import="model.Document, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Verify Documents</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h2>Pending Document Verifications</h2>
    <%
        List<Document> submittedDocs = (List<Document>) request.getAttribute("submittedDocs");
    %>
    <% if (submittedDocs.isEmpty()) { %>
        <p>No documents awaiting verification.</p>
    <% } else { %>
    <table border="1" cellpadding="8">
        <tr><th>Document (Employee)</th><th>Status</th><th>File</th><th>Action</th></tr>
        <% for (Document d : submittedDocs) { %>
        <tr>
            <td><%= d.getDocumentName() %> (<%= d.getEmployeeName() %>)</td>
            <td><span class="status-<%= d.getStatus().toLowerCase() %>"><%= d.getStatus() %></span></td>
            <td>
                <% if (d.getFileName() != null) { %>
                    <a href="DownloadDocumentServlet?documentId=<%= d.getDocumentId() %>" target="_blank">View File</a>
                <% } else { %>
                    No file
                <% } %>
            </td>
            <td>
                <form action="VerifyDocumentsServlet" method="post" style="display:inline;">
                    <input type="hidden" name="documentId" value="<%= d.getDocumentId() %>">
                    <input type="hidden" name="action" value="approve">
                    <button type="submit">Approve</button>
                </form>
                <form action="VerifyDocumentsServlet" method="post" style="display:inline;">
                    <input type="hidden" name="documentId" value="<%= d.getDocumentId() %>">
                    <input type="hidden" name="action" value="reject">
                    <button type="submit">Reject</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>
    <% } %>
    <br><a href="adminDashboard.jsp">Back to Dashboard</a>
</body>
</html>