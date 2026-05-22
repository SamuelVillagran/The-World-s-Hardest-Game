package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class BombTest {

	private static final int FUSE_FRAMES = 300;
	private static final int RESPAWN_X = 100;
	private static final int RESPAWN_Y = 100;

	private static class TestLevel extends Level {
		@Override
		public void initialize() {
			putBomb(0, 0);
		}

		@Override
		public int getLevelTime() {
			return 10;
		}
	}

	private Player runBombTest(PlayerType type, int frames) throws HardestGameException {
		CollisionChecker checker = new CollisionChecker();
		TestLevel level = new TestLevel();
		level.initialize();

		Player player = new HumanPlayer(type, "test");
		player.setPosition(50, 9);
		player.setRespawnPoint(RESPAWN_X, RESPAWN_Y);
		level.setPlayers(new ArrayList<Player>(List.of(player)));

		for (int i = 0; i < frames; i++) {
			level.update(checker);
		}

		return player;
	}

	/**
	 * Verify if player dies and come back the respawn zone when this is red
	 * @throws HardestGameException
	 */
	@Test
	void redPlayerDiesAndRespawnsWhenBombExplodes() throws HardestGameException {
		Player player = runBombTest(PlayerType.RED, FUSE_FRAMES);

		assertEquals(1, player.getDeaths());
		assertEquals(RESPAWN_X, player.getPosX());
		assertEquals(RESPAWN_Y, player.getPosY());
		assertEquals(1, player.getLifes());
		assertEquals("red", player.getNameState());
	}

	/**
	 * Verify if player dies and come back the respawn zone when this is blue
	 * @throws HardestGameException
	 */
	@Test
	void bluePlayerDiesAndRespawnsWhenBombExplodes() throws HardestGameException {
		Player player = runBombTest(PlayerType.BLUE, FUSE_FRAMES);

		assertEquals(1, player.getDeaths());
		assertEquals(RESPAWN_X, player.getPosX());
		assertEquals(RESPAWN_Y, player.getPosY());
		assertEquals(1, player.getLifes());
		assertEquals("blue", player.getNameState());
	}

	/**
	 * Verify if player dies and come back the respawn zone when this is green
	 * @throws HardestGameException
	 */
	@Test
	void greenPlayerLosesOneLifeOnFirstBombExplosion() throws HardestGameException {
		Player player = runBombTest(PlayerType.GREEN, FUSE_FRAMES);

		assertEquals(0, player.getDeaths());
		assertEquals(50, player.getPosX());
		assertEquals(9, player.getPosY());
		assertEquals(1, player.getLifes());
		assertEquals("green", player.getNameState());
	}

	@Test
	void greenPlayerDiesAndRespawnsAfterSecondBombExplosion() throws HardestGameException {
		Player player = runBombTest(PlayerType.GREEN, FUSE_FRAMES * 2);

		assertEquals(1, player.getDeaths());
		assertEquals(RESPAWN_X, player.getPosX());
		assertEquals(RESPAWN_Y, player.getPosY());
		assertEquals(2, player.getLifes());
		assertEquals("green", player.getNameState());
	}
}
