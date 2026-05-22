package domain;

public final class LevelCatalog {

	public static final int DEFAULT_LEVEL_TIME_SECONDS = 90;
	private static final int TOTAL_LEVELS = 3;

	private LevelCatalog() {
	}

	public static Level create(int levelNumber) throws HardestGameException {
		return new Level(definition(levelNumber));
	}

	public static LevelDefinition definition(int levelNumber) throws HardestGameException {
		return switch (levelNumber) {
			case 1 -> levelOne();
			case 2 -> levelTwo();
			case 3 -> levelThree();
			default -> throw new HardestGameException("Nivel no existe");
		};
	}

	public static boolean hasLevel(int levelNumber) {
		return levelNumber >= 1 && levelNumber <= TOTAL_LEVELS;
	}

	public static int totalLevels() {
		return TOTAL_LEVELS;
	}

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
				.bomb(9, 26);

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

	private static void rectangleZone(LevelBuilder builder, String type, int topRow, int leftCol, int bottomRow,
			int rightCol) {
		builder.zone(type,
				LevelBuilder.tilePoint(topRow, leftCol),
				LevelBuilder.tilePoint(topRow, rightCol),
				LevelBuilder.tilePoint(bottomRow, rightCol),
				LevelBuilder.tilePoint(bottomRow, leftCol));
	}
}
