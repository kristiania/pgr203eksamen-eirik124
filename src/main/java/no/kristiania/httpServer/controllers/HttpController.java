package no.kristiania.httpServer.controllers;

import no.kristiania.httpServer.HttpMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

public interface HttpController {
    void handle(String requestMethod, HttpMessage request, Socket clientSocket, OutputStream outputStream) throws IOException, SQLException;
}
