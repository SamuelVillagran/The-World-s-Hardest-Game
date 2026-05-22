package domain;

import java.io.Serializable;
import java.util.List;

public class ModePlayer implements GameMode, Serializable {
	private final PlayerType type;
	private final String playerName;
	
	public ModePlayer(PlayerType type, String name) {
		this.type = type;
		this.playerName = name;
	}

	@Override
	public List<Player> createPlayers() throws HardestGameException {
		return List.of(new HumanPlayer(type, playerName));
	}

	@Override
	public boolean isGameOver(List<Player> players, Level level) {
		return level.isTimeUp();
	}

	@Override
	public Player getWinner(List<Player> players) {
		return players.get(0).hasGoalCompleted() ? players.get(0) : null;
	}
}
