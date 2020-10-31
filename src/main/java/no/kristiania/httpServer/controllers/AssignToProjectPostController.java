package no.kristiania.httpServer.controllers;

import no.kristiania.database.MemberToProject;
import no.kristiania.database.MemberToProjectDao;
import no.kristiania.httpServer.HttpMessage;
import no.kristiania.httpServer.QueryString;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class AssignToProjectPostController implements HttpController {
    MemberToProjectDao memberToProjectDao;

    public AssignToProjectPostController(MemberToProjectDao memberToProjectDao) {
        this.memberToProjectDao = memberToProjectDao;
    }

   /* @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
            QueryString requestParameter = new QueryString(request.getBody());


            MemberToProject memberToProject = new MemberToProject();
            memberToProject.setProjectName(URLDecoder.decode(requestParameter.getParameter("select_project"), StandardCharsets.UTF_8.name()));
            memberToProject.setProjectMemberName(URLDecoder.decode(requestParameter.getParameter("select_project_member"), StandardCharsets.UTF_8.name()));
            memberToProject.setTaskName(URLDecoder.decode(requestParameter.getParameter("select_task"), StandardCharsets.UTF_8.name()));
            memberToProject.setStatus(URLDecoder.decode(requestParameter.getParameter("select_status"), StandardCharsets.UTF_8.name()));
            memberToProject.setDescription(URLDecoder.decode(requestParameter.getParameter("description"), StandardCharsets.UTF_8.name()));
            memberToProjectDao.insert(memberToProject);

            String body = "Okay";
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Connection: close\r\n" +
                    "Content-Length: " + body.length() + "\r\n" +
                    "\r\n" +
                    body;

            clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
    } */

    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException, SQLException {

    }
}
