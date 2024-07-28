package annotations.test.databases;

import annotations.test.annotations.InitializerClass;
import annotations.test.annotations.InitializerMethod;
import annotations.test.annotations.RetryOperation;

import java.io.IOException;

@InitializerClass
public class DatabaseConnection {
    private int failCounter = 5;

    @InitializerMethod
    @RetryOperation(
            numberOfRetries = 10,
            retryExceptions = IOException.class,
            durationBetweenRetriesMs = 1,
            failureMessage = "Connection to database 1 failed after retries"
    )
    public void connectToDatabase1() throws IOException {
        System.out.println("Connecting to DB 1");
        if (failCounter > 0) {
            failCounter--;
            throw new IOException("Connection failed");
        }
        System.out.println("Connected to DB 1");
    }

    @InitializerMethod
    @RetryOperation(numberOfRetries = 10)
    public void connectToDatabase2() {
        System.out.println("Connected to DB 2");
    }
}
