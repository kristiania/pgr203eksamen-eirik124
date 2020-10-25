package no.kristiania.httpServer.controllers;

import no.kristiania.database.Task;
import no.kristiania.database.TaskDao;
import no.kristiania.httpServer.HttpMessage;
import no.kristiania.httpServer.QueryString;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class TaskPostController implements ControllerMcControllerface {
    private TaskDao taskDao;

    public TaskPostController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        QueryString requestParameter = new QueryString(request.getBody());

        Task task = new Task();
        task.setName(URLDecoder.decode(requestParameter.getParameter("task_name"), StandardCharsets.UTF_8));
        taskDao.insert(task);

        String body = "Okay";
        String response = "HTTP/1.1 200 OK\r\n" +
                "Connection: close\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
    }
}
