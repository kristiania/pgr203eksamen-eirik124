package no.kristiania.httpServer;

import no.kristiania.database.ProjectMember;

import java.io.IOException;
import java.net.Socket;

public class ProjectGetController implements ControllerMcControllerface {
    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException {
        String body = "<ul>";
        body += "</ul>";
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Content-Type: text/html; application/x-www-form-urlencoded\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
    }
}
