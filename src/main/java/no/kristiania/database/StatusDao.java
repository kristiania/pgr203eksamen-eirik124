package no.kristiania.database;

import no.kristiania.database.objects.Status;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class StatusDao extends AbstractDao<Status> {

    public StatusDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void insertData(Status status, PreparedStatement sqlStatement) throws SQLException {
        sqlStatement.setString(1, status.getStatus());
    }

    @Override
    protected Status readObject(ResultSet rs) throws SQLException {
        Status status = new Status();
        status.setId(rs.getLong("id"));
        status.setName(rs.getString("status"));
        return status;
    }

    public long insert(Status status) throws SQLException {
        return insert(status, "INSERT INTO status (status) VALUES (?)");
    }


    public List<Status> listAllElements() throws SQLException {
        return listAllElements(
                "select * from status"
        );
    }

}
