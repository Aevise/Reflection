package annotations.repeatableAnnotations;

import annotations.repeatableAnnotations.annotations.Annotations;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Annotations.ScanPackages({"repeatableAnnotations.loaders"})
public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException {
        schedule();
    }

    public static void schedule() throws IOException, URISyntaxException, ClassNotFoundException {
        Annotations.ScanPackages scanPackages = Main.class.getAnnotation(Annotations.ScanPackages.class);
        if (scanPackages == null || scanPackages.value().length == 0) {
            return;
        }

        List<Class<?>> allClasses = getAllClasses(scanPackages.value());
        List<Method> scheduledExecutorMethod = getScheduledExecutorMethod(allClasses);

        for (Method method : scheduledExecutorMethod) {
            scheduleMethodExecution(method);
        }
    }

    private static void scheduleMethodExecution(Method method) {
        Annotations.ExecuteOnSchedule[] schedules = method.getAnnotationsByType(Annotations.ExecuteOnSchedule.class);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        for (Annotations.ExecuteOnSchedule schedule : schedules) {
            executorService.scheduleAtFixedRate(
                    () -> runWhenScheduled(method),
                    schedule.delaySeconds(),
                    schedule.periodSeconds(),
                    TimeUnit.SECONDS
            );
        }

    }

    private static void runWhenScheduled(Method method) {
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        System.out.println("Executing at: " + simpleDateFormat.format(currentDate));

        try {
            method.invoke(null);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static List<Method> getScheduledExecutorMethod(List<Class<?>> allClasses) {
        List<Method> scheduledMethods = new ArrayList<>();

        for (Class<?> clazz : allClasses) {
            if (!clazz.isAnnotationPresent(Annotations.ScheduledExecutorClass.class)) {
                continue;
            }
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getAnnotationsByType(Annotations.ExecuteOnSchedule.class).length != 0) {
                    scheduledMethods.add(method);
                }
            }
        }
        return scheduledMethods;
    }

    private static List<Class<?>> getAllClasses(String... packageNames) throws IOException, ClassNotFoundException, URISyntaxException {
        List<Class<?>> allClasses = new ArrayList<>();

        for (String packageName : packageNames) {
            String packageRelativePath = packageName.replace(".", "/");

            URI packageUri = annotations.Main.class.getResource(packageRelativePath).toURI();

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
}
