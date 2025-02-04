package annotations.parametersAnnotations;

import annotations.parametersAnnotations.annotations.Annotations;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
//        BestGameFinder bestGamesFinder = new BestGameFinder();
//
//        List<String> bestGamesInDescendingOrder = execute(bestGamesFinder);
//
//        System.out.println(bestGamesInDescendingOrder);

        SQLQueryBuilder sqlQueryBuilder = new SQLQueryBuilder(Arrays.asList("1", "2", "3"),
                10,
                "Movies",
                Arrays.asList("Id", "Name"));
        Object execute = execute(sqlQueryBuilder);
        System.out.println(execute);
//        System.out.println(execute(sqlQueryBuilder));?
    }

    public static <T> T execute(Object instance) throws InvocationTargetException, IllegalAccessException {
        Class<?> clazz = instance.getClass();

        Map<String, Method> operationToMethod = getOperationToMethod(clazz);
        Map<String, Field> inputToField = getInputToField(clazz);

        Method finalResultMethod = findFinalResultMethod(clazz);

        return (T) executeWithDependencies(instance, finalResultMethod, operationToMethod, inputToField);
    }

    private static Map<String, Field> getInputToField(Class<?> clazz) {
        Map<String, Field> inputToField = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Annotations.Input.class)) {
                continue;
            }
            Annotations.Input input = field.getAnnotation(Annotations.Input.class);
            inputToField.put(input.value(), field);
        }
        return inputToField;
    }

    private static Object executeWithDependencies(Object instance, Method finalMethod, Map<String, Method> operationToMethod, Map<String, Field> inputToField)
            throws InvocationTargetException, IllegalAccessException {
        List<Object> parameterValues = new ArrayList<>(finalMethod.getParameterCount());

        for (Parameter parameter : finalMethod.getParameters()) {
            Object value = null;
            if (parameter.isAnnotationPresent(Annotations.DependsOn.class)) {
                String dependencyOperationName = parameter.getAnnotation(Annotations.DependsOn.class).value();
                Method dependencyMethod = operationToMethod.get(dependencyOperationName);

                value = executeWithDependencies(instance, dependencyMethod, operationToMethod, inputToField);
            } else if (parameter.isAnnotationPresent(Annotations.Input.class)) {
                String inputName = parameter.getAnnotation(Annotations.Input.class).value();

                Field field = inputToField.get(inputName);
                field.setAccessible(true);
                value = field.get(instance);

            }

            parameterValues.add(value);
        }

        return finalMethod.invoke(instance, parameterValues.toArray());
    }

    private static Map<String, Method> getOperationToMethod(Class<?> clazz) {
        Map<String, Method> operationNameToMethod = new HashMap<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Annotations.Operation.class)) {
                continue;
            }

            Annotations.Operation operation = method.getAnnotation(Annotations.Operation.class);

            operationNameToMethod.put(operation.value(), method);
        }
        return operationNameToMethod;
    }

    private static Method findFinalResultMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Annotations.FinalResult.class)) {
                return method;
            }
        }

        throw new RuntimeException("No method found with FinalResult annotation");
    }
}
