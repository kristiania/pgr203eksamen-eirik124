package no.kristiania.httpServer;

import no.kristiania.database.*;
import no.kristiania.database.objects.Member;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Date;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpServerTest {

    private JdbcDataSource dataSource = TestDatabaseSource.dataSource();
    private HttpServer server;
    private MemberDao memberDao;
    private ProjectDao projectDao;
    private TaskDao taskDao;


    @Test
    void shouldReturnSuccessfulStatusCode() throws IOException {
        HttpServer server = new HttpServer(0);
        server.start();
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo");
        assertEquals(200, client.getStatusCode());
    }

    @Test
    void shouldReturnUnsuccessfulStatusCode() throws IOException {
        HttpServer server = new HttpServer(0);
        server.start();
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?status=404");
        assertEquals(404, client.getStatusCode());
    }

  /*  @Test
    void shouldReturnContentLength() throws IOException {
        HttpServer server = new HttpServer(0);
        server.start();
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?body=HelloWorld");
        assertEquals("10", client.getResponseHeader("Content-Length"));
    }

    @Test
    void shouldReturnResponseBody() throws IOException {
        HttpServer server = new HttpServer(0);
        server.start();
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?body=HelloWorld");
        assertEquals("HelloWorld", client.getResponseBody());
    }*/

    @Test
    void shouldReturnFileFromDisk() throws IOException {
        HttpServer server = new HttpServer(0);
        server.start();
        File contentRoot = new File("target/test-classes");

        String fileContent = "Hello World " + new Date();
        Files.writeString(new File(contentRoot, "test.txt").toPath(), fileContent);

        HttpClient client = new HttpClient("localhost", server.getPort(), "/test.txt");
        assertEquals(fileContent, client.getResponseBody());
        assertEquals("text/plain", client.getResponseHeader("Content-Type"));
    }

    @Test
    void shouldReturnCorrectContentType() throws IOException {
        HttpServer server = new HttpServer(0);
        server.start();
        File contentRoot = new File("target/test-classes");

        Files.writeString(new File(contentRoot, "index.html").toPath(), "<h2>Hello World</h2>");

        HttpClient client = new HttpClient("localhost", server.getPort(), "/index.html");
        assertEquals("text/html", client.getResponseHeader("Content-Type"));
    }

    @Test
    void shouldReturn404IfFileNotFound() throws IOException {
        HttpServer server = new HttpServer(0);
        server.start();
        HttpClient client = new HttpClient("localhost", server.getPort(), "/notFound.txt");
        assertEquals(404, client.getStatusCode());
    }

   // @Test
    void shouldPostNewMember() throws IOException, SQLException {
        HttpServer server = new HttpServer(0);
        server.start();
        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/newProjectMember", "POST", "first_name=Eirik&last_name=Test&email=test@email.com");

        assertEquals(302, client.getStatusCode());
        /*assertThat(server.getProjectMembers())
                .extracting(Member::getFirstName)
                .contains("Eirik");*/
    }

   // @Test
    void shouldReturnExistingProjectMembers() throws IOException, SQLException {
        HttpServer server = new HttpServer(0);
        server.start();
        MemberDao memberDao = new MemberDao(dataSource);
        Member member = new Member();
        member.setFirstName("Paal Anders");
        member.setLastName("Byenstuen");
        member.setEmail("pl4nders@gmail.com");
        memberDao.insert(member);

        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/members");
        assertThat(client.getResponseBody()).contains("<li>Paal Anders Byenstuen, pl4nders@gmail.com</li>");
    }

    //@Test
    void shouldPostNewProject() throws IOException, SQLException {
        HttpServer server = new HttpServer(0);
        server.start();
        String requestBody = "project_name=IT-Prosjekter";
        HttpClient postClient = new HttpClient("localhost", server.getPort(), "/api/newProject", "POST", requestBody);
        assertEquals(302, postClient.getStatusCode());

        HttpClient getClient = new HttpClient("localhost", server.getPort(), "/api/projects");
        assertThat(getClient.getResponseBody()).contains("IT-Prosjekter");
    }

}