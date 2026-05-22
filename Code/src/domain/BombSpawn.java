package domain;

public class BombSpawn extends SuperObjectSpawn implements LevelComponent {


	public BombSpawn(int row, int col) {
		super(row, col);
	}

	@Override
	public void addTo(Level level) {
		level.addBomb(row, col);
	}
}
