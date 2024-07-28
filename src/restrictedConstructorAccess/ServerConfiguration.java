package restrictedConstructorAccess;

import java.net.InetSocketAddress;

public class ServerConfiguration {
    private static ServerConfiguration serverConfigurationInstance;
    private final InetSocketAddress serverAddress;
    private final String greetingsMessage;

    private ServerConfiguration(int port, String greetingsMessage) {
        this.greetingsMessage = greetingsMessage;
        this.serverAddress = new InetSocketAddress("localhost", port);

        if (serverConfigurationInstance == null) {
            serverConfigurationInstance = this;
        }
    }

    public static ServerConfiguration getServerConfigurationInstance() {
        return serverConfigurationInstance;
    }

    public InetSocketAddress getServerAddress() {
        return serverAddress;
    }

    public String getGreetingsMessage() {
        return greetingsMessage;
    }
}
