package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CoinInteractions {
	
	@Test
	void shouldRedCoinDoesNotChangeStateOfRedPlayer() throws HardestGameException {
	    Player player = new HumanPlayer(PlayerType.RED, "test");

	    Coin coin = new Coin();
	    RedCoin redCoin = new RedCoin(coin);
	    redCoin.onContactWithPlayer(player, null);
	    assertEquals("red", player.getNameState());
	}
	
	@Test
	void shouldGreenCoinChangesRedPlayerToGreenStateAndAddsLife() throws HardestGameException {
	    Player player = new HumanPlayer(PlayerType.RED, "test");
	    assertEquals(1, player.getLifes());
	    GreenCoin greenCoin = new GreenCoin(new Coin());
	    greenCoin.onContactWithPlayer(player, null);
	    assertEquals("green", player.getNameState());
	    assertEquals(2, player.getLifes());
	}
	
	@Test
	void shouldBlueCoinChangesRedPlayerToBlueState() throws HardestGameException {
	    Player player = new HumanPlayer(PlayerType.RED, "test");
	    float velocidadAntes = player.getSpeed();
	    BlueCoin blueCoin = (BlueCoin) CoinFactory.createSkin("blue", new Coin());
	    blueCoin.onContactWithPlayer(player, null);
	    assertEquals("blue", player.getNameState());
	    assertTrue(player.getSpeed() > velocidadAntes);
	}
	
	@Test
	void shouldRedCoinChangesGreenPlayerToRedState() throws HardestGameException { // Preuba apoyada de Chat-GPT
	    Player player = new HumanPlayer(PlayerType.GREEN, "test");
	    Level level = Level.builder(1).time(60).build();
	    level.initialize();
	    Coin coin = new Coin(75f, 75f, new RedCoin(new Coin()));
	    level.getElements().put(9999, coin);
	    level.setPlayers(new ArrayList<>(List.of(player)));
	    player.setPosition(75, 75);
	    new CollisionChecker().checkContactsWithInteractable(player, level, level);
	    assertEquals("red", player.getNameState());
	    assertEquals(1, player.getCollectedCoins());
	}
}
