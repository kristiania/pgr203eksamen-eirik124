package no.kristiania.httpServer;

import no.kristiania.database.Project;
import no.kristiania.database.ProjectDao;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ProjectPostController implements ControllerMcControllerface {
    private ProjectDao projectDao;

    public ProjectPostController(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        QueryString requestParameter = new QueryString(request.getBody());

        Project project = new Project();
        project.setName(requestParameter.getParameter("project_name"));
        projectDao.insert(project);

        String body = "Okay";
        String response = "HTTP/1.1 200 OK\r\n" +
                "Connection: close\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
    }
}
