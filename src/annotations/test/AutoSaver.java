package annotations.test;

import annotations.annotations.InitializerClass;
import annotations.annotations.InitializerMethod;

@InitializerClass
public class AutoSaver {

    @InitializerMethod
    public void startAutoSavingThreads(){
        System.out.println("Start automating data saving to disk");
    }
}
