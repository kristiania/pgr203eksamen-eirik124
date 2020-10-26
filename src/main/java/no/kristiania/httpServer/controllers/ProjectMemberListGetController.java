package no.kristiania.httpServer.controllers;

import no.kristiania.database.ProjectMember;
import no.kristiania.database.ProjectMemberDao;
import no.kristiania.httpServer.HttpMessage;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ProjectMemberListGetController implements ControllerMcControllerface {
    private ProjectMemberDao projectMemberDao;

    public ProjectMemberListGetController(ProjectMemberDao projectMemberDao) {
        this.projectMemberDao = projectMemberDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        String body = "";
        for (ProjectMember projectMember : projectMemberDao.list()) {
            body += "<option value='"+ projectMember.getFirstName() +" " + projectMember.getLastName() +"'>" + projectMember.getFirstName() + " " + projectMember.getLastName() + "</option>";
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
