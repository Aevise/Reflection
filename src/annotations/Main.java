package annotations;

import annotations.test.annotations.InitializerClass;
import annotations.test.annotations.InitializerMethod;
import annotations.test.annotations.RetryOperation;
import annotations.test.annotations.ScanPackages;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@ScanPackages({"test", "test.configs", "test.databases", "test.http"})
public class Main {

    public static void main(String[] args) throws Throwable {
        initialize();

    }

    public static void initialize() throws Throwable {
        ScanPackages annotation = Main.class.getAnnotation(ScanPackages.class);

        if (annotation == null || annotation.value().length == 0) {
            return;
        }

        List<Class<?>> classes = getAllClasses(annotation.value());

        for (Class<?> aClass : classes) {
            if (!aClass.isAnnotationPresent(InitializerClass.class)) {
                continue;
            }
            List<Method> methods = getAllInitializingMethods(aClass);

            Object instance = aClass.getDeclaredConstructor().newInstance();

            for (Method method : methods) {
                callInitializingMethod(instance, method);
            }
        }
    }

    private static void callInitializingMethod(Object instance, Method method) throws Throwable {
        RetryOperation retryOperation = method.getAnnotation(RetryOperation.class);

        int numberOfRetries = retryOperation == null ? 0 : retryOperation.numberOfRetries();

        while (true) {
            try {
                method.invoke(instance);
                break;
            } catch (InvocationTargetException e) {
                Throwable targetException = e.getTargetException();

                if (numberOfRetries > 0 && Set.of(retryOperation.retryExceptions()).contains(targetException.getClass())) {
                    numberOfRetries--;

                    System.out.println("Retrying...");
                    Thread.sleep(retryOperation.durationBetweenRetriesMs());
                } else if (retryOperation != null) {
                    throw new Exception(retryOperation.failureMessage(), targetException);
                } else {
                    throw targetException;
                }
            }
        }
    }

    private static List<Class<?>> getAllClasses(String... packageNames) throws IOException, ClassNotFoundException, URISyntaxException {
        List<Class<?>> allClasses = new ArrayList<>();

        for (String packageName : packageNames) {
            String packageRelativePath = packageName.replace(".", "/");

            URI packageUri = Main.class.getResource(packageRelativePath).toURI();

            if (packageUri.getScheme().equals("file")) {
                Path packageFullPath = Paths.get(packageUri);
                allClasses.addAll(getAllPackageClasses(packageFullPath, packageName));
            } else if (packageUri.getScheme().equals("jar")) {
                FileSystem fileSystem = FileSystems.newFileSystem(packageUri, Collections.emptyMap());

                Path packageFullPathJar = fileSystem.getPath(packageRelativePath);
                allClasses.addAll(getAllPackageClasses(packageFullPathJar, packageName));

                fileSystem.close();
            }
        }
        return allClasses;
    }


    private static List<Class<?>> getAllPackageClasses(Path packagePath, String packageName) throws IOException, ClassNotFoundException {
        if (!Files.exists(packagePath)) {
            return List.of();
        }

        List<Path> files = Files.list(packagePath)
                .filter(Files::isRegularFile)
                .toList();

        List<Class<?>> classes = new ArrayList<>();
        for (Path file : files) {
            String fileName = file.getFileName().toString();

            if (fileName.endsWith(".class")) {
                //annotations hack
                String classFullName = "annotations." + packageName + "." + fileName.replaceFirst("\\.class$", "");
                Class<?> aClass = Class.forName(classFullName);
                classes.add(aClass);
            }
        }
        return classes;
    }

    private static List<Method> getAllInitializingMethods(Class<?> clazz) {
        List<Method> initializingMethods = new ArrayList<>();

        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(InitializerMethod.class)) {
                initializingMethods.add(declaredMethod);
            }
        }
        return initializingMethods;
    }
}
