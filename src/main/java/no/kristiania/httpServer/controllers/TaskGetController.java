package no.kristiania.httpServer.controllers;

import no.kristiania.database.Project;
import no.kristiania.database.Task;
import no.kristiania.database.TaskDao;
import no.kristiania.httpServer.HttpMessage;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class TaskGetController implements ControllerMcControllerface {
    private TaskDao taskDao;

    public TaskGetController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        String body = "";
        for (Task task : taskDao.list()) {
            body += "<option id='" + task.getId() + "'>" + task.getName() + "</option> ";
        }

        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Content-Type: text/html; application/x-www-form-urlencoded\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
    }
}
