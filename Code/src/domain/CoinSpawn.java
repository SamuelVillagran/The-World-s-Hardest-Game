package domain;

public class CoinSpawn extends SuperObjectSpawn implements LevelComponent {

	public CoinSpawn(int row, int col) {
		super(row, col);
	}

	@Override
	public void addTo(Level level) {
		level.addCoin(row, col);
	}
}
