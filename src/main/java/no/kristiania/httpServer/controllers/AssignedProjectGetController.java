package no.kristiania.httpServer.controllers;

import no.kristiania.database.MemberToProject;
import no.kristiania.database.MemberToProjectDao;
import no.kristiania.httpServer.HttpMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class AssignedProjectGetController implements HttpController {
    private MemberToProjectDao memberToProjectDao;

    public AssignedProjectGetController(MemberToProjectDao memberToProjectDao) {
        this.memberToProjectDao = memberToProjectDao;
    }

   /* @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        String body = "";
        for (MemberToProject memberToProject : memberToProjectDao.list()) {

            body += "<div class='project-card " + memberToProject.getStatus() + "' id='"+ memberToProject.getId() +"'>" +
                    "<h3>"+ memberToProject.getProjectName() +"</h3>" +
                    "<h5>Task: " + memberToProject.getTaskName() + "</h5>" +
                    "<b>Description:</b><br>" +
                    "<p> " + memberToProject.getDescription() + "</p><br><br>" +
                    "Assigned to: " + memberToProject.getProjectMemberName() + "<br> " +
                    "Status: " + memberToProject.getStatus() + "<br><br><br>" +
                    "<select value='"+ memberToProject.getStatus() +"' onchange='/api/updateStatus'> " +
                    "<option value='to do'>To do </option>" +
                    "<option value='in progress'>In progress </option>" +
                    "<option value='done'>Done</option>" +
                    "</select>" +
                    "</div>";
        }

        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Content-Type: text/html; application/x-www-form-urlencoded\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
    }*/

    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException, SQLException {

    }
}
