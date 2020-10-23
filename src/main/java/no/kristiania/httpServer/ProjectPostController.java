package no.kristiania.httpServer;

import no.kristiania.database.ProjectMember;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class ProjectPostController implements ControllerMcControllerface {
    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException {
        QueryString requestParameter = new QueryString(request.getBody());

        String body = "Okay";
        String response = "HTTP/1.1 200 OK\r\n" +
                "Connection: close\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
    }
}
