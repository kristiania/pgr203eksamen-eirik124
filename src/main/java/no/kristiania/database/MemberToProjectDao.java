package no.kristiania.database;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberToProjectDao {

    private DataSource dataSource;

    public MemberToProjectDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(MemberToProject memberToProject) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO projectmember_to_project (project_name, projectmember_name, task_name, status, description) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, memberToProject.getProjectName());
                statement.setString(2, memberToProject.getProjectMemberName());
                statement.setString(3, memberToProject.getTaskName());
                statement.setString(4, memberToProject.getStatus());
                statement.setString(5, memberToProject.getDescription());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    memberToProject.setId(generatedKeys.getLong("id"));
                }
            }
        }
    }

    public MemberToProject retrieve(Long id) throws SQLException {
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

    public void updateStatus(String status, long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE projectmember_to_project SET status = ? WHERE id=?")) {
                statement.setString(1,status);
                statement.setLong(2,id);
                statement.executeUpdate();
            }
        }
    }

    private MemberToProject mapRowToProjectMemberToProject(ResultSet rs) throws SQLException {
        MemberToProject memberToProject = new MemberToProject();
        memberToProject.setId(rs.getLong("id"));
        memberToProject.setProjectName(rs.getString("project_name"));
        memberToProject.setProjectMemberName(rs.getString("projectmember_name"));
        memberToProject.setTaskName(rs.getString("task_name"));
        memberToProject.setStatus(rs.getString("status"));
        memberToProject.setDescription(rs.getString("description"));
        return memberToProject;
    }

    public List<MemberToProject> list() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from projectmember_to_project")) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<MemberToProject> memberToProjects = new ArrayList<>();
                    while (rs.next()) {
                        memberToProjects.add(mapRowToProjectMemberToProject(rs));
                    }
                    return memberToProjects;
                }
            }
        }
    }
}
