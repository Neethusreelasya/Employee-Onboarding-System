package dao;

import model.Document;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {

    public List<Document> getDocumentsByEmployee(int employeeId) {
        List<Document> docs = new ArrayList<>();
        String sql = "SELECT id, document_name, status, file_name FROM documents WHERE employee_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Document d = new Document();
                d.setDocumentId(rs.getInt("id"));
                d.setDocumentName(rs.getString("document_name"));
                d.setStatus(rs.getString("status"));
                d.setFileName(rs.getString("file_name"));
                docs.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docs;
    }

    // Save the uploaded file and mark the document as SUBMITTED
    public boolean saveDocumentFile(int documentId, String fileName, String fileType, byte[] fileData) {
        String sql = "UPDATE documents SET file_name = ?, file_type = ?, file_data = ?, status = 'SUBMITTED', submitted_date = CURDATE() WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fileName);
            stmt.setString(2, fileType);
            stmt.setBytes(3, fileData);
            stmt.setInt(4, documentId);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch the actual file bytes for download/viewing
    public Document getDocumentFile(int documentId) {
        String sql = "SELECT document_name, file_name, file_type, file_data FROM documents WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, documentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Document d = new Document();
                d.setDocumentId(documentId);
                d.setDocumentName(rs.getString("document_name"));
                d.setFileName(rs.getString("file_name"));
                d.setFileType(rs.getString("file_type"));
                d.setFileData(rs.getBytes("file_data"));
                return d;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean markSubmitted(int documentId) {
        String sql = "UPDATE documents SET status = 'SUBMITTED', submitted_date = CURDATE() WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, documentId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all documents with status SUBMITTED (awaiting HR verification)
    public List<Document> getSubmittedDocuments() {
        List<Document> docs = new ArrayList<>();
        String sql = "SELECT d.id, d.document_name, d.status, d.file_name, u.name AS employee_name " +
                     "FROM documents d " +
                     "JOIN employees e ON d.employee_id = e.id " +
                     "JOIN users u ON e.user_id = u.id " +
                     "WHERE d.status = 'SUBMITTED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Document d = new Document();
                d.setDocumentId(rs.getInt("id"));
                d.setDocumentName(rs.getString("document_name"));
                d.setStatus(rs.getString("status"));
                d.setEmployeeName(rs.getString("employee_name"));
                d.setFileName(rs.getString("file_name"));
                docs.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docs;
    }

    // HR approves or rejects a submitted document
    public boolean updateDocumentStatus(int documentId, String newStatus) {
        String sql = "UPDATE documents SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, documentId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Create a new document requirement for a specific employee
    public boolean assignDocument(int employeeId, String documentName) {
        String sql = "INSERT INTO documents (employee_id, document_name, status) VALUES (?, ?, 'PENDING')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            stmt.setString(2, documentName);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}