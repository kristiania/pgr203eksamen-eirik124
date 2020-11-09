package no.kristiania.httpServer;

import no.kristiania.database.MemberDao;
import no.kristiania.httpServer.controllers.EchoController;
import no.kristiania.httpServer.controllers.HttpController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private MemberDao memberDao;
    private Map<String, HttpController> controllers = new HashMap<>();
    private final ServerSocket serverSocket;

    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        controllers.put("/echo", new EchoController());
    }

    public void start() {
        logger.info("Started on http://localhost:{}/", 8080);

        new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    handleRequest(clientSocket);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    private void handleRequest(Socket clientSocket) throws IOException, SQLException {
        HttpMessage request = new HttpMessage(clientSocket);
        String requestLine = request.getStartLine();
        System.out.println("REQUEST " + requestLine);

        String requestMethod = requestLine.split(" ")[0];
        String requestTarget = requestLine.split(" ")[1];

        int questionPos = requestTarget.indexOf('?');

        String requestPath = questionPos != -1 ? requestTarget.substring(0, questionPos) : requestTarget;

        HttpController controller = controllers.get(requestPath);
        if ( controller != null) {
            controller.handle(requestMethod, request, clientSocket, clientSocket.getOutputStream());
        } else {
            handleFileRequest(clientSocket, requestPath, clientSocket.getOutputStream());
        }
    }

    public void handleFileRequest(Socket clientSocket, String requestPath, OutputStream outputStream) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(requestPath)) {
            if (inputStream == null) {
                outputStream.write(("HTTP/1.1 404 Not found\r\n" +
                        "Content-Length: 9\r\n" +
                        "Connenction: close\r\n" +
                        "\r\n" +
                        "Not found").getBytes(StandardCharsets.UTF_8));
                return;
            }
            if (requestPath.equals("/")) {
                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: /index.html\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes(StandardCharsets.UTF_8));
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            inputStream.transferTo(buffer);

            String contentType;
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

            clientSocket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
            clientSocket.getOutputStream().write(buffer.toByteArray());
        }
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(8080);
        server.start();
    }

/*    public List<Member> getProjectMembers() throws SQLException {
        return memberDao.list();
    }*/

    public void addController(String path, HttpController controller) {
        controllers.put(path, controller);
    }
}