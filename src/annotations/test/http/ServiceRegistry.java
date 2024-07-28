package annotations.test.http;

import annotations.test.annotations.InitializerClass;
import annotations.test.annotations.InitializerMethod;

@InitializerClass
public class ServiceRegistry {

    @InitializerMethod
    public void registerService() {
        System.out.println("Service successfully registered");
    }
}
