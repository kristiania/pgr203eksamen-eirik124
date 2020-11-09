package no.kristiania.httpServer.controllers;

import no.kristiania.database.objects.Member;
import no.kristiania.database.MemberDao;
import no.kristiania.httpServer.HttpMessage;
import no.kristiania.httpServer.QueryString;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class MemberController implements HttpController {
    String body;
    private final MemberDao dao;
    String status = "200";
    String redirect;

    public MemberController(MemberDao dao) {
        this.dao = dao;
    }

    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException {
        String requestTarget = RequestTarget.requestTarget(request);

        try {
            if (requestMethod.equals("POST")) {
                QueryString requestParameter = new QueryString(request.getBody());

                if (requestTarget.equals("/api/updateMember")) {
                    updateMember(requestParameter);
                    redirect = "/updateMember.html";
                } else {
                    executeSqlStatement(requestParameter);
                    redirect = "/newProjectMember.html";
                }

                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: " + redirect + "\r\n" +
                        "Transfer-Encoding: chunked" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes(StandardCharsets.UTF_8));
            } else {
                if (requestTarget.equals("/api/projectMemberList")) {
                    body = getBodyList();
                } else {
                    body = "<ul>";
                    body += getBody();
                    body += "</ul>";
                }

                outputStream.write(("HTTP/1.1 " + status + " OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + body.length() + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        body).getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }

        } catch (SQLException e) {
            String message = e.toString();
            outputStream.write(("HTTP/1.1 500 Internal server error\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + message.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    message).getBytes(StandardCharsets.UTF_8));
        }
    }

    private void executeSqlStatement(QueryString requestParameter) throws UnsupportedEncodingException, SQLException {
        Member member = new Member();
        member.setFirstName(URLDecoder.decode(requestParameter.getParameter("first_name"), StandardCharsets.UTF_8.name()));
        member.setLastName(URLDecoder.decode(requestParameter.getParameter("last_name"), StandardCharsets.UTF_8.name()));
        member.setEmail(URLDecoder.decode(requestParameter.getParameter("email"), StandardCharsets.UTF_8.name()));
        dao.insert(member);
    }

    private void updateMember(QueryString requestParameter) throws UnsupportedEncodingException, SQLException {
        String firstName = URLDecoder.decode(requestParameter.getParameter("update-member-f-name"), StandardCharsets.UTF_8.name());
        String lastName = URLDecoder.decode(requestParameter.getParameter("update-member-l-name"), StandardCharsets.UTF_8.name());
        String email = URLDecoder.decode(requestParameter.getParameter("update-member-email"), StandardCharsets.UTF_8.name());
        String memberId = requestParameter.getParameter("member");
        long id = Long.parseLong(memberId);
        dao.updateMember(firstName, lastName, email, id);
    }

    public String getBody() throws SQLException {
        return dao.listAllElements().stream()
                .map(dao -> "<li>" + dao.getFirstName() + " " + dao.getLastName() + ", " + dao.getEmail() + "</li>")
                .collect(Collectors.joining(""));
    }

    public String getBodyList() throws SQLException {
        return dao.listAllElements().stream()
                .map(dao -> "<option value='" + dao.getId() + "'>" + dao.getFirstName() + " " + dao.getLastName() + "</option>")
                .collect(Collectors.joining(""));
    }
}
