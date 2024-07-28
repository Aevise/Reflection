package proxy;

import proxy.external.DatabaseReader;
import proxy.external.HttpClient;
import proxy.impl.DatabaseReaderImpl;
import proxy.impl.HttpClientImpl;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        HttpClient httpClient = createProxy(new HttpClientImpl());
        DatabaseReader databaseReader = createProxy(new DatabaseReaderImpl());


        useHttpClient(httpClient);
        System.out.println(" ");
        useDatabaseReader(databaseReader);
        System.out.println(" ");

        List<String> listOfGreetings = createProxy(new ArrayList<>());
        listOfGreetings.add("gello");
        System.out.println(" ");
        listOfGreetings.add("hallo");
        System.out.println(" ");
        listOfGreetings.remove("gello");
    }

    public static void useHttpClient(HttpClient httpClient) {
        httpClient.initialize();
        String response = httpClient.sendRequest("some request");

        System.out.println(String.format("Http response is : %s", response));
    }

    public static void useDatabaseReader(DatabaseReader databaseReader) throws InterruptedException {
        int rowsInGamesTable = 0;
        try {
            rowsInGamesTable = databaseReader.countRowsInTable("GamesTable");
        } catch (IOException e) {
            System.out.println("Catching and logging exception " + e);
            return;
        }


        System.out.println(String.format("There are %s rows in GamesTable", rowsInGamesTable));

        String[] data = databaseReader.readRow("SELECT * from GamesTable");

        System.out.println(String.format("Received %s", String.join(" , ", data)));
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Object originalObject){
        Class<?>[] interfaces = originalObject.getClass().getInterfaces();
        TimeMeasuringProxyHandler timeMeasuringProxyHandler = new TimeMeasuringProxyHandler(originalObject);

        return (T) Proxy.newProxyInstance(originalObject.getClass().getClassLoader(), interfaces, timeMeasuringProxyHandler);

    }

    public static class TimeMeasuringProxyHandler implements InvocationHandler{
        private final Object originalObject;

        public TimeMeasuringProxyHandler(Object originalObject) {
            this.originalObject = originalObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result;
            System.out.println("Measuring Proxy - Before execution method: " + method.getName());

            long startTime = System.currentTimeMillis();
            try{
                result = method.invoke(originalObject, args);
            }catch (InvocationTargetException e){
                throw e.getTargetException();
            }
            long endTime = System.currentTimeMillis();

            System.out.printf("Measuring Proxy - Execution of %s took %dms\n", method.getName(), endTime-startTime);

            return result;
        }
    }

}
