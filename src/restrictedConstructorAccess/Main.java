package restrictedConstructorAccess;

import restrictedConstructorAccess.web.WebServer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        initializeConfiguration();

        WebServer webServer = new WebServer();
        webServer.startServer();
    }

    public static void initializeConfiguration() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<ServerConfiguration> declaredConstructor =
                ServerConfiguration.class.getDeclaredConstructor(int.class, String.class);

        declaredConstructor.setAccessible(true);
        declaredConstructor.newInstance(8080, "Hello there!");
    }
}
