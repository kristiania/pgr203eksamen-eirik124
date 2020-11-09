package no.kristiania.database;

import no.kristiania.database.objects.MemberToProject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class MemberToProjectDao extends AbstractDao<MemberToProject> {

    public MemberToProjectDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void insertData(MemberToProject memberToProject, PreparedStatement sqlStatement) throws SQLException {
        sqlStatement.setInt(1, memberToProject.getProjectId());
        sqlStatement.setInt(2, memberToProject.getMemberNameId());
        sqlStatement.setInt(3, memberToProject.getTaskId());
        sqlStatement.setInt(4, memberToProject.getStatusId());
        sqlStatement.setString(5, memberToProject.getDescription());
    }

    @Override
    protected MemberToProject readObject(ResultSet rs) throws SQLException {
        MemberToProject memberToProject = new MemberToProject();
        memberToProject.setId(rs.getLong("id"));
        memberToProject.setProjectId(rs.getInt("project_name"));
        memberToProject.setProjectName(rs.getString("p_name"));
        memberToProject.setProjectMemberFirstName(rs.getString("first_name"));
        memberToProject.setProjectMemberLastName(rs.getString("last_name"));
        memberToProject.setNameId(rs.getInt("projectmember_name"));
        memberToProject.setTaskId(rs.getInt("task_name"));
        memberToProject.setTaskName(rs.getString("name"));
        memberToProject.setStatusId(rs.getInt("status_id"));
        memberToProject.setStatus(rs.getString("status"));
        memberToProject.setDescription(rs.getString("description"));
        return memberToProject;
    }

    public long insert(MemberToProject memberToProject) throws SQLException {
        return insert(memberToProject, "INSERT INTO projectmember_to_project (project_name, projectmember_name, task_name, status_id, description) VALUES (?, ?, ?, ?, ?)");
    }

    public void updateStatus(int status, long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE projectmember_to_project SET status_id = ? WHERE id=?")) {
                statement.setInt(1,status);
                statement.setLong(2,id);
                statement.executeUpdate();
            }
        }
    }

    public void deleteAssignment(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM projectmember_to_project WHERE id = ?")) {
                statement.setLong(1,id);
                statement.executeUpdate();
            }
        }
    }


    public List<MemberToProject> listAllElements() throws SQLException {
      return listAllElements(
              "SELECT task.id, task.name, status.status, project.p_name, task_name, project_name, projectmember_name, description, projectmembers.first_name, projectmembers.last_name, status_id FROM task INNER JOIN projectmember_to_project ON  projectmember_to_project.task_name = task.id INNER JOIN project ON projectmember_to_project.project_name = project.id INNER JOIN projectmembers ON projectmember_to_project.projectmember_name = projectmembers.id INNER JOIN status ON projectmember_to_project.status_id = status.id"
      );
    }
}
