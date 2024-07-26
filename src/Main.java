import interfaceAnalyzer.InterfaceAnalyzer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.*;

import static annotations.Main.initialize;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException, URISyntaxException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        Set<Class<?>> allImplementedInterfaces = InterfaceAnalyzer.findAllImplementedInterfaces(Collection.class);
        System.out.println(allImplementedInterfaces);
        System.out.println("-----------------------");
        Set<Class<?>> allImplementedInterfaces1 = InterfaceAnalyzer.findAllImplementedInterfaces(ListIterator.class);
        System.out.println(allImplementedInterfaces1);
        System.out.println("-----------------------");

        Class<String> stringClass = String.class;

        List<String> names = new ArrayList<>();
        Class<? extends List> aClass1 = names.getClass();
        System.out.println(aClass1);
        System.out.println(aClass1.getTypeName());
        System.out.println(aClass1.getName());
        System.out.println(aClass1.getSimpleName());


        String name = aClass1.getPackage().getName();
        String packageName = aClass1.getPackageName();
        System.out.println(name +"    " + packageName);


        Map<String, Integer> map = new HashMap<>();
        Class<? extends Map> aClass = map.getClass();

        Class<?> squareClass = Class.forName("Main$Square");

        Class<Comparable> comparableClass = Comparable.class;

        printClassInfo(stringClass, aClass, squareClass);

        System.out.println("----------------------------");

        var circleObject = new Drawable() {
            @Override
            public int getNumberOfCorners() {
                return 0;
            }
        };

        printClassInfo(Collection.class, boolean.class, int[][].class, Color.class, circleObject.getClass());

        System.out.println("+++++++++++++------------------+++++++++++++++");
    }

    private static void printClassInfo(Class<?> ... classes){
        for (Class<?> aclass : classes) {
            System.out.printf("Class name: [%s], class package: [%s]%n", aclass.getSimpleName(), aclass.getPackageName());

            Class<?>[] implementedInterfaces = aclass.getInterfaces();
            for (Class<?> implementedInterface : implementedInterfaces) {
                System.out.printf("Class [%s] implements: [%s]\n", aclass.getSimpleName(), implementedInterface.getSimpleName());
            }
            System.out.println("Is array: " + aclass.isArray());
            System.out.println("Is primitive: " + aclass.isPrimitive());
            System.out.println("Is enum: " + aclass.isEnum());
            System.out.println("Is interface: " + aclass.isInterface());
            System.out.println("Is anonymous class: " + aclass.isAnonymousClass());

            System.out.println();
        }

    }

    private static class Square implements Drawable {
        @Override
        public int getNumberOfCorners() {
            return 4;
        }
    }

    private static interface Drawable {
        int getNumberOfCorners();
    }

    private enum Color {
        BLUE,
        RED,
        GREEN
    }
}