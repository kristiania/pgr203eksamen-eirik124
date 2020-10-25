package no.kristiania.httpServer.controllers;

import no.kristiania.httpServer.HttpMessage;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public interface ControllerMcControllerface {
    void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException;
}
