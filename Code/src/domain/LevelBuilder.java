package domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class LevelBuilder {

	private int mapNumber;
	private int coinsRequired;
	private int timeLimitSeconds;
	private List<LevelComponent> components;

	public LevelBuilder(int mapNumber) {
		this.mapNumber = mapNumber;
		this.timeLimitSeconds = LevelCatalog.DEFAULT_LEVEL_TIME_SECONDS;
		this.components = new ArrayList<>();
	}

	public LevelBuilder coinsRequired(int coinsRequired) {
		this.coinsRequired = coinsRequired;
		return this;
	}

	public LevelBuilder timeLimitSeconds(int timeLimitSeconds) {
		this.timeLimitSeconds = timeLimitSeconds;
		return this;
	}

	public LevelBuilder time(int seconds) {
		return timeLimitSeconds(seconds);
	}

	public LevelBuilder enemy(String type, int startRow, int startCol, int endRow, int endCol) {
		return enemy(type, tilePoint(startRow, startCol), tilePoint(endRow, endCol));
	}

	public LevelBuilder enemy(String type, Point... movement) {
		components.add(new EnemySpawn(type, movement));
		return this;
	}

	public LevelBuilder coin(int row, int col) {
		components.add(new CoinSpawn(row, col));
		return this;
	}

	public LevelBuilder bomb(int row, int col) {
		components.add(new BombSpawn(row, col));
		return this;
	}

	public LevelBuilder zone(String type, Point... points) {
		components.add(new ZoneSpawn(type, points));
		return this;
	}

	public LevelDefinition buildDefinition() {
		return new LevelDefinition(mapNumber, configuredCoinsRequired(), timeLimitSeconds, components);
	}

	public Level build() {
		return new Level(buildDefinition());
	}

	static Point tilePoint(int row, int col) {
		return new Point(col * DimensionGame.TILESIZEWIDTH, row * DimensionGame.TILESIZEHEIGHT);
	}

	private int configuredCoinsRequired() {
		long coinCount = components.stream()
				.filter(component -> component instanceof CoinSpawn)
				.count();
		if (coinsRequired > 0 && coinsRequired != coinCount) {
			throw new IllegalStateException("Las monedas requeridas deben coincidir con las monedas del nivel.");
		}
		return (int) coinCount;
	}
}
