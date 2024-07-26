package arrays;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayFlattening {
    public static void main(String[] args) {
        ArrayFlattening arrayFlattening = new ArrayFlattening();

        Object concat = arrayFlattening.concat(int.class, 1, 2, 3, new int[]{4, 5, 6}, 7);
    }

    public <T> T concat(Class<?> type, Object... arguments) {

        if (arguments.length == 0) {
            return null;
        }

        List<Object> elements = new ArrayList<>();
        for (Object argument : arguments) {
            if (argument != null && argument.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(argument); i++) {
                    elements.add(Array.get(argument, i));
                }
            } else {
                elements.add(argument);
            }
        }

        T flattenedArray = (T) Array.newInstance(type, elements.size());
        for (int i = 0; i < elements.size(); i++) {
            Array.set(flattenedArray, i, elements.get(i));
        }

        return flattenedArray;
    }
}
