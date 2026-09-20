
package util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EmailUtil {

    // Read ONLY from environment variables. Never type real keys or emails as fallbacks in code.
    private static final String API_KEY = System.getenv("BREVO_API_KEY");
    private static final String FROM_EMAIL = System.getenv("MAIL_FROM");

    public static void sendEmail(String toEmail, String subject, String body) {
        if (API_KEY == null || API_KEY.isBlank() || FROM_EMAIL == null || FROM_EMAIL.isBlank()) {
            System.out.println("Email NOT sent: set BREVO_API_KEY and MAIL_FROM environment variables.");
            return;
        }

        try {
            String jsonBody = "{"
                    + "\"sender\":{\"email\":\"" + FROM_EMAIL + "\"},"
                    + "\"to\":[{\"email\":\"" + toEmail + "\"}],"
                    + "\"subject\":\"" + escapeJson(subject) + "\","
                    + "\"textContent\":\"" + escapeJson(body) + "\""
                    + "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", API_KEY)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Email API response: " + response.statusCode() + " " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "");
    }
}
