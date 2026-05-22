package domain;

public class CoinSpawn extends SuperObjectSpawn implements LevelComponent {

	private int row;
	private int col;

	public CoinSpawn(int row, int col) {
		super(row, col);
	}

	@Override
	public void addTo(Level level) {
		level.addCoin(row, col);
	}
}
