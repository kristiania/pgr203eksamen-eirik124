package no.kristiania.httpServer.controllers;

import no.kristiania.database.Project;
import no.kristiania.database.ProjectDao;
import no.kristiania.httpServer.HttpMessage;
import no.kristiania.httpServer.controllers.ControllerMcControllerface;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ProjectGetController implements ControllerMcControllerface {
    private ProjectDao projectDao;

    public ProjectGetController(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        String body = "";
        for (Project project : projectDao.list()) {
            body += "<option id='" + project.getId() + "'>" + project.getName() + "</option>";
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
