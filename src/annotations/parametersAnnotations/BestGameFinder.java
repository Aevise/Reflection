package annotations.parametersAnnotations;

import annotations.parametersAnnotations.annotations.Annotations;
import annotations.parametersAnnotations.database.Database;

import java.util.*;

public class BestGameFinder {

    private Database database = new Database();

    @Annotations.Operation("All-Games")
    public Set<String> getAllGames() {
        return database.readAllGames();
    }

    @Annotations.Operation("Game-To-Price")
    public Map<String, Float> getGameToPrice(@Annotations.DependsOn("All-Games") Set<String> allGames) {
        return database.readGameToPrice(allGames);
    }

    @Annotations.Operation("Game-To-Rating")
    public Map<String, Float> getGameToRating(@Annotations.DependsOn("All-Games") Set<String> allGames) {
        return database.readGameToRatings(allGames);
    }

    @Annotations.Operation("Score-To-Game")
    public SortedMap<Double, String> scoreGames(@Annotations.DependsOn("Game-To-Price") Map<String, Float> gameToPrice,
                                                @Annotations.DependsOn("Game-To-Rating") Map<String, Float> gameToRating) {
        SortedMap<Double, String> gameToScore = new TreeMap<>(Collections.reverseOrder());
        for (String gameName : gameToPrice.keySet()) {
            double score = (double) gameToRating.get(gameName) / gameToPrice.get(gameName);
            gameToScore.put(score, gameName);
        }

        return gameToScore;
    }

    @Annotations.FinalResult
    public List<String> getTopGames(@Annotations.DependsOn("Score-To-Game") SortedMap<Double, String> gameToScore) {
        return new ArrayList<>(gameToScore.values());
    }
}
