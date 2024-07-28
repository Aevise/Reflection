package fields_setting;

import fields_setting.data.GameConfig;
import fields_setting.data.UserInterfaceConfig;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    private static final Path GAME_CONFIG_PATH = Path.of("src/fields_setting/resources/game-properties.cfg");
    private static final Path USER_INTERFACE_PATH = Path.of("src/fields_setting/resources/user-interface.cfg");


    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        GameConfig config = createConfigObject(GameConfig.class, GAME_CONFIG_PATH);
        UserInterfaceConfig config2 = createConfigObject(UserInterfaceConfig.class, USER_INTERFACE_PATH);
        System.out.println(config);
        System.out.println(config2);
    }

    public static <T> T createConfigObject(Class<T> clazz, Path filePath)
            throws IOException, NoSuchMethodException,
            InvocationTargetException, InstantiationException,
            IllegalAccessException {

        Scanner scanner = new Scanner(filePath);

        Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);

        T configInstance = (T) declaredConstructor.newInstance();

        while (scanner.hasNextLine()) {
            String configLine = scanner.nextLine();

            String[] nameValuePair = configLine.split("=");
            String propertyName = nameValuePair[0];
            String propertyValue = nameValuePair[1];

            Field declaredField = null;
            try {
                declaredField = clazz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                System.out.println("Property name: " + declaredField + " is unsupported");
                continue;
            }

            declaredField.setAccessible(true);
            Object parsedValue;
            if (declaredField.getType().isArray()) {
                parsedValue = parseArray(declaredField.getType().getComponentType(), propertyValue);
            } else {
                parsedValue = parseValue(declaredField.getType(), propertyValue);
            }
            declaredField.set(configInstance, parsedValue);
        }
        return configInstance;
    }

    private static Object parseArray(Class<?> arrayElementType, String value) {
        String[] elementValues = value.split(",");

        Object newArray = Array.newInstance(arrayElementType, elementValues.length);

        for (int i = 0; i < elementValues.length; i++) {
            Array.set(newArray, i, parseValue(arrayElementType, elementValues[i]));
        }
        return newArray;
    }

    private static Object parseValue(Class<?> type, String propertyValue) {
        if (type.equals(int.class)) {
            return Integer.parseInt(propertyValue);
        } else if (type.equals(short.class)) {
            return Short.parseShort(propertyValue);
        } else if (type.equals(long.class)) {
            return Long.parseLong(propertyValue);
        } else if (type.equals(double.class)) {
            return Double.parseDouble(propertyValue);
        } else if (type.equals(float.class)) {
            return Float.parseFloat(propertyValue);
        } else if (type.equals(String.class)) {
            return propertyValue;
        }

        throw new RuntimeException(String.format("Type %s is not supported", type.getTypeName()));
    }
}
