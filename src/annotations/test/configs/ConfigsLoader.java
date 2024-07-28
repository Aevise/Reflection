package annotations.test.configs;

import annotations.test.annotations.InitializerClass;
import annotations.test.annotations.InitializerMethod;

@InitializerClass
public class ConfigsLoader {

    @InitializerMethod
    public void loadAllConfigs() {
        System.out.println("Loading all configuration files");
    }
}
