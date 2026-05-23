package domain;

import java.util.List;

public interface GameMode {
	
	public List<Player> createPlayers() throws HardestGameException;
	public boolean isGameOver(List<Player> players, Level level);
	public Player getWinner(List<Player> players);
}
