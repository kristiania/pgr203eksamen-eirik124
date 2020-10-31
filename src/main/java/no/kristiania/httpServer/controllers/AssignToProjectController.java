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
import java.util.stream.Collectors;

public class AssignToProjectController implements HttpController {
    private MemberToProjectDao dao;
    private String body;

    public AssignToProjectController(MemberToProjectDao dao) {
        this.dao = dao;
    }

    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException, SQLException {
        try {
            if (requestMethod.equals("POST")) {
                QueryString requestParameter = new QueryString(request.getBody());


                MemberToProject memberToProject = new MemberToProject();
                memberToProject.setProjectName(URLDecoder.decode(requestParameter.getParameter("select_project"), StandardCharsets.UTF_8.name()));
                memberToProject.setProjectMemberName(URLDecoder.decode(requestParameter.getParameter("select_project_member"), StandardCharsets.UTF_8.name()));
                memberToProject.setTaskName(URLDecoder.decode(requestParameter.getParameter("select_task"), StandardCharsets.UTF_8.name()));
                memberToProject.setStatus(URLDecoder.decode(requestParameter.getParameter("select_status"), StandardCharsets.UTF_8.name()));
                memberToProject.setDescription(URLDecoder.decode(requestParameter.getParameter("description"), StandardCharsets.UTF_8.name()));
                dao.insert(memberToProject);

                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: /assignToProject.html\r\n" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes("UTF-8"));

            } else {


                body = getBody();
                String status = "200";

                outputStream.write(("HTTP/1.1 " + status + " OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + body.length() + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        body).getBytes("UTF-8"));
                outputStream.flush();
            }
        } catch (SQLException e) {
            String message = e.toString();
            outputStream.write(("HTTP/1.1 500 Internal server error\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + message.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    message).getBytes("UTF-8"));
        }
    }

    public String getBody() throws SQLException {
        return dao.list().stream()
                .map(dao -> String.format("<div class='project-card " + dao.getStatus() + "' id='"+ dao.getId() +"'>" +
                        "<h3>"+ dao.getProjectName() +"</h3>" +
                        "<h5>Task: " + dao.getTaskName() + "</h5>" +
                        "<b>Description:</b><br>" +
                        "<p> " + dao.getDescription() + "</p><br><br>" +
                        "Assigned to: " + dao.getProjectMemberName() + "<br> " +
                        "Status: " + dao.getStatus() + "<br><br><br>" +
                        "<select value='"+ dao.getStatus() +"' onchange='/api/updateStatus'> " +
                        "<option value='to do'>To do </option>" +
                        "<option value='in progress'>In progress </option>" +
                        "<option value='done'>Done</option>" +
                        "</select>" +
                        "</div>"))
                .collect(Collectors.joining(""));
    }
}
