package domain;

public class BombSpawn implements LevelComponent {

	private int row;
	private int col;

	public BombSpawn(int row, int col) {
		this.row = row;
		this.col = col;
	}

	@Override
	public void addTo(Level level) {
		level.addBomb(row, col);
	}
}
