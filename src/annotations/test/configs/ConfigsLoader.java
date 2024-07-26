package annotations.test.configs;

import annotations.annotations.InitializerClass;
import annotations.annotations.InitializerMethod;

@InitializerClass
public class ConfigsLoader {

    @InitializerMethod
    public void loadAllConfigs(){
        System.out.println("Loading all configuration files");
    }
}
