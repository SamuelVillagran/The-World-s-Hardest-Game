package domain;

public class CoinSpawn implements LevelComponent {

	private int row;
	private int col;

	public CoinSpawn(int row, int col) {
		this.row = row;
		this.col = col;
	}

	@Override
	public void addTo(Level level) {
		level.addCoin(row, col);
	}
}
