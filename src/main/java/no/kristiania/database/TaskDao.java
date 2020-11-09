package no.kristiania.database;

import no.kristiania.database.objects.Task;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class TaskDao extends AbstractDao<Task> {

    public TaskDao(DataSource dataSource) {
       super(dataSource);
    }

    @Override
    public void insertData(Task task, PreparedStatement sqlStatement) throws SQLException {
        sqlStatement.setString(1, task.getName());
    }

    @Override
    protected Task readObject(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setName(rs.getString("name"));
        return task;
    }

    public long insert (Task task) throws SQLException {
        return insert(task, "INSERT INTO task (name) VALUES (?)");
    }

    public List<Task> listAllElements() throws SQLException {
        return listAllElements(
                "select * from task"
        );
    }

    public void updateTask(String name, long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE task SET name = ? WHERE id=?")) {
                statement.setString(1,name);
                statement.setLong(2,id);
                statement.executeUpdate();
            }
        }
    }
}
