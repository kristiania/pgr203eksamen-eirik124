package no.kristiania.database;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProjectMemberDao {

    private DataSource dataSource;

    public ProjectMemberDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(ProjectMember projectMember) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO projectmembers (first_name, last_name, email) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, projectMember.getFirstName());
                statement.setString(2, projectMember.getLastName());
                statement.setString(3, projectMember.getEmail());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    projectMember.setId(generatedKeys.getLong("id"));
                }
            }
        }
    }

    public ProjectMember retrieve(Long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM projectmembers WHERE id = ?")) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return mapRowToProjectMember(rs);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    private ProjectMember mapRowToProjectMember(ResultSet rs) throws SQLException {
        ProjectMember projectMember = new ProjectMember();
        projectMember.setId(rs.getLong("id"));
        projectMember.setFirstName(rs.getString("first_name"));
        projectMember.setLastName(rs.getString("last_name"));
        projectMember.setEmail(rs.getString("email"));
        return projectMember;
    }

    public List<ProjectMember> list() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from projectmembers")) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<ProjectMember> projectMembers = new ArrayList<>();
                    while (rs.next()) {
                        projectMembers.add(mapRowToProjectMember(rs));
                    }
                    return projectMembers;
                }
            }
        }
    }
}
