package domain;

public class CoinSpawn extends SuperObjectSpawn implements LevelComponent {

	private String type;
	
	public CoinSpawn(int row, int col, String type) {
		super(row, col);
		this.type = type;
	}

//	@Override
//	public void addTo(Level level, String type) {
//		level.addCoin(row, col);
//	}

	public CoinSpawn(int row, int col) {
		super(row, col);
	}

	@Override
	public void addTo(Level level) {
		if (type == null) {
			level.addCoin(row, col);
			return;
		}
		level.addCoin(row, col, type);
	}
}
