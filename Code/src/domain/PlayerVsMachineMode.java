package domain;

import java.util.List;

public class PlayerVsMachineMode implements GameMode{

	public PlayerVsMachineMode(PlayerType typePy1, String namePy1, PlayerType typePy2) throws HardestGameException {
		throw new HardestGameException(HardestGameException.WORKING_IN_FUNCTION);
	}

	@Override
	public List<Player> createPlayers() throws HardestGameException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isGameOver(List<Player> players, Level level) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Player getWinner(List<Player> players) {
		// TODO Auto-generated method stub
		return null;
	}
}
