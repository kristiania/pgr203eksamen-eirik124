package no.kristiania.httpServer;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public interface ControllerMcControllerface {
    void handle(HttpMessage request, Socket clientSocket) throws IOException, SQLException;
}
