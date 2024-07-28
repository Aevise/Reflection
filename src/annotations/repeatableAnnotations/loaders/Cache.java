package annotations.repeatableAnnotations.loaders;

import annotations.repeatableAnnotations.annotations.Annotations;

@Annotations.ScheduledExecutorClass
public class Cache {
    @Annotations.ExecuteOnSchedule(periodSeconds = 5)
    @Annotations.ExecuteOnSchedule(delaySeconds = 10, periodSeconds = 1)
    public static void reloadCache() {
        System.out.println("Reloading cache");
    }
}
