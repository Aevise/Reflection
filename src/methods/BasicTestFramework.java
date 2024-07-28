package methods;

import methods.data.ClothingProduct;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class BasicTestFramework {
    public static void main(String[] args) {
        testGetters(ClothingProduct.class);
        testSetters(ClothingProduct.class);
    }

    public static void testSetters(Class<?> dataClass) {
        List<Field> allFields = getAllFields(dataClass);

        for (Field field : allFields) {
            String setterName = "set" + capitalizeFirstLetter(field.getName());

            Method setterMethod = null;
            try {
                setterMethod = dataClass.getMethod(setterName, field.getType());
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(String.format("Setter: %s not found", setterName));
            }

            if (!setterMethod.getReturnType().equals(void.class)) {
                throw new IllegalStateException(String.format("Setter method: %s has to return void", setterName));
            }
        }

    }

    public static void testGetters(Class<?> dataClass) {
        List<Field> allFields = getAllFields(dataClass);

        Map<String, Method> methodMap = mapMethodNameToMethod(dataClass);

        for (Field field : allFields) {
            String getterName = "get" + capitalizeFirstLetter(field.getName());

            if (!methodMap.containsKey(getterName)) {
                throw new IllegalStateException(String.format("Field: %s does not have a getter method", field.getName()));
            }

            Method getter = methodMap.get(getterName);
            if (!getter.getReturnType().equals(field.getType())) {
                throw new IllegalStateException(String.format("Getter method: %s has return type %s, but expected: %s",
                        getter.getName(), getter.getReturnType().getTypeName(), field.getType().getTypeName()));
            }

            if (getter.getParameterCount() > 0) {
                throw new IllegalStateException(String.format("Method %s has more than 0 parameters", getter.getName()));
            }

        }
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        if (clazz == null || clazz.equals(Object.class)) {
            return List.of();
        }
        List<Field> allFields = new ArrayList<>();

        Field[] classFields = clazz.getDeclaredFields();
        List<Field> superClassFields = getAllFields(clazz.getSuperclass());

        allFields.addAll(Arrays.asList(classFields));
        allFields.addAll(superClassFields);

        return allFields;

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
