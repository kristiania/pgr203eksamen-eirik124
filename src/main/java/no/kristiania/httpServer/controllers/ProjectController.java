package no.kristiania.httpServer.controllers;

import no.kristiania.database.Project;
import no.kristiania.database.ProjectDao;
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

public class ProjectController implements HttpController {
    private ProjectDao dao;
    private String body;
    private String redirect;

    public ProjectController(ProjectDao dao) {
        this.dao = dao;
    }

    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException, SQLException {
        try {
            if (requestMethod.equals("POST")) {
                String requestLine = request.getStartLine();
                String requestTarget = requestLine.split(" ")[1];
                QueryString requestParameter = new QueryString(request.getBody());

                if (requestTarget.equals("/api/updateProject")) {
                    updateName(requestParameter);
                    redirect = "/updateProject.html";
                } else {
                    executeSqlStatement(requestParameter);
                    redirect = "/newProject.html";
                }

                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: "+ redirect +" \r\n" +
                        "Transfer-Encoding: chunked" +
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

    private void executeSqlStatement(QueryString requestParameter) throws SQLException {
        Project project = new Project();
        project.setName(URLDecoder.decode(requestParameter.getParameter("project_name"), StandardCharsets.UTF_8));
        dao.insert(project);
    }

    private void updateName(QueryString parameters) throws SQLException, UnsupportedEncodingException {
        String name = URLDecoder.decode(parameters.getParameter("update-project-name"), StandardCharsets.UTF_8.name());
        String idString = parameters.getParameter("project-name");
        long id = Long.parseLong(idString);
        dao.updateName(name, id);
    }


    public String getBody() throws SQLException {
        return dao.list().stream()
                .map(dao -> String.format("<option name='" + dao.getId() +"' value='" + dao.getId() +"' id='" + dao.getId() + "'>" + dao.getName() + "</option>"))
                .collect(Collectors.joining(""));
    }
}
