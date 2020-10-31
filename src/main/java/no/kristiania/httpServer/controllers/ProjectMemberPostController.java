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

public class ProjectMemberPostController implements HttpController {
    private MemberDao memberDao;

    public ProjectMemberPostController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

   /* @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        QueryString requestParameter = new QueryString(request.getBody());


        Member member = new Member();
        member.setFirstName(URLDecoder.decode(requestParameter.getParameter("first_name"), StandardCharsets.UTF_8.name()));
        member.setLastName(URLDecoder.decode(requestParameter.getParameter("last_name"), StandardCharsets.UTF_8.name()));
        member.setEmail(URLDecoder.decode(requestParameter.getParameter("email"), StandardCharsets.UTF_8.name()));
        memberDao.insert(member);
        System.out.println(member.getFirstName());

        String body = "Okay";
        String response = "HTTP/1.1 200 OK\r\n" +
                "Connection: close\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
    } */

    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException, SQLException {

    }
}
