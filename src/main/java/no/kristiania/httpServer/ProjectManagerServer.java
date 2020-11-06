package no.kristiania.httpServer;

import no.kristiania.database.*;
import no.kristiania.httpServer.controllers.*;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class ProjectManagerServer {
    private final HttpServer server;
    private MemberDao memberDao;
    private ProjectDao projectDao;
    private TaskDao taskDao;
    private MemberToProjectDao memberToProjectDao;
    private static final Logger logger = LoggerFactory.getLogger(ProjectManagerServer.class);

    public ProjectManagerServer(int port) throws IOException, IOException {
        Properties properties = new Properties();

        try (FileReader reader = new FileReader("pgr203.properties")) {
            properties.load(reader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));

        Flyway.configure().dataSource(dataSource).load().migrate();

        memberDao = new MemberDao(dataSource);
        projectDao = new ProjectDao(dataSource);
        taskDao = new TaskDao(dataSource);
        memberToProjectDao = new MemberToProjectDao(dataSource);


        server = new HttpServer(port);
        logger.info("Using database {}", dataSource.getUrl());
        server.addController("/api/newProject", new ProjectController(projectDao));
        server.addController("/api/projects", new ProjectController(projectDao));
        server.addController("/api/updateProject", new ProjectController(projectDao));
        server.addController("/api/members", new MemberController(memberDao));
        server.addController("/api/newProjectMember", new MemberController(memberDao));
        server.addController("/api/projectMemberList", new MemberController(memberDao));
        server.addController("/api/newTask", new TaskController(taskDao));
        server.addController("/api/tasks", new TaskController(taskDao));
        server.addController("/api/assignToProject", new AssignToProjectController(memberToProjectDao));
        server.addController("/api/assignedProjects", new AssignToProjectController(memberToProjectDao));
        server.addController("/api/updateStatus", new AssignToProjectController(memberToProjectDao));
    }

    public static void main(String[] args) throws IOException {
        new ProjectManagerServer(8080).start();
    }

    private void start() throws IOException {
        server.start();
    }

}
