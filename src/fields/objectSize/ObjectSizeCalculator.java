package fields.objectSize;

import java.lang.reflect.Field;

public class ObjectSizeCalculator {
    private static final long HEADER_SIZE = 12;
    private static final long REFERENCE_SIZE = 4;

    public static void main(String[] args) throws IllegalAccessException {
        ObjectSizeCalculator objectSizeCalculator = new ObjectSizeCalculator();
        long asere = objectSizeCalculator.sizeOfObject("asere");
        System.out.println(asere);
    }

    public long sizeOfObject(Object input) throws IllegalAccessException {
        long objectSize = HEADER_SIZE + REFERENCE_SIZE;
        Field[] declaredFields = input.getClass().getDeclaredFields();

        for (Field field : declaredFields) {
            Class<?> fieldType = field.getType();
            field.setAccessible(true);

            if (field.isSynthetic()) {
                continue;
            }


            if (fieldType.isPrimitive()) {
                objectSize += sizeOfPrimitiveType(fieldType);
            } else if (fieldType.equals(String.class)) {
                objectSize += sizeOfString((String) field.get(input));
            } else {
                objectSize += sizeOfObject(field.get(input));
            }
        }

        return objectSize;
    }


    /*************** Helper methods ********************************/
    private long sizeOfPrimitiveType(Class<?> primitiveType) {
        if (primitiveType.equals(int.class)) {
            return 4;
        } else if (primitiveType.equals(long.class)) {
            return 8;
        } else if (primitiveType.equals(float.class)) {
            return 4;
        } else if (primitiveType.equals(double.class)) {
            return 8;
        } else if (primitiveType.equals(byte.class)) {
            return 1;
        } else if (primitiveType.equals(short.class)) {
            return 2;
        }
        throw new IllegalArgumentException(String.format("Type: %s is not supported", primitiveType));
    }

    private long sizeOfString(String inputString) {
        int stringBytesSize = inputString.getBytes().length;
        return HEADER_SIZE + REFERENCE_SIZE + stringBytesSize;
    }
}
