package annotations.test;

import annotations.test.annotations.InitializerClass;
import annotations.test.annotations.InitializerMethod;

@InitializerClass
public class AutoSaver {

    @InitializerMethod
    public void startAutoSavingThreads() {
        System.out.println("Start automating data saving to disk");
    }
}
