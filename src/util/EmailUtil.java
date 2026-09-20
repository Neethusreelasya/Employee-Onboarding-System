package util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EmailUtil {

    private static final String API_KEY = System.getenv("SENDGRID_API_KEY") != null
            ? System.getenv("SENDGRID_API_KEY") : "your_sendgrid_api_key_here";
    private static final String FROM_EMAIL = System.getenv("MAIL_FROM") != null
            ? System.getenv("MAIL_FROM") : "your_verified_sender_email_here";

    public static void sendEmail(String toEmail, String subject, String body) {
        try {
            String jsonBody = "{"
                    + "\"personalizations\":[{\"to\":[{\"email\":\"" + toEmail + "\"}]}],"
                    + "\"from\":{\"email\":\"" + FROM_EMAIL + "\"},"
                    + "\"subject\":\"" + escapeJson(subject) + "\","
                    + "\"content\":[{\"type\":\"text/plain\",\"value\":\"" + escapeJson(body) + "\"}]"
                    + "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.sendgrid.com/v3/mail/send"))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("SendGrid API response: " + response.statusCode() + " " + response.body());

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
