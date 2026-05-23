package domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class CoinCollectionTest {
	//Datos generados con IA - Chat-GPT
    private static final int COIN_ROW = 1;
    private static final int COIN_COL = 1;
    private static final int COIN_PX  = COIN_COL * DimensionGame.TILESIZEWIDTH
            + DimensionGame.TILESIZE / 4; // = 45
    private static final int COIN_PY  = COIN_ROW * DimensionGame.TILESIZEHEIGHT
            + DimensionGame.TILESIZE / 4; // = 45


    @Test
    void playerCoinCounterStartsAtZero() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        assertEquals(0, player.getCollectedCoins());
    }

    @Test
    void playerCollectsCoinWhenOverlapping() throws HardestGameException {
        Level level = buildLevelWithCoin();
        CollisionChecker checker = new CollisionChecker();

        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.setPosition(COIN_PX, COIN_PY); // misma posición que la moneda
        level.setPlayers(new ArrayList<>(List.of(player)));
        
        checker.checkContactsWithInteractable(player, level, level);

        assertEquals(1, player.getCollectedCoins());
    }

    @Test
    void coinIsRemovedFromLevelAfterCollection() throws HardestGameException {
        Level level = buildLevelWithCoin();
        CollisionChecker checker = new CollisionChecker();

        long coinsBeforeCollection = countCoins(level);

        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.setPosition(COIN_PX, COIN_PY);
        level.setPlayers(new ArrayList<>(List.of(player)));

        checker.checkContactsWithInteractable(player, level, level);

        long coinsAfterCollection = countCoins(level);
        assertTrue(coinsAfterCollection < coinsBeforeCollection);
    }

    @Test
    void playerFarFromCoinDoesNotCollectIt() throws HardestGameException {
        Level level = buildLevelWithCoin();
        CollisionChecker checker = new CollisionChecker();

        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.setPosition(600, 600); // lejos de la moneda
        level.setPlayers(new ArrayList<>(List.of(player)));

        checker.checkContactsWithInteractable(player, level, level);

        assertEquals(0, player.getCollectedCoins());
    }

    @Test
    void eachCoinIncreasesCounterByOne() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.addCoin();
        player.addCoin();
        player.addCoin();
        assertEquals(3, player.getCollectedCoins());
    }

    @Test
    void levelHasCorrectCoinsRequired() throws HardestGameException {
        Level level = Level.builder(1)
                .coin(1, 1)
                .coin(2, 2)
                .build();
        level.initialize();

        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.addCoin(); // solo 1 de 2
        level.setPlayers(new ArrayList<>(List.of(player)));

        assertFalse(level.playerHasAllCoins(player));

        player.addCoin(); // ahora tiene 2
        assertTrue(level.playerHasAllCoins(player));
    }


    private long countCoins(Level level) {
        return level.getElements().values().stream()
                .filter(e -> e instanceof Coin)
                .count();
    }
    
    private Level buildLevelWithCoin() throws HardestGameException {
        Level level = Level.builder(1)
                .coin(COIN_ROW, COIN_COL)
                .build();
        level.initialize();
        return level;
    }

}
