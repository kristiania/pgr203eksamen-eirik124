package no.kristiania.httpServer;

import no.kristiania.database.objects.Project;
import no.kristiania.database.ProjectDao;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectTest {
    private JdbcDataSource dataSource = TestDatabaseSource.dataSource();


    @Test
    void shouldLocateSavedProjects() throws SQLException {
        ProjectDao projectDao = new ProjectDao(dataSource);
        Project project = projectSample();
        projectDao.insert(project);
        assertThat(projectDao.listAllElements().contains(project));
    }


    private Project projectSample() {
        Project project = new Project();
        String projectName = "test-projsket";
        project.setName(projectName);
        return project;
    }
}
