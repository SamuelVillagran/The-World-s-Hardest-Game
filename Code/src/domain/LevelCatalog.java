package domain;

/**
 * Level catalog class, structured with the help of GPT 5.5 AI.
 * Uses the Composite pattern to place level components using the spawn classes.
 */
public final class LevelCatalog {

	public static final int DEFAULT_LEVEL_TIME_SECONDS = 90;
	private static final int TOTAL_LEVELS = 3;

	
	/*
	 * Private constructor to prevent instantiation of this catalog class.
	 */
	private LevelCatalog() {
	}
	
	/**
	 * Creates a level instance with all its interactive objects.
	 * Uses the Factory Method pattern internally to build the level from its catalog definition.
	 * @param levelNumber the number of the level to create
	 * @return a fully constructed Level instance
	 * @throws HardestGameException if the level number does not exist
	 */
	public static Level create(int levelNumber) throws HardestGameException {
		return new Level(definition(levelNumber));
	}

	/**
	 * Defines which level will be created based on the given number.
	 * @param levelNumber the number of the level to define
	 * @return the level definition containing all the elements for that level
	 * @throws HardestGameException if the level number does not exist
	 */
	public static LevelDefinition definition(int levelNumber) throws HardestGameException {
		return switch (levelNumber) {
			case 1 -> levelOne();
			case 2 -> levelTwo();
			case 3 -> levelThree();
			default -> throw new HardestGameException("Nivel no existe");
		};
	}

	/**
	 * Give if exists the level
	 * @param levelNumber levelNumber is the number to verify if exists at the game
	 * @return true if level number is between range of level created 
	 * 			false otherwise
	 */
	public static boolean hasLevel(int levelNumber) {
		return levelNumber >= 1 && levelNumber <= TOTAL_LEVELS;
	}

	/**
	 * Give number total of levels created
	 */
	public static int totalLevels() {
		return TOTAL_LEVELS;
	}

	/**
	 * Defines the contents of level one.
	 * @return the complete definition of level one
	 */
	private static LevelDefinition levelOne() {
		LevelBuilder builder = new LevelBuilder(1)
				.coinsRequired(2)
				.enemy("basic", 8, 7, 8, 24)
				.enemy("basic", 9, 24, 9, 7)
				.enemy("basic", 10, 7, 10, 24)
				.enemy("basic", 11, 24, 11, 7)
				.coin(9, 10)
				.coin(9, 23);

		rectangleZone(builder, "initial", 6, 2, 13, 5);
		rectangleZone(builder, "goal", 6, 26, 13, 29);
		return builder.buildDefinition();
	}

	/**
	 * Defines the contents of level two.
	 * @return the complete definition of level two
	 */
	private static LevelDefinition levelTwo() {
		LevelBuilder builder = new LevelBuilder(2)
				.coinsRequired(4)
				.coin(4, 6)
				.coin(13, 6)
				.coin(4, 23)
				.coin(13, 23);

		int numEnemies = 18;
		int startRowDown = 4;
		int startRowUp = 13;
		int startCol = 6;
		for (int i = 0; i < numEnemies; i++) {
			int col = startCol + i;
			if (i % 2 == 0) {
				builder.enemy("vertical", startRowDown, col, startRowUp, col);
			} else {
				builder.enemy("vertical", startRowUp, col, startRowDown, col);
			}
		}

		rectangleZone(builder, "initial", 7, 2, 13, 5);
		rectangleZone(builder, "goal", 7, 26, 13, 29);
		return builder.buildDefinition();
	}

	/**
	 * Defines the contents of level three.
	 * @return the complete definition of level three
	 */
	private static LevelDefinition levelThree() {
		LevelBuilder builder = new LevelBuilder(3)
				.coinsRequired(0)
				.enemy("acelerate", 2, 6, 15, 6)
				.enemy("acelerate", 2, 10, 15, 10)
				.enemy("acelerate", 2, 12, 15, 12)
				.enemy("acelerate", 15, 11, 3, 11)
				.enemy("acelerate", 15, 13, 3, 13)
				.bomb(7, 3)
				.bomb(3, 9)
				.bomb(14, 14)
				.bomb(14, 16)
				.bomb(10, 17)
				.bomb(6, 17)
				.bomb(5, 23)
				.bomb(9, 26)
				.coin(14, 3, "red")
				.coin(14, 7, "blue")
				.coin(3, 16, "green");

		builder.zone("initial",
				LevelBuilder.tilePoint(3, 2),
				LevelBuilder.tilePoint(3, 4),
				LevelBuilder.tilePoint(4, 4),
				LevelBuilder.tilePoint(4, 2));
		builder.zone("goal",
				LevelBuilder.tilePoint(13, 25),
				LevelBuilder.tilePoint(7, 28),
				LevelBuilder.tilePoint(15, 28),
				LevelBuilder.tilePoint(15, 25));
		return builder.buildDefinition();
	}

	/*
	 * Builds a rectangular zone of the given type using four corner tile points.
	 * @param builder the LevelBuilder instance used to construct the level
	 * @param type the type of zone to create (e.g. "initial" or "goal")
	 * @param topRow the topmost row of the zone
	 * @param leftCol the leftmost column of the zone
	 * @param bottomRow the bottommost row of the zone
	 * @param rightCol the rightmost column of the zone
	 */
	private static void rectangleZone(LevelBuilder builder, String type, int topRow, int leftCol, int bottomRow,
			int rightCol) {
		builder.zone(type,
				LevelBuilder.tilePoint(topRow, leftCol),
				LevelBuilder.tilePoint(topRow, rightCol),
				LevelBuilder.tilePoint(bottomRow, rightCol),
				LevelBuilder.tilePoint(bottomRow, leftCol));
	}
}
