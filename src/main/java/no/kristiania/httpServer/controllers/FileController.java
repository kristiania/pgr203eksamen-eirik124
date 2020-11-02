package no.kristiania.httpServer.controllers;

import no.kristiania.httpServer.HttpMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class FileController implements HttpController {
    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException, SQLException {
        try (InputStream inputStream = getClass().getResourceAsStream(request)) {
            if (inputStream == null) {
                String body = request + " does not exist";
                String response = "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: " + body.length() + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        body;
                // Write the response back to the client
                clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
                return;
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            inputStream.transferTo(buffer);

            String contentType = "text/plain";
            if (request.endsWith(".html")) {
                contentType = "text/html";
            } else if (request.endsWith(".css")) {
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
