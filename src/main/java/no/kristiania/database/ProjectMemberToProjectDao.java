package no.kristiania.database;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectMemberToProjectDao {

    private DataSource dataSource;

    public ProjectMemberToProjectDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(ProjectMemberToProject projectMemberToProject) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO projectmember_to_project (project_name, projectmember_name, task_name, status, description) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, projectMemberToProject.getProjectName());
                statement.setString(2, projectMemberToProject.getProjectMemberName());
                statement.setString(3, projectMemberToProject.getTaskName());
                statement.setString(4, projectMemberToProject.getStatus());
                statement.setString(5, projectMemberToProject.getDescription());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    projectMemberToProject.setId(generatedKeys.getLong("id"));
                }
            }
        }
    }

    public ProjectMemberToProject retrieve(Long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM projectmember_to_project WHERE id = ?")) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return mapRowToProjectMemberToProject(rs);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    private ProjectMemberToProject mapRowToProjectMemberToProject(ResultSet rs) throws SQLException {
        ProjectMemberToProject projectMemberToProject = new ProjectMemberToProject();
        projectMemberToProject.setId(rs.getLong("id"));
        projectMemberToProject.setProjectName(rs.getString("project_name"));
        projectMemberToProject.setProjectMemberName(rs.getString("projectmember_name"));
        projectMemberToProject.setTaskName(rs.getString("task_name"));
        projectMemberToProject.setStatus(rs.getString("status"));
        projectMemberToProject.setDescription(rs.getString("description"));
        return projectMemberToProject;
    }

    public List<ProjectMemberToProject> list() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from projectmember_to_project")) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<ProjectMemberToProject> projectMemberToProjects = new ArrayList<>();
                    while (rs.next()) {
                        projectMemberToProjects.add(mapRowToProjectMemberToProject(rs));
                    }
                    return projectMemberToProjects;
                }
            }
        }
    }
}
