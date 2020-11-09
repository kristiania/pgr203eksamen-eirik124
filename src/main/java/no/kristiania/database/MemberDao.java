package no.kristiania.database;

import no.kristiania.database.objects.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class MemberDao extends AbstractDao<Member> {



    public MemberDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void insertData(Member member, PreparedStatement sqlStatement) throws SQLException {
        sqlStatement.setString(1, member.getFirstName());
        sqlStatement.setString(2, member.getLastName());
        sqlStatement.setString(3, member.getEmail());
    }

    @Override
    protected Member readObject(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setFirstName(rs.getString("first_name"));
        member.setLastName(rs.getString("last_name"));
        member.setEmail(rs.getString("email"));
        return member;
    }

    public long insert(Member member) throws SQLException {
        return insert(member, "INSERT INTO projectmembers (first_name, last_name, email) VALUES (?, ?, ?)");
    }

    public List<Member> listAllElements() throws SQLException {
        return listAllElements(
                "select * from projectmembers"
        );
    }


    public void updateMember(String firstName, String lastName, String email, long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE projectmembers SET first_name = ?, last_name = ?, email = ? WHERE id=?")) {
                statement.setString(1,firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.setLong(4,id);
                statement.executeUpdate();
            }
        }
    }

}
