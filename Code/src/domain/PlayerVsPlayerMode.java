package domain;

import java.util.List;

public class PlayerVsPlayerMode implements GameMode {

	private final PlayerType typePy1;
	private final String player1Name;
	private final PlayerType typePy2;
	private final String player2Name;
	
	public PlayerVsPlayerMode(PlayerType typePy1, String namePy1, PlayerType typePy2, String namePy2) {
		this.typePy1 = typePy1;
		this.player1Name = namePy1;
		this.typePy2 = typePy2;
		this.player2Name = namePy2;
	}
	
	@Override
	public List<Player> createPlayers() throws HardestGameException {
		return List.of(new HumanPlayer(typePy1, player1Name), new HumanPlayer(typePy2, player2Name));
	}

	@Override
	public boolean isGameOver(List<Player> players, Level level) {
		return level.isTimeUp();
	}

	@Override
	public Player getWinner(List<Player> players) {
		Player py1 = players.get(0);
		Player py2 = players.get(1);
		int deathsPy1 = py1.getDeaths();
		int deathsPy2 = py2.getDeaths();
		if (deathsPy1 < deathsPy2) {
			return py1;
		}
		if (deathsPy1 > deathsPy2) {
			return py2;
		}
		if (deathsPy1 == deathsPy2) {
			int numCoinsPy1 = py1.getCollectedCoins();
			int numCoinsPy2 = py2.getCollectedCoins();
			if (numCoinsPy1 > numCoinsPy2) {
				return py1;
			}
			if (numCoinsPy2 < numCoinsPy2) {
				return py2;
			}
		}
		return null;
	}

}
