package domain;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Verifica las condiciones de finalización de un nivel
 * Un nivel se completa cuando:
 * 	La suma de monedas recogidas por todos los jugadores sea >= coinsRequired
 *  Todos los jugadores están dentro de una GoalZone
 */
class LevelCompletionTest {

    private static final int INSIDE_GOAL_X = 200;
    private static final int INSIDE_GOAL_Y = 200;

    private static final int OUTSIDE_GOAL_X = 700;
    private static final int OUTSIDE_GOAL_Y = 700;

    @Test
    void levelIsNotCompletedInitially() throws HardestGameException {
        Level level = buildLevelWithGoalOnly();
        level.initialize();

        Player player = redPlayerAt(OUTSIDE_GOAL_X, OUTSIDE_GOAL_Y);
        level.setPlayers(new ArrayList<>(List.of(player)));

        assertFalse(level.isCompleted());
    }

    @Test
    void levelNotCompletedWhenPlayerOutsideGoalZone() throws HardestGameException {
        Level level = buildLevelWithGoalOnly();
        level.initialize();

        Player player = redPlayerAt(OUTSIDE_GOAL_X, OUTSIDE_GOAL_Y);
        level.setPlayers(new ArrayList<>(List.of(player)));

        assertFalse(level.isCompleted());
    }

    @Test
    void levelNotCompletedWhenPlayerInGoalButMissingCoins() throws HardestGameException {
        Level level = buildLevelWithCoinAndGoal();
        level.initialize();

        Player player = redPlayerAt(INSIDE_GOAL_X, INSIDE_GOAL_Y);
        level.setPlayers(new ArrayList<>(List.of(player)));

        assertFalse(level.isCompleted());
    }

    @Test
    void levelNotCompletedWhenCoinsCollectedButPlayerNotInGoal() throws HardestGameException {
        Level level = buildLevelWithCoinAndGoal();
        level.initialize();

        Player player = redPlayerAt(OUTSIDE_GOAL_X, OUTSIDE_GOAL_Y);
        player.addCoin();
        level.setPlayers(new ArrayList<>(List.of(player)));

        assertFalse(level.isCompleted());
    }

    @Test
    void levelCompletedWhenAllCoinsCollectedAndPlayerInGoal() throws HardestGameException {
        Level level = buildLevelWithCoinAndGoal();
        level.initialize();

        Player player = redPlayerAt(INSIDE_GOAL_X, INSIDE_GOAL_Y);
        player.addCoin();
        level.setPlayers(new ArrayList<>(List.of(player)));

        assertTrue(level.isCompleted());
    }

    @Test
    void levelWithZeroCoinsRequiredCompletesWhenPlayerInGoal() throws HardestGameException {
        Level level = buildLevelWithGoalOnly();
        level.initialize();

        Player player = redPlayerAt(INSIDE_GOAL_X, INSIDE_GOAL_Y);
        level.setPlayers(new ArrayList<>(List.of(player)));

        assertTrue(level.isCompleted());
    }

    @Test
    void playerHasAllCoinsReturnsFalseWhenNotEnough() throws HardestGameException {
        Level level = buildLevelWithCoinAndGoal();
        level.initialize();

        Player player = redPlayerAt(0, 0);
        level.setPlayers(new ArrayList<>(List.of(player)));

        assertFalse(level.playerHasAllCoins(player));
    }

    @Test
    void playerHasAllCoinsReturnsTrueWhenEnough() throws HardestGameException {
        Level level = buildLevelWithCoinAndGoal();
        level.initialize();

        Player player = redPlayerAt(0, 0);
        player.addCoin();
        level.setPlayers(new ArrayList<>(List.of(player)));

        assertTrue(level.playerHasAllCoins(player));
    }
    
    private Level buildLevelWithGoalOnly() {
        return Level.builder(1)
                .zone("goal",
                        new Point(1, 1),
                        new Point(400, 1),
                        new Point(400, 400),
                        new Point(1, 400))
                .build();
    }

    private Level buildLevelWithCoinAndGoal() {
        return Level.builder(1)
                .coin(1, 1)
                .zone("goal",
                        new Point(1, 1),
                        new Point(400, 1),
                        new Point(400, 400),
                        new Point(1, 400))
                .build();
    }

    private Player redPlayerAt(int x, int y) throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.setPosition(x, y);
        return player;
    }
}
