package no.kristiania.httpServer.controllers;

import no.kristiania.httpServer.HttpMessage;
import no.kristiania.httpServer.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class FileController implements HttpController {

    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException, SQLException {
        String requestLine = request.getStartLine();
        String requestTarget = requestLine.split(" ")[1];
        int questionPos = requestTarget.indexOf('?');
        String requestPath = questionPos != -1 ? requestTarget.substring(0, questionPos) : requestTarget;

        try (InputStream inputStream = getClass().getResourceAsStream(requestPath)) {
            if (inputStream == null) {
                outputStream.write(("HTTP/1.1 404 Not found\r\n" +
                        "Content-Length: 9\r\n" +
                        "Connenction: close\r\n" +
                        "\r\n" +
                        "Not found").getBytes("UTF-8"));
                return;
            }
            if (requestPath.equals("/")) {
                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: /index.html\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes("UTF-8"));
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            inputStream.transferTo(buffer);

            String contentType = "text/plain";
            if (requestPath.endsWith(".html")) {
                contentType = "text/html";
            } else if (requestPath.endsWith(".css")) {
                contentType = "text/css";
            } else {
                contentType = "text/plain";
            }

            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Length: " + buffer.toByteArray().length + "\r\n" +
                    "Connection: close\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "\r\n";

            clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
            clientSocket.getOutputStream().write(buffer.toByteArray());
        }
    }
}
