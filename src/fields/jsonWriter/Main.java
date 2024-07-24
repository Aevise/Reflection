package fields.jsonWriter;

import fields.data.Address;
import fields.data.Person;
import fields.jsonWriter.data.Actor;
import fields.jsonWriter.data.Movie;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Address address = new Address("Ulica", (short) 1);
        Person person = new Person("John", true, 28, 756.23f, address);
        System.out.println(objectToJson(person, 0));

        Actor actor = new Actor("Tomash hehe", new String[]{"The Last Man", "Not Really!"});
        Actor actor1 = new Actor("Khrysia hehe", new String[]{"The Last Man", "Not Really!", "Dummy"});
        Actor actor2 = new Actor("Fejbian xd", new String[]{"The Last Man", "Not Really!", "Sniper Elite"});
        Movie movie = new Movie("The Last Man", 9.8f, new String[]{"Action", "Romance", "Tomash"}, new Actor[]{actor, actor1, actor2});

        System.out.println(objectToJson(actor, 0));
        System.out.println(objectToJson(movie, 0));
    }


    public static String objectToJson(Object instance, int indentSize) throws IllegalAccessException {
        Field[] declaredFields = instance.getClass().getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(indent(indentSize));
        stringBuilder.append("{");
        stringBuilder.append("\n");

        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            field.setAccessible(true);

            if(field.isSynthetic()){
                continue;
            }

            stringBuilder.append(indent(indentSize + 1));
            stringBuilder.append(formatStringValue(field.getName()));

            stringBuilder.append(":");

            Class<?> fieldType = field.getType();
            if(fieldType.isPrimitive()){
                stringBuilder.append(formatPrimitiveValue(field.get(instance), fieldType));
            }else if(fieldType.equals(String.class)){
                stringBuilder.append(formatStringValue(field.get(instance).toString()));
            }else if(fieldType.isArray()){
                stringBuilder.append(arrayToJson(field.get(instance), indentSize + 1));
            }
            else {
                stringBuilder.append(objectToJson(field.get(instance), indentSize + 1));
            }

            if(i != declaredFields.length - 1){
                stringBuilder.append(",");
            }
            stringBuilder.append("\n");
        }

        stringBuilder.append(indent(indentSize));
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    private static String arrayToJson(Object arrayInstance, int indentSize) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();
        int arrayLength = Array.getLength(arrayInstance);

        Class<?> componentType = arrayInstance.getClass().getComponentType();

        stringBuilder.append("[\n");

        for (int i = 0; i < arrayLength; i++) {
            Object element = Array.get(arrayInstance, i);

            if(componentType.isPrimitive()){
                stringBuilder.append(indent(indentSize + 1));
                stringBuilder.append(formatPrimitiveValue(element, componentType));
            } else if(componentType.equals(String.class)){
                stringBuilder.append(indent(indentSize + 1));
                stringBuilder.append(formatStringValue(element.toString()));
            }else {
                stringBuilder.append(objectToJson(element, indentSize + 1));
            }

            if( i != arrayLength - 1){
                stringBuilder.append(", ");
            }
            stringBuilder.append("\n");
        }
        stringBuilder.append(indent(indentSize) + "]");
        return stringBuilder.toString();
    }

    private static String indent(int indentSize){
        return "\t".repeat(Math.max(0, indentSize));
    }

    private static String formatStringValue(String value){
        return String.format("\"%s\"", value);
    }

    private static String formatPrimitiveValue(Object instance, Class<?> type) {
        if(type.equals(boolean.class)
        || type.equals(int.class)
        || type.equals(long.class)
        || type.equals(short.class)
        ){
            return instance.toString();
        }else if(type.equals(double.class) || type.equals(float.class)){
            return String.format("%.02f", instance);
        }
        throw new RuntimeException(String.format("Type: %s is not supported", type.getName()));
    }
}
