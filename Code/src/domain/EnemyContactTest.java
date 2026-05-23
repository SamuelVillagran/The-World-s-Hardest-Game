package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class EnemyContactTest {

    private static final int FRAMES = 11;

    // Jugador Red

    @Test
    void redPlayerDiesOnFirstEnemyContact() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.onEnemyContact();
        assertTrue(player.isDead());
    }

    @Test
    void redPlayerEntersDeadStateOnContact() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.onEnemyContact();
        assertEquals("deadstate", player.getNameState());
    }

    @Test
    void redPlayerDeathCounterIncreasesOnEnemyContact() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        assertEquals(0, player.getDeaths(), "No debe haber muertes al inicio");
        player.onEnemyContact();
        assertEquals(1, player.getDeaths());
    }

    // Jugador Blue

    @Test
    void bluePlayerDiesOnFirstEnemyContact() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.BLUE, "test");
        player.onEnemyContact();
        assertTrue(player.isDead());
    }

    @Test
    void bluePlayerEntersDeadStateOnContact() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.BLUE, "test");
        player.onEnemyContact();
        assertEquals("deadstate", player.getNameState());
    }


    @Test
    void greenPlayerEntersSlowedStateAfterSufficientFrames() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.GREEN, "test");
        for (int i = 0; i < FRAMES; i++) {
            player.onEnemyContact();
        }
        assertFalse(player.isDead());
        assertEquals("slowedstate", player.getNameState());
        assertEquals(1, player.getLifes());
    }

    @Test
    void greenPlayerIsSlowerAfterFirstContact() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.GREEN, "test");
        float normalSpeed = player.getSpeed();

        for (int i = 0; i < FRAMES; i++) {
            player.onEnemyContact();
        }

        assertTrue(player.getSpeed() < normalSpeed);
    }

    //Jugador Green con contacto prolongados
    @Test
    void greenPlayerDiesAfterContactConsecutives() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.GREEN, "test");
        for (int i = 0; i < 2* FRAMES; i++) {
            player.onEnemyContact();
        }

        assertTrue(player.isDead());
        assertEquals("deadstate", player.getNameState());
    }

    @Test
    void greenPlayerDeathCountsAsOneDeath() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.GREEN, "test");

        for (int i = 0; i < FRAMES * 2; i++) {
            player.onEnemyContact();
        }

        assertEquals(1, player.getDeaths());
    }

    @Test
    void deadPlayerHasZeroSpeedMultiplier() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.onEnemyContact();
        assertEquals(0.0f, player.getSpeed(), 0.001f);
    }

    @Test
    void deadPlayerStaysDeadOnFurtherEnemyContact() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.onEnemyContact();
        player.onEnemyContact();
        assertTrue(player.isDead());
        assertEquals(1, player.getDeaths());
    }
}
