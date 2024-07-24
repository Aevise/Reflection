package fields;

import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {
//        printDeclaredFieldsInfo(Movie.class);
//        printFieldsInfo(Movie.class);

//        printDeclaredFieldsInfo(Product.class);
//        printFieldsInfo(Product.class);

//        printDeclaredFieldsInfo(Category.class);
//        printFieldsInfo(Category.class);

//        printDeclaredFieldsInfo(Movie.MovieStats.class);

        Movie movie = new Movie("Hehee", 2002, 12.87, true, Category.ADVENTURE);
        printDeclaredFieldsInfo(movie.getClass(), movie);


    }

    public static <T> void printDeclaredFieldsInfo(Class<? extends T> clazz, T instance) throws IllegalAccessException {
        for (Field declaredField : clazz.getDeclaredFields()) {
            System.out.printf("Field name: %s type: %s\n",
                    declaredField.getName(),
                    declaredField.getType().getName());

            System.out.printf("Is field synthetic: %s\n", declaredField.isSynthetic());
            System.out.printf("Field value: %s\n", declaredField.get(instance));

            System.out.printf("Difference between name and simpleName:\nName: %s\nSimpleName: %s\n",
                    declaredField.getType().getName(),
                    declaredField.getType().getSimpleName());
            System.out.println(" ");
        }
    }

    public static void printDeclaredFieldsInfo(Class<?> clazz){
        for (Field declaredField : clazz.getDeclaredFields()) {
            System.out.printf("Field name: %s type: %s\n",
                    declaredField.getName(),
                    declaredField.getType().getName());

            System.out.printf("Is field synthetic: %s\n", declaredField.isSynthetic());

            System.out.printf("Difference between name and simpleName:\nName: %s\nSimpleName: %s\n",
                    declaredField.getType().getName(),
                    declaredField.getType().getSimpleName());
            System.out.println(" ");
        }
    }

    public static void printFieldsInfo(Class<?> clazz){
        for (Field declaredField : clazz.getFields()) {
            System.out.printf("Field name: %s\ntype: %s\n",
                    declaredField.getName(),
                    declaredField.getType().getName());

            System.out.printf("Difference between name and simpleName:\nName: %s\nSimpleName: %s\n",
                    declaredField.getType().getName(),
                    declaredField.getType().getSimpleName());
            System.out.println(" ");
        }
    }

    public static class Movie extends Product{
        public static final double MINIMUM_PRICE = 10.99;

        private boolean isReleased;
        private Category category;
        private double actualPrice;

        public Movie(String name, int year, double price, boolean isReleased, Category category) {
            super(name, year);
            this.isReleased = isReleased;
            this.category = category;
            this.actualPrice = Math.max(price, MINIMUM_PRICE);
        }

        public class MovieStats{
            private double timesWatched;

            public MovieStats(double timesWatched) {
                this.timesWatched = timesWatched;
            }

            public double getRevenue(){
                return timesWatched * actualPrice;
            }
        }
    }

    public static class Product{
        protected String name;
        protected int year;
        protected double actualPrice;

        public Product(String name, int year) {
            this.name = name;
            this.year = year;
        }
    }

    public enum Category{
        ADVENTURE,
        ACTION,
        COMEDY
    }
}
