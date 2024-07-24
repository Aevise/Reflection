package fields.arrays;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;

public class getArrayElement {

    public static void main(String[] args) throws IllegalAccessException {
        int [] input = new int[] {0, 10, 20, 30, 40};

        getArrayElement getArrayElement = new getArrayElement();
        System.out.println(getArrayElement.getArrayElement(input, 0));
        System.out.println(getArrayElement.getArrayElement(input, 1));
        System.out.println(getArrayElement.getArrayElement(input, 2));
        System.out.println(getArrayElement.getArrayElement(input, 3));
        System.out.println(getArrayElement.getArrayElement(input, 4));
        System.out.println("-----------------------------------");
        System.out.println(getArrayElement.getArrayElement(input, 0));
        System.out.println(getArrayElement.getArrayElement(input, -1));
        System.out.println(getArrayElement.getArrayElement(input, -2));
        System.out.println(getArrayElement.getArrayElement(input, -3));
        System.out.println(getArrayElement.getArrayElement(input, -4));
    }

    public Object getArrayElement(Object array, int index){
        if(array.getClass().isArray()){
            if (index >= 0) {
                return Array.get(array, index);
            }
            int arrayLength = Array.getLength(array);
            return Array.get(array, arrayLength + index);
        }
        throw new IllegalArgumentException("Given object is not an array!");
    }
}
