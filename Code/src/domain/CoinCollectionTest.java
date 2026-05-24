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
    public void shouldPlayerCoinCounterStartsAtZero() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        assertEquals(0, player.getCollectedCoins());
    }

    @Test
    public void shouldPlayerCollectsCoinWhenOverlapping() throws HardestGameException {
        Level level = buildLevelWithCoin();
        CollisionChecker checker = new CollisionChecker();

        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.setPosition(COIN_PX, COIN_PY); // misma posición que la moneda
        level.setPlayers(new ArrayList<>(List.of(player)));
        
        checker.checkContactsWithInteractable(player, level, level);

        assertEquals(1, player.getCollectedCoins());
    }

    @Test
    public void shouldCoinIsRemovedFromLevelAfterCollection() throws HardestGameException {
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
    public void shouldPlayerFarFromCoinDoesNotCollectIt() throws HardestGameException {
        Level level = buildLevelWithCoin();
        CollisionChecker checker = new CollisionChecker();

        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.setPosition(600, 600); // lejos de la moneda
        level.setPlayers(new ArrayList<>(List.of(player)));

        checker.checkContactsWithInteractable(player, level, level);

        assertEquals(0, player.getCollectedCoins());
    }

    @Test
    public void shouldEachCoinIncreasesCounterByOne() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.addCoin();
        player.addCoin();
        player.addCoin();
        assertEquals(3, player.getCollectedCoins());
    }

    @Test
    public void shouldLevelHasCorrectCoinsRequired() throws HardestGameException {
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

    //Verificación niveles:
    
    @Test
    public void levelOneLoadsCorrectEnemiesAndCoins() throws HardestGameException {
        Level level = LevelCatalog.create(1);
        level.initialize();
        
        var elements = level.getElements().values();
        long enemies = elements.stream().filter(e -> e instanceof Enemy).count();
        long coins   = elements.stream().filter(e -> e instanceof Coin).count();
        long bombs   = elements.stream().filter(e -> e instanceof Bomb).count();
        
        assertEquals(4, enemies);
        assertEquals(2, coins);
        assertEquals(0, bombs);
        assertEquals(90, level.getLevelTime());
    }
    
    @Test
    public void levelTwoLoadsEighteenEnemies() throws HardestGameException {
        Level level = LevelCatalog.create(2);
        level.initialize();
        
        var elements = level.getElements().values();
        long enemies = elements.stream().filter(e -> e instanceof Enemy).count();
        long coins   = elements.stream().filter(e -> e instanceof Coin).count();
        
        assertEquals(18, enemies);
        assertEquals(4, coins);
    }
    
    @Test
    public void levelThreeHasBombsAndSkinCoins() throws HardestGameException {
        Level level = LevelCatalog.create(3);
        level.initialize();
        
        var elements = level.getElements().values();
        long enemies = elements.stream().filter(e -> e instanceof Enemy).count();
        long bombs   = elements.stream().filter(e -> e instanceof Bomb).count();
        
        assertEquals(5, enemies);
        assertEquals(8, bombs);
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
