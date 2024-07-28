package theory.constructors;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class Constructors {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Constructor<?>[] declaredConstructors = Class1.class.getDeclaredConstructors();
        Constructor<?>[] constructors = Class1.class.getConstructors();


        System.out.printf("%s\n%s\n", Arrays.toString(declaredConstructors), Arrays.toString(constructors));

        System.out.println("----------");

        Constructor<?>[] declaredConstructors1 = Class2.class.getDeclaredConstructors();
        Constructor<?>[] constructors1 = Class2.class.getConstructors();
        System.out.printf("%s\n%s\n", Arrays.toString(declaredConstructors1), Arrays.toString(constructors1));


        System.out.println("Constructor:");
        for (Constructor<?> constructor : constructors) {
            System.out.println(constructor);
        }

        Constructor<Class1> constructor = Class1.class.getConstructor(String.class);
        System.out.println("Result of getting given constructor: " + constructor);
//        Constructor<Class1> constructor1 = Class1.class.getConstructor();
//        Constructor<Class2> constructor2 = Class2.class.getConstructor();

        System.out.println("hmm:\n" + Class3.class.getConstructor());

        printConstructorData(Person.class);

        Object tomasz = createInstanceWithArguments(Person.class, "Tomasz");
        System.out.println(tomasz);

        Object instanceWithArguments = createInstanceWithArguments(Person.class, 12, new Address("Kek", 1), "Tomasz");
        System.out.println(instanceWithArguments);
    }

    public static void printConstructorData(Class<?> clazz) {
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        System.out.printf("Class %s has %d declared constructors:\n", clazz.getSimpleName(), declaredConstructors.length);
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            System.out.println(declaredConstructor);
            List<String> parameters = Arrays.stream(declaredConstructor.getParameterTypes())
                    .map(Class::getSimpleName)
                    .toList();
            System.out.println("Takes: " + parameters);
        }
    }

    public static <T> T createInstanceWithArguments(Class<T> clazz, Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Constructor<?> declaredConstructor : clazz.getDeclaredConstructors()) {
            if (declaredConstructor.getParameterTypes().length == args.length) {
                return (T) declaredConstructor.newInstance(args);
            }
        }
        System.out.println("No appropriate constructor found");
        return null;
    }

    public static class Person {

        private final int age;

        private final Address address;
        private final String name;

        public Person() {
            this.name = "anonymous";
            this.age = 0;
            this.address = null;
        }

        public Person(String name) {
            this.name = name;
            this.age = 0;
            this.address = null;
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
            this.address = null;
        }

        public Person(int age, Address address, String name) {
            this.age = age;
            this.address = address;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age=" + age +
                    ", address=" + address +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class Address {
        private String street;
        private int number;

        public Address(String street, int number) {
            this.street = street;
            this.number = number;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "street='" + street + '\'' +
                    ", number=" + number +
                    '}';
        }
    }
}
