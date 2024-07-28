package fields.arrays;

import java.lang.reflect.Array;

public class Main {
    public static void main(String[] args) {
        String[] strings = new String[]{"123", "gege", "test"};
        int[][] doubles = new int[][]{{1, 2, 3}, {4, 5, 6}, {1}};
        int[][][] triple = new int[][][]{
                {
                        {1, 2, 3}, {4, 5, 6}
                },
                {
                        {1, 2, 3}, {4, 5, 6}
                },
        };

        inspectArrayObject(strings);
        inspectArrayObject(doubles);
        inspectArrayObject(triple);

        System.out.println(inspectArrayValues(strings));
        System.out.println(inspectArrayValues(doubles));
        System.out.println(inspectArrayValues(triple));
    }

    public static String inspectArrayValues(Object array) {
        int length = Array.getLength(array);
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        for (int i = 0; i < length; i++) {
            Object element = Array.get(array, i);

            if (element.getClass().isArray()) {
                stringBuilder.append(inspectArrayValues(element));
            } else {
                stringBuilder.append(element);
            }

            if (i != length - 1) {
                stringBuilder.append(", ");
            }

        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static void inspectArrayObject(Object array) {
        Class<?> aClass = array.getClass();

        if (aClass.isArray()) {
            System.out.println("Object is an array");

            System.out.printf("Array is of type: %s\n", aClass.getComponentType().getTypeName());
        }
    }
}
