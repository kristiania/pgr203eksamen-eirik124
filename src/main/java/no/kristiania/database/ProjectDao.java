package no.kristiania.database;

import no.kristiania.database.objects.Project;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class ProjectDao extends AbstractDao<Project> {


    public ProjectDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void insertData(Project project, PreparedStatement sqlStatement) throws SQLException {
        sqlStatement.setString(1, project.getName());
    }

    @Override
    protected Project readObject(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setId(rs.getLong("id"));
        project.setName(rs.getString("p_name"));
        return project;
    }

    public long insert(Project project) throws SQLException {
        return insert(project, "INSERT INTO project (p_name) VALUES (?)");
    }

    public List<Project> listAllElements() throws SQLException {
        return listAllElements(
                "select * from project"
        );
    }

    public void updateName(String name, long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE project SET p_name = ? WHERE id=?")) {
                statement.setString(1,name);
                statement.setLong(2,id);
                statement.executeUpdate();
            }
        }
    }
}
