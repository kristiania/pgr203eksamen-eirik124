package no.kristiania.httpServer;

import no.kristiania.database.ProjectDao;
import no.kristiania.database.ProjectMember;
import no.kristiania.database.ProjectMemberDao;
import no.kristiania.database.TaskDao;
import no.kristiania.httpServer.controllers.*;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class HttpServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private ProjectMemberDao projectMemberDao;
    private ProjectDao projectDao;
    private TaskDao taskDao;

    private Map<String, ControllerMcControllerface> controllers;
    private final ServerSocket serverSocket;

    public HttpServer(int port, DataSource dataSource) throws IOException {
        projectMemberDao = new ProjectMemberDao(dataSource);
        projectDao = new ProjectDao(dataSource);
        taskDao = new TaskDao(dataSource);

        controllers = Map.of(
                "/api/newProject", new ProjectPostController(projectDao),
                "/api/projects", new ProjectGetController(projectDao),
                "/api/projectMembers", new ProjectMemberGetController(projectMemberDao),
                "/api/newProjectMember", new ProjectMemberPostController(projectMemberDao),
                "/api/newTask", new TaskPostController(taskDao),
                "/api/tasks", new TaskGetController(taskDao)
        );

        serverSocket = new ServerSocket(port);

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

        if (requestMethod.equals("POST")) {
            getController(requestPath).handle(request, clientSocket);

        } else {
            if (requestPath.equals("/echo")) {
                handleEchoRequest(clientSocket, requestTarget, questionPos);
            } else if (requestPath.equals("/api/projectMembers")) {
                ControllerMcControllerface controller = controllers.get(requestPath);
                controller.handle(request, clientSocket);
            } else if(requestPath.equals("/api/projects")) {
                ControllerMcControllerface controller = controllers.get(requestPath);
                controller.handle(request, clientSocket);
            } else if (requestPath.equals("/api/tasks")) {
                ControllerMcControllerface controller = controllers.get(requestPath);
                controller.handle(request, clientSocket);
            } else {
                ControllerMcControllerface controller = controllers.get(requestPath);
                if (controller != null) {
                    controller.handle(request, clientSocket);
                } else {
                    handleFileRequest(clientSocket, requestPath);
                }
            }
        }
    }

    private ControllerMcControllerface getController(String requestPath) {
        return controllers.get(requestPath);
    }

    private void handleFileRequest(Socket clientSocket, String requestPath) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(requestPath)) {
            if (inputStream == null) {
                String body = requestPath + " does not exist";
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



    private void handleEchoRequest(Socket clientSocket, String requestTarget, int questionPos) throws IOException {
        String statusCode = "200";
        String body = "Hello <strong>World</strong>!";
        if (questionPos != -1) {
            QueryString queryString = new QueryString(requestTarget.substring(questionPos + 1));
            if (queryString.getParameter("status") != null) {
                statusCode = queryString.getParameter("status");
            }
            if (queryString.getParameter("body") != null) {
                body = queryString.getParameter("body");
            }
        }
        String response = "HTTP/1.1 " + statusCode + " OK\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
    }

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader("pgr203.properties")) {
            properties.load(fileReader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));

        logger.info("Using database {}", dataSource.getUrl());
        Flyway.configure().dataSource(dataSource).load().migrate();

        HttpServer server = new HttpServer(8080, dataSource);
        logger.info("Started on http://localhost:{}/index.html", 8080);

    }


    public List<ProjectMember> getProjectMembers() throws SQLException {
        return projectMemberDao.list();
    }
}