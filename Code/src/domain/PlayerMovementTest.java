package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Veirfica el comportamiento del movimiento y los estados del jugador
 */
class PlayerMovementTest {

    //Movimiento basico

    @Test
    void redPlayerMovesRightWhenDirectionIsR() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        int initialX = player.getPosX();
        player.move('r');
        assertTrue(player.getPosX() > initialX);
    }

    @Test
    void redPlayerMovesLeftWhenDirectionIsL() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        int initialX = player.getPosX();
        player.move('l');
        assertTrue(player.getPosX() < initialX);
    }

    @Test
    void redPlayerMovesDownWhenDirectionIsD() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        int initialY = player.getPosY();
        player.move('d');
        assertTrue(player.getPosY() > initialY);
    }

    @Test
    void redPlayerMovesUpWhenDirectionIsU() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        int initialY = player.getPosY();
        player.move('u');
        assertTrue(player.getPosY() < initialY);
    }

    //Pruebas unitarias
    @Test
    void bluePlayerIsLargerThanRedPlayer() throws HardestGameException {
        Player red = new HumanPlayer(PlayerType.RED, "red");
        Player blue = new HumanPlayer(PlayerType.BLUE, "blue");
        assertTrue(blue.getWidth() > red.getWidth());
        assertTrue(blue.getHeight() > red.getHeight());
    }

    @Test
    void redPlayerStartsWithOneLife() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        assertEquals(1, player.getLifes());
    }

    @Test
    void greenPlayerStartsWithTwoLives() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.GREEN, "test");
        assertEquals(2, player.getLifes());
    }

    @Test
    void deadPlayerCannotMove() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.onEnemyContact();
        int x = player.getPosX();
        int y = player.getPosY();
        player.move('r');
        player.move('l');
        player.move('u');
        player.move('d');
        assertEquals(x, player.getPosX());
        assertEquals(y, player.getPosY());
    }

    @Test
    void deadPlayerReportsDeadState() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        assertFalse(player.isDead());
        player.onEnemyContact();
        assertTrue(player.isDead());
        assertEquals("deadstate", player.getNameState());
    }


    @Test
    void playerReturnsToRespawnPointAfterDeath() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.setRespawnPoint(200, 300);
        player.onEnemyContact();
        player.respawn();
        assertEquals(200, player.getPosX());
        assertEquals(300, player.getPosY());
    }

    @Test
    void playerRestoresLifeAfterRespawn() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        player.onEnemyContact();
        player.respawn();
        assertEquals(1, player.getLifes());
    }

    @Test
    void deathCounterIncrementsOnEachDeath() throws HardestGameException {
        Player player = new HumanPlayer(PlayerType.RED, "test");
        assertEquals(0, player.getDeaths());
        player.onEnemyContact();
        assertEquals(1, player.getDeaths());
        player.respawn();
        player.onEnemyContact();
        assertEquals(2, player.getDeaths());
    }
}
