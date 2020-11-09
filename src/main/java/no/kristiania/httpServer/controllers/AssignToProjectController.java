package no.kristiania.httpServer.controllers;

import no.kristiania.database.objects.MemberToProject;
import no.kristiania.database.MemberToProjectDao;
import no.kristiania.httpServer.HttpMessage;
import no.kristiania.httpServer.QueryString;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class AssignToProjectController implements HttpController {

    private final MemberToProjectDao dao;
    String redirect;
    public AssignToProjectController(MemberToProjectDao dao) {
        this.dao = dao;
    }

    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException {

        try {
            if (requestMethod.equals("POST")) {
                String requestTarget = RequestTarget.requestTarget(request);
                QueryString requestParameter = new QueryString(request.getBody());

                if (requestTarget.equals("/api/updateStatus")) {
                    updateStatus(requestParameter);
                    redirect = "/assignedProjects.html";
                } else if (requestTarget.equals("/api/deleteAssignment")) {
                    deleteAssignment(requestParameter);
                    redirect = "/assignedProjects.html";
                } else {
                    executeSqlStatement(requestParameter);
                    redirect = "/assignToProject.html";
                }

                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: " + redirect +"\r\n" +
                        "Transfer-Encoding: chunked" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes(StandardCharsets.UTF_8));
            } else {
                String body = getBody();
                String status = "200";

                outputStream.write(("HTTP/1.1 " + status + " OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + body.length() + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        body).getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
        } catch (SQLException e) {
            String message = e.toString();
            outputStream.write(("HTTP/1.1 500 Internal server error\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + message.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    message).getBytes(StandardCharsets.UTF_8));
        }
    }

    private void executeSqlStatement(QueryString requestParameter) throws SQLException, UnsupportedEncodingException {
        MemberToProject memberToProject = new MemberToProject();
        memberToProject.setProjectId(Integer.parseInt(URLDecoder.decode(requestParameter.getParameter("select_project"), StandardCharsets.UTF_8.name())));
        memberToProject.setNameId(Integer.parseInt(URLDecoder.decode(requestParameter.getParameter("select_project_member"), StandardCharsets.UTF_8.name())));
        memberToProject.setTaskId(Integer.parseInt(URLDecoder.decode(requestParameter.getParameter("select_task"), StandardCharsets.UTF_8.name())));
        memberToProject.setStatusId(Integer.parseInt(URLDecoder.decode(requestParameter.getParameter("select_status"), StandardCharsets.UTF_8.name())));
        memberToProject.setDescription(URLDecoder.decode(requestParameter.getParameter("description"), StandardCharsets.UTF_8.name()));
        dao.insert(memberToProject);
    }

    private void updateStatus(QueryString parameters) throws SQLException {
        String status = parameters.getParameter("status_update");
        int statusId = Integer.parseInt(status);
        String idString = parameters.getParameter("id");
        long id = Long.parseLong(idString);
        dao.updateStatus(statusId, id);
    }

    private void deleteAssignment(QueryString parameters) throws SQLException {
        String idString = parameters.getParameter("delete-id");
        long id = Long.parseLong(idString);
        dao.deleteAssignment(id);
    }

    public String getBody() throws SQLException {
        return dao.listAllElements().stream()
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
