package no.kristiania.httpServer.controllers;

import no.kristiania.database.Member;
import no.kristiania.database.MemberDao;
import no.kristiania.httpServer.HttpMessage;
import no.kristiania.httpServer.QueryString;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class MemberController implements HttpController {
    private String body;
    private MemberDao dao;
    String status = "200";

    public MemberController(MemberDao dao) {
        this.dao = dao;
    }

    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException, SQLException {
        String requestLine = request.getStartLine();
        String requestTarget = requestLine.split(" ")[1];

        try {
            if (requestMethod.equals("POST")) {
                QueryString requestParameter = new QueryString(request.getBody());


                Member member = new Member();
                member.setFirstName(URLDecoder.decode(requestParameter.getParameter("first_name"), StandardCharsets.UTF_8.name()));
                member.setLastName(URLDecoder.decode(requestParameter.getParameter("last_name"), StandardCharsets.UTF_8.name()));
                member.setEmail(URLDecoder.decode(requestParameter.getParameter("email"), StandardCharsets.UTF_8.name()));
                dao.insert(member);
                System.out.println(member.getFirstName());

                outputStream.write(("HTTP/1.1 302 Redirect\r\n" +
                        "Location: /newProjectMember.html\r\n" +
                        "Transfer-Encoding: chunked" +
                        "Connection: close\r\n" +
                        "\r\n").getBytes("UTF-8"));
            } else {
                if (requestTarget.equals("/api/projectMemberList")) {

                    body = getBodyList();
                    outputStream.write(("HTTP/1.1 " + status + " OK\r\n" +
                            "Content-Type: text/html\r\n" +
                            "Content-Length: " + body.length() + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n" +
                            body).getBytes("UTF-8"));
                    outputStream.flush();
                } else {
                    body = "<ul>";
                    body += getBody();
                    body += "</ul>";


                    outputStream.write(("HTTP/1.1 " + status + " OK\r\n" +
                            "Content-Type: text/html\r\n" +
                            "Content-Length: " + body.length() + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n" +
                            body).getBytes("UTF-8"));
                    outputStream.flush();
                }
            }

        } catch (SQLException e) {
            String message = e.toString();
            outputStream.write(("HTTP/1.1 500 Internal server error\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + message.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    message).getBytes("UTF-8"));
        }

    }

    public String getBody() throws SQLException {
        return dao.list().stream()
                .map(dao -> String.format("<li>" + dao.getFirstName() + " " + dao.getLastName() + ", " + dao.getEmail() + "</li>"))
                .collect(Collectors.joining(""));
    }

    public String getBodyList() throws SQLException {
        return dao.list().stream()
                .map(dao -> String.format("<option value='"+ dao.getFirstName() +" " + dao.getLastName() +"'>" + dao.getFirstName() + " " + dao.getLastName() + "</option>"))
                .collect(Collectors.joining(""));
    }
}
