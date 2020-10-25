package no.kristiania.httpServer.controllers;

import no.kristiania.database.ProjectMember;
import no.kristiania.database.ProjectMemberDao;
import no.kristiania.httpServer.HttpMessage;
import no.kristiania.httpServer.QueryString;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class ProjectMemberPostController implements ControllerMcControllerface {
    private ProjectMemberDao projectMemberDao;

    public ProjectMemberPostController(ProjectMemberDao projectMemberDao) {
        this.projectMemberDao = projectMemberDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        QueryString requestParameter = new QueryString(request.getBody());


        ProjectMember projectMember = new ProjectMember();
        projectMember.setFirstName(URLDecoder.decode(requestParameter.getParameter("first_name"), StandardCharsets.UTF_8.name()));
        projectMember.setLastName(URLDecoder.decode(requestParameter.getParameter("last_name"), StandardCharsets.UTF_8.name()));
        projectMember.setEmail(URLDecoder.decode(requestParameter.getParameter("email"), StandardCharsets.UTF_8.name()));
        projectMemberDao.insert(projectMember);
        System.out.println(projectMember.getFirstName());

        String body = "Okay";
        String response = "HTTP/1.1 200 OK\r\n" +
                "Connection: close\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
    }
}
