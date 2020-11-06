package no.kristiania.httpServer.controllers;

import no.kristiania.database.Task;
import no.kristiania.database.TaskDao;
import no.kristiania.httpServer.HttpMessage;
import no.kristiania.httpServer.QueryString;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class TaskController implements HttpController {
    private TaskDao dao;
    private String body;

    public TaskController(TaskDao dao) {
        this.dao = dao;
    }

    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException, SQLException {
        try {
            if (requestMethod.equals("POST")) {
                QueryString requestParameter = new QueryString(request.getBody());

                Task task = new Task();
                task.setName(URLDecoder.decode(requestParameter.getParameter("task_name"), StandardCharsets.UTF_8));
                dao.insert(task);

                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: /newTask.html\r\n" +
                        "Transfer-Encoding: chunked" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes("UTF-8"));
            } else {
                body = getBody();
                String status = "200";

                String response = "HTTP/1.1 "+ status +" OK\r\n" +
                        "Content-Length: " + body.length() + "\r\n" +
                        "Content-Type: text/html; application/x-www-form-urlencoded\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        body;

                clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
            }
        } catch (SQLException e) {
            String message = e.toString();
            outputStream.write(("HTTP/1.1 500 Internal server error\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + message.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    message).getBytes("UTF-8"));
        }
    }

    public String getBody() throws SQLException {
        return dao.list().stream()
                .map(dao -> String.format("<option id='" + dao.getId() + "'>" + dao.getName() + "</option> "))
                .collect(Collectors.joining(""));
    }
}
