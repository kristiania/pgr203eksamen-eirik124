package no.kristiania.httpServer.controllers;

import no.kristiania.database.ProjectMemberToProject;
import no.kristiania.database.ProjectMemberToProjectDao;
import no.kristiania.httpServer.HttpMessage;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class AssignedProjectGetController implements ControllerMcControllerface {
    private ProjectMemberToProjectDao projectMemberToProjectDao;

    public AssignedProjectGetController(ProjectMemberToProjectDao projectMemberToProjectDao) {
        this.projectMemberToProjectDao = projectMemberToProjectDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        String body = "";
        for (ProjectMemberToProject projectMemberToProject : projectMemberToProjectDao.list()) {


            body += "<div class='project-card " + projectMemberToProject.getStatus() + "' id='"+ projectMemberToProject.getId() +"'>" +
                    "<h3>"+ projectMemberToProject.getProjectName() +"</h3>" +
                    "<h5>Task: " + projectMemberToProject.getTaskName() + "</h5>" +
                    "<b>Description:</b><br>" +
                    "<p> " + projectMemberToProject.getDescription() + "</p><br><br>" +
                    "Assigned to: " + projectMemberToProject.getProjectMemberName() + "<br> " +
                    "Status: " + projectMemberToProject.getStatus() + "<br><br><br>" +
                    "</div>";
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
