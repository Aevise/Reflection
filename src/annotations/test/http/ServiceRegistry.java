package annotations.test.http;

import annotations.annotations.InitializerClass;
import annotations.annotations.InitializerMethod;

@InitializerClass
public class ServiceRegistry {

    @InitializerMethod
    public void registerService(){
        System.out.println("Service successfully registered");
    }
}
