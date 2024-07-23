package restrictedConstructorAccess.web;

import com.sun.net.httpserver.HttpServer;
import restrictedConstructorAccess.ServerConfiguration;

import java.io.IOException;
import java.io.OutputStream;

public class WebServer {

    public void startServer() throws IOException {
        HttpServer httpServer = HttpServer.create(ServerConfiguration.getServerConfigurationInstance().getServerAddress(), 0);

        httpServer.createContext("/greeting").setHandler(
                exchange -> {
                    String responseMessage = ServerConfiguration.getServerConfigurationInstance().getGreetingsMessage();
                    exchange.sendResponseHeaders(200, responseMessage.length());

                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(responseMessage.getBytes());
                    responseBody.flush();
                    responseBody.close();
                }
        );

        System.out.printf("Starting server on address: %s:%s\n",
                ServerConfiguration.getServerConfigurationInstance().getServerAddress().getHostName(),
                ServerConfiguration.getServerConfigurationInstance().getServerAddress().getPort());

        httpServer.start();

    }
}
