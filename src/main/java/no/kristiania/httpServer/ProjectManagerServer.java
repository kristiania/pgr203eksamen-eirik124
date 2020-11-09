package no.kristiania.httpServer;

import no.kristiania.database.daos.*;
import no.kristiania.httpServer.controllers.*;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ProjectManagerServer {
    private final HttpServer server;
    private static final Logger logger = LoggerFactory.getLogger(ProjectManagerServer.class);

    public ProjectManagerServer(int port) throws IOException {
        Properties properties = new Properties();

        try (FileReader reader = new FileReader("pgr203.properties")) {
            properties.load(reader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));

        Flyway.configure().dataSource(dataSource).load().migrate();

        MemberDao memberDao = new MemberDao(dataSource);
        ProjectDao projectDao = new ProjectDao(dataSource);
        TaskDao taskDao = new TaskDao(dataSource);
        MemberToProjectDao memberToProjectDao = new MemberToProjectDao(dataSource);
        StatusDao statusDao = new StatusDao(dataSource);

        server = new HttpServer(port);
        logger.info("Using database {}", dataSource.getUrl());
        server.addController("/", new TargetController());
        server.addController("/api/newProject", new ProjectController(projectDao));
        server.addController("/api/projects", new ProjectController(projectDao));
        server.addController("/api/updateProject", new ProjectController(projectDao));
        server.addController("/api/members", new MemberController(memberDao));
        server.addController("/api/newProjectMember", new MemberController(memberDao));
        server.addController("/api/projectMemberList", new MemberController(memberDao));
        server.addController("/api/updateMember", new MemberController(memberDao));
        server.addController("/api/newTask", new TaskController(taskDao));
        server.addController("/api/tasks", new TaskController(taskDao));
        server.addController("/api/updateTask", new TaskController(taskDao));
        server.addController("/api/assignToProject", new AssignToProjectController(memberToProjectDao));
        server.addController("/api/assignedProjects", new AssignToProjectController(memberToProjectDao));
        server.addController("/api/filterByStatus", new FilterController(new MemberToProjectDao(dataSource)));
        server.addController("/api/updateStatus", new AssignToProjectController(memberToProjectDao));
        server.addController("/api/deleteAssignment", new AssignToProjectController(memberToProjectDao));
        server.addController("/api/fetchStatus", new StatusController(statusDao));
    }

    public static void main(String[] args) throws IOException {
        new ProjectManagerServer(8080).start();
    }

    private void start() {
        server.start();
    }
}
