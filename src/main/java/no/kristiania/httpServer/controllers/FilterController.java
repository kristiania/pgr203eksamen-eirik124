package no.kristiania.httpServer.controllers;

import no.kristiania.database.daos.MemberToProjectDao;
import no.kristiania.httpServer.HttpMessage;
import no.kristiania.httpServer.QueryString;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class FilterController implements HttpController {
    private MemberToProjectDao dao;
    private int taskStatus;

    public FilterController(MemberToProjectDao dao) {
        this.dao = dao;
    }

    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException, SQLException {
        String requestTarget = RequestTarget.requestTarget(request);
        if (requestMethod.equals("POST")) {
            QueryString requestParameter = new QueryString(request.getBody());
            taskStatus = Integer.parseInt(java.net.URLDecoder.decode(requestParameter.getParameter("statusFilter"), StandardCharsets.UTF_8.name()));
            dao.filter(taskStatus);

            outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                    "Location: /filterAssignedProjects.html\r\n" +
                    "Connection: close\r\n" +
                    "\r\n").getBytes(StandardCharsets.UTF_8));
        } else {
            String status = "200";
            String body = getBody();
            outputStream.write(("HTTP/1.1 " + status + " OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + body.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    body).getBytes());
            outputStream.flush();
        }

    }

    public String getBody() throws SQLException {
        return dao.filter(this.taskStatus).stream()
                .map(dao -> "<div class='project-card " + dao.getStatus() + "' id='" + dao.getId() + "'>" +
                        "<h3>" + dao.getProjectName() + "</h3>" +
                        "<h5>Task: " + dao.getTaskName() + "</h5>" +
                        "<b>Description:</b><br>" +
                        "<p> " + dao.getDescription() + "</p><br><br>" +
                        "Assigned to: " + dao.getFirstName() + " " + dao.getLastName() + "<br> " +
                        "Status: " + dao.getStatus() + "<br><br><br>" +
                        "<form method='POST' action='/api/updateStatus'>" +
                        "<input type='hidden' name='id' value='" + dao.getId() + "'>" +
                        "<select class='status_update' id='status_update' name='status_update'> " +
                        "</select>" +
                        "<button> Update Status</button>" +
                        "</form>" +
                        "<br><br>" +
                        "<form method='POST' action='/api/deleteAssignment'>" +
                        "<input type='hidden' name='delete-id' value='" + dao.getId() + "'>" +
                        "<button> Remove assignment</button>" +
                        "</form>" +
                        "</div>")
                .collect(Collectors.joining(""));
    }


}
