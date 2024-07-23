package dependencyInjection;

import dependencyInjection.game.Game;
import dependencyInjection.game.internal.TicTacToeGame;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Game game = createObjectRecursively(TicTacToeGame.class);

        game.startGame();
    }

    public static <T> T createObjectRecursively(Class<T> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = getFirstConstructor(clazz);

        List<Object> constructorArguments = new ArrayList<>();

        for (Class<?> parameterType : constructor.getParameterTypes()) {
            Object parameterValue = createObjectRecursively(parameterType);
            constructorArguments.add(parameterValue);
        }
        constructor.setAccessible(true);
        return (T) constructor.newInstance(constructorArguments.toArray());
    }

    private static Constructor<?> getFirstConstructor(Class<?> clazz){
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        if(declaredConstructors.length == 0){
            throw new IllegalStateException(String.format("No constructor has been found for class %s", clazz.getName()));
        }
        return declaredConstructors[0];
    }
}
