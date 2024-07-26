package methods;

import methods.data.Product;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BasicTestFramework {
    public static void main(String[] args) {
        testGetters(Product.class);
    }

    public static void testGetters(Class<?> dataClass) {
        Field[] declaredFields = dataClass.getDeclaredFields();

        Map<String, Method> methodMap = mapMethodNameToMethod(dataClass);

        for (Field declaredField : declaredFields) {
            String getterName = "get" + capitalizeFirstLetter(declaredField.getName());

            if (!methodMap.containsKey(getterName)) {
                throw new IllegalStateException(String.format("Field: %s does not have a getter method", declaredField.getName()));
            }

            Method getter = methodMap.get(getterName);
            if (!getter.getReturnType().equals(declaredField.getType())) {
                throw new IllegalStateException(String.format("Getter method: %s has return type %s, but expected: %s",
                        getter.getName(), getter.getReturnType().getTypeName(), declaredField.getType().getTypeName()));
            }

            if (getter.getParameterCount() > 0) {
                throw new IllegalStateException(String.format("Method %s has more than 0 parameters", getter.getName()));
            }

        }
    }

    private static String capitalizeFirstLetter(String name) {
        return name.substring(0, 1).toUpperCase().concat(name.substring(1));
    }

    private static Map<String, Method> mapMethodNameToMethod(Class<?> dataClass) {
        Method[] methods = dataClass.getMethods();

        Map<String, Method> nameToMethod = new HashMap<>();

        for (Method method : methods) {
            nameToMethod.put(method.getName(), method);
        }
        return nameToMethod;

    }
}
