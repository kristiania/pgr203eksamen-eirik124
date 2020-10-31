package no.kristiania.httpServer.controllers;

import no.kristiania.database.Member;
import no.kristiania.database.MemberDao;
import no.kristiania.httpServer.HttpMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class ProjectMemberListGetController implements HttpController {
    private MemberDao memberDao;

    public ProjectMemberListGetController(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

   /* @Override
    public void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException {
        String body = "";
        for (Member member : memberDao.list()) {
            body += "<option value='"+ member.getFirstName() +" " + member.getLastName() +"'>" + member.getFirstName() + " " + member.getLastName() + "</option>";
        }
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Content-Type: text/html; application/x-www-form-urlencoded\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes("UTF-8"));
    } */

    @Override
    public void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException, SQLException {

    }
}
