package domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * LevelBuilder class, structured with the help of GPT 5.5 AI.
 * Uses the Builder pattern to build level.
 * This class create with mwthods objects going to be at level
 * Its state is mutable  
 */
public class LevelBuilder {

	private int mapNumber;
	private int coinsRequired;
	private int timeLimitSeconds;
	private List<LevelComponent> components;

	/**
	 * Coinstructo of LevelBuilder
	 * @param mapNumber mapNumber is the number of map that going to construct
	 */
	public LevelBuilder(int mapNumber) {
		this.mapNumber = mapNumber;
		this.timeLimitSeconds = LevelCatalog.DEFAULT_LEVEL_TIME_SECONDS;
		this.components = new ArrayList<>();
	}

	/**
	 * Set the coins that required to complete level
	 * @param coinsRequired coinsRequired are number of coins going to have to obtain the players to complete level
	 * @return this level builder
	 */
	public LevelBuilder coinsRequired(int coinsRequired) {
		this.coinsRequired = coinsRequired;
		return this;
	}

	/**
	 * Set the limit of time of level
	 * @param timeLimitSeconds timeLimitSeconds is the number of limit seconds taht have player to complete level
	 * @return this level builder
	 */
	public LevelBuilder timeLimitSeconds(int timeLimitSeconds) {
		this.timeLimitSeconds = timeLimitSeconds;
		return this; 
	}

	/**
	 * Time is the tima to set this level boilder
	 * @param seconds seconds are the int that has players to complete level 
	 * @return this level biulder
	 */
	public LevelBuilder time(int seconds) {
		return timeLimitSeconds(seconds);
	}

	/**
	 * This make a specific enemy 
	 * @param type type of enemy that going to create
	 * @param startRow startRow is the inital row that going to be enemy
	 * @param startCol startCol is the initial col that going to be enemy
	 * @param endRow endRow is the last row that going to be enemy
	 * @param endCol endCol is the last col that going to be enemy
	 * @return The specific enemy that wants to create
	 */
	public LevelBuilder enemy(String type, int startRow, int startCol, int endRow, int endCol) {
		return enemy(type, tilePoint(startRow, startCol), tilePoint(endRow, endCol));
	}

	/**
	 * This make a specific enemy but with a specific movement
	 * @param type type of enemy that going to create "vertical", "acelerate", "basic"
	 * @param movement movement is the points that going to move the enemy
	 * @return this level biulder
	 */
	public LevelBuilder enemy(String type, Point... movement) {// Los tres puntos significa Point[]
		components.add(new EnemySpawn(type, movement));
		return this;
	}

	/**
	 * This build a coin with skin
	 * @param row row where coin will be 
	 * @param col col where coin will be 
	 * @return this level builder
	 */
	public LevelBuilder coin(int row, int col, String type) {
		components.add(new CoinSpawn(row, col, type));
		return this;
	}

	/**
	 * This build a normal coin 
	 * @param row row where coin will be 
	 * @param col col where coin will be 
	 * @return this level builder
	 */
	public LevelBuilder coin(int row, int col) {
		components.add(new CoinSpawn(row, col));
		return this;
	}
	
	/**
	 * This build a bomb
	 * @param row row where bomb will be 
	 * @param col col where bomb will be 
	 * @return this level builder
	 */
	public LevelBuilder bomb(int row, int col) {
		components.add(new BombSpawn(row, col));
		return this;
	}

	/**
	 * This make a specific zone 
	 * @param type type of zone "checkpoint", "lifesource", "goal" or "initial"
	 * @param points points that constitude the zone (polygon)
	 * @return this level builder
	 */
	public LevelBuilder zone(String type, Point... points) {
		components.add(new ZoneSpawn(type, points));
		return this;
	}

	/**
	 * Give the definition og level of this level builder
	 * @return
	 */
	public LevelDefinition buildDefinition() {
		return new LevelDefinition(mapNumber, configuredCoinsRequired(), timeLimitSeconds, components);
	}

	/**
	 * Creates the level with the defition of level
	 * @return level with specific definition
	 */
	public Level build() {
		return new Level(buildDefinition());
	}

	/**
	 * Creates a new point 
	 * @param row row to set point
	 * @param col col to set point
	 * @return Point that wants create in a specific row and col
	 */
	static Point tilePoint(int row, int col) {
		return new Point(col * DimensionGame.TILESIZEWIDTH, row * DimensionGame.TILESIZEHEIGHT);
	}

	/**
	 * Count the coins that are at the level
	 * @return The number of coins that are at the level
	 */
	private int configuredCoinsRequired() {
		int coinCount = (int) components.stream()
				.filter(component -> component instanceof CoinSpawn)
				.count();
		if (coinsRequired > 0 && coinsRequired != coinCount) {
			throw new IllegalStateException("Las monedas requeridas deben coincidir con las monedas del nivel.");
		}
		return coinCount;
	}

	
}
