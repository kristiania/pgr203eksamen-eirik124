package no.kristiania.httpServer.controllers;

import no.kristiania.database.ProjectMember;
import no.kristiania.database.ProjectMemberToProject;
import no.kristiania.database.ProjectMemberToProjectDao;
import no.kristiania.httpServer.HttpMessage;
import no.kristiania.httpServer.QueryString;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class AssignToProjectPostController implements ControllerMcControllerface {
    ProjectMemberToProjectDao projectMemberToProjectDao;

    public AssignToProjectPostController(ProjectMemberToProjectDao projectMemberToProjectDao) {
        this.projectMemberToProjectDao = projectMemberToProjectDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
            QueryString requestParameter = new QueryString(request.getBody());


            ProjectMemberToProject projectMemberToProject = new ProjectMemberToProject();
            projectMemberToProject.setProjectName(URLDecoder.decode(requestParameter.getParameter("select_project"), StandardCharsets.UTF_8.name()));
            projectMemberToProject.setProjectMemberName(URLDecoder.decode(requestParameter.getParameter("select_project_member"), StandardCharsets.UTF_8.name()));
            projectMemberToProject.setTaskName(URLDecoder.decode(requestParameter.getParameter("select_task"), StandardCharsets.UTF_8.name()));
            projectMemberToProject.setStatus(URLDecoder.decode(requestParameter.getParameter("select_status"), StandardCharsets.UTF_8.name()));
            projectMemberToProject.setDescription(URLDecoder.decode(requestParameter.getParameter("description"), StandardCharsets.UTF_8.name()));
            projectMemberToProjectDao.insert(projectMemberToProject);

            String body = "Okay";
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Connection: close\r\n" +
                    "Content-Length: " + body.length() + "\r\n" +
                    "\r\n" +
                    body;

            clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
    }
}
