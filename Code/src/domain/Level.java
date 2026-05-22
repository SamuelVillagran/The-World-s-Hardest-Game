package domain;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Level implements CollisionContext, Serializable {

	private int numCoin;
	private LinkedHashMap<Integer, Element> elements;
	private Map map;
	private CollisionChecker cChecker;
	private List<Player> players;
	private List<Zone> zones;
	private int levelTime;
	private List<LevelComponent> components;
	private boolean initialized;

	private Level(int mapNumber, int numCoin, int levelTime, CollisionChecker cChecker,
			List<LevelComponent> components) {
		this.numCoin = numCoin;
		this.levelTime = levelTime;
		this.cChecker = cChecker;
		this.components = new ArrayList<>(components);
		this.elements = new LinkedHashMap<>();
		this.players = new ArrayList<>();
		this.zones = new ArrayList<>();
		this.map = new Map(mapNumber);
		registerTiles();
	}

	public static Builder builder(int mapNumber, CollisionChecker cChecker) {
		return new Builder(mapNumber, cChecker);
	}

	public static Level create(int levelNumber, CollisionChecker cChecker) throws HardestGameException {
		switch (levelNumber) {
			case 1:
				return createLevelOne(cChecker);
			case 2:
				return createLevelTwo(cChecker);
			case 3:
				return createLevelThree(cChecker);
			default:
				throw new HardestGameException("Nivel no existe");
		}
	}

	private static Level createLevelOne(CollisionChecker cChecker) {
		Builder builder = builder(1, cChecker)
				.coinsRequired(2)
				.time(90)
				.enemy("basic", 8, 7, 8, 24)
				.enemy("basic", 9, 24, 9, 7)
				.enemy("basic", 10, 7, 10, 24)
				.enemy("basic", 11, 24, 11, 7)
				.coin(10, 9)
				.coin(23, 9);

		rectangleZone(builder, "initial", 6, 2, 13, 5);
		rectangleZone(builder, "goal", 6, 26, 13, 29);
		return builder.build();
	}

	private static Level createLevelTwo(CollisionChecker cChecker) {
		Builder builder = builder(2, cChecker)
				.coinsRequired(3)
				.time(80)
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
		return builder.build();
	}

	private static Level createLevelThree(CollisionChecker cChecker) {
		Builder builder = builder(3, cChecker)
				.coinsRequired(0)
				.time(90)
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
				tilePoint(3, 2),
				tilePoint(3, 4),
				tilePoint(4, 4),
				tilePoint(4, 2));
		builder.zone("goal",
				tilePoint(13, 25),
				tilePoint(7, 28),
				tilePoint(15, 28),
				tilePoint(15, 25));
		return builder.build();
	}

	private static void rectangleZone(Builder builder, String type, int topRow, int leftCol, int bottomRow,
			int rightCol) {
		builder.zone(type,
				tilePoint(topRow, leftCol),
				tilePoint(topRow, rightCol),
				tilePoint(bottomRow, rightCol),
				tilePoint(bottomRow, leftCol));
	}

	private static Point tilePoint(int row, int col) {
		return new Point(col * DimensionGame.TILESIZEWIDTH, row * DimensionGame.TILESIZEHEIGHT);
	}

	public void initialize() {
		if (initialized) {
			return;
		}
		for (LevelComponent component : components) {
			component.addTo(this);
		}
		initialized = true;
	}

	public HashMap<Integer, Element> getElements() {
		return elements;
	}

	/**
	 * Check if the level has all its coins collected by players.
	 * @return true if there are no pending coins and all players reached the goal.
	 */
	public boolean isCompleted() {
		int totalCoinsCollected = 0;
		for (Player player : players) {
			totalCoinsCollected += player.getCollectedCoins();
		}
		if (totalCoinsCollected < numCoin) {
			return false;
		}
		for (Player player : players) {
			if (!player.hasGoalCompleted()) {
				return false;
			}
		}
		return true;
	}

	public void spawnPlayers(List<Player> pys) {
		Zone zone = getInitialZone();
		if (zone == null) {
			return;
		}
		for (Player player : players) {
			player.setPosition(zone.getSpawnX(), zone.getSpawnY());
			player.setRespawnPoint(zone.getSpawnX(), zone.getSpawnY());
		}
	}

	public HashMap<String, String> getElementsToDraw() {
		HashMap<String, String> pathsElements = new HashMap<>();
		for (Element e : elements.values()) {
			String nameClass = e.getNameClass();
			if (!pathsElements.containsKey(nameClass)) {
				pathsElements.put(nameClass, e.getPathImage());
			}
		}
		return pathsElements;
	}

	public int[][] getMapTileNum() {
		return map.getMapTileNum();
	}

	private void registerTiles() {
		for (Tile tile : map.getTiles()) {
			elements.put(elements.size() + 1, tile);
		}
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
		for (Player py : players) {
			elements.put(elements.size() + 1, py);
		}
	}

	public void removeElement(Element element) {
		elements.entrySet().stream()
				.filter(entry -> entry.getValue().equals(element))
				.map(entry -> entry.getKey())
				.findFirst()
				.ifPresent(key -> elements.remove(key));
	}

	public void update(CollisionChecker checker) throws HardestGameException {
		for (Enemy am : getEnemies()) {
			am.move(checker, this);
		}

		for (Bomb bomb : getBombs()) {
			bomb.action();
			bomb.explodeIfPending(this);
		}

		for (Player player : players) {
			checker.checkContactsWithInteractable(player, this, this);
			if (player.isDead()) {
				player.respawn();
			}
		}

		checkZones();
	}

	protected List<Enemy> getEnemies() {
		return elements.values().stream()
				.filter(e -> e instanceof Enemy)
				.map(e -> (Enemy) e)
				.toList();
	}

	private List<Bomb> getBombs() {
		return elements.values().stream()
				.filter(e -> e instanceof Bomb)
				.map(e -> (Bomb) e)
				.toList();
	}

	public List<Damageable> getDamageablesInArea(int x, int y, float width, float height) {
		List<Damageable> targets = elements.values().stream()
				.filter(e -> e instanceof Damageable)
				.filter(e -> overlapsArea((Element) e, x, y, width, height))
				.map(e -> (Damageable) e)
				.collect(Collectors.toCollection(ArrayList::new));

		for (Player player : players) {
			if (!targets.contains(player) && overlapsArea(player, x, y, width, height)) {
				targets.add(player);
			}
		}
		return targets;
	}

	private boolean overlapsArea(Element e, int x, int y, float width, float height) {
		return e.getPosX() < x + width
				&& e.getPosX() + e.getWidth() > x
				&& e.getPosY() < y + height
				&& e.getPosY() + e.getHeight() > y;
	}

	public boolean playerHasAllCoins(Player player) {
		return player.getCollectedCoins() >= numCoin;
	}

	public void checkZones() {
		for (Player player : players) {
			for (Zone zone : zones) {
				if (zone.contains(player.getPosX(), player.getPosY())) {
					zone.whenPlayerEnter(player, this);
				}
			}
		}
	}

	public List<AutomaticMovement> getElementsAutomaticMovement() {
		return elements.values().stream()
				.filter(e -> e instanceof AutomaticMovement)
				.map(e -> (AutomaticMovement) e)
				.toList();
	}

	private void putEnemy(List<Point> movement, String type) {
		Enemy enemy = new Enemy(movement);
		int unIdAlto = elements.size() + 1000;
		switch (type) {
			case "basic" -> enemy.setStrategyMovement(new Basic(enemy));
			case "vertical" -> enemy.setStrategyMovement(new Vertical(enemy));
			case "acelerate" -> enemy.setStrategyMovement(new Acelerate(enemy));
		}
		elements.put(unIdAlto, enemy);
	}

	private void putCoin(int row, int col) {
		int desface = DimensionGame.TILESIZE / 4;
		Coin coin = new Coin(col * DimensionGame.TILESIZEHEIGHT + desface,
				row * DimensionGame.TILESIZEWIDTH + desface);
		elements.put(elements.size() + 1, coin);
	}

	private void putZone(List<Point> figure, String type) {
		switch (type) {
			case "goal" -> zones.add(new GoalZone(new Figure(figure)));
			case "initial" -> zones.add(new InitialZone(new Figure(figure)));
		}
	}

	public List<Solid> getSolidElements() {
		return elements.values().stream()
				.filter(e -> e instanceof Solid)
				.map(e -> (Solid) e)
				.collect(Collectors.toList());
	}

	public List<Interactable> getInteractableElements() {
		return elements.values().stream()
				.filter(e -> e instanceof Interactable)
				.map(e -> (Interactable) e)
				.collect(Collectors.toList());
	}

	public Zone getInitialZone() {
		for (Zone zone : zones) {
			if (zone instanceof InitialZone) {
				return zone;
			}
		}
		return null;
	}

	private void putBomb(int row, int col) {
		int desface = DimensionGame.TILESIZE / 4;
		Bomb bomb = new Bomb(col * DimensionGame.TILESIZEWIDTH + desface,
				row * DimensionGame.TILESIZEHEIGHT + desface);
		elements.put(elements.size() + 1, bomb);
	}

	public int getLevelTime() {
		return levelTime;
	}

	public static class Builder {

		private int mapNumber;
		private CollisionChecker cChecker;
		private int numCoin;
		private int levelTime;
		private List<LevelComponent> components;

		private Builder(int mapNumber, CollisionChecker cChecker) {
			this.mapNumber = mapNumber;
			this.cChecker = cChecker;
			this.levelTime = 90;
			this.components = new ArrayList<>();
		}

		public Builder coinsRequired(int numCoin) {
			this.numCoin = numCoin;
			return this;
		}

		public Builder time(int seconds) {
			this.levelTime = seconds;
			return this;
		}

		public Builder enemy(String type, int startRow, int startCol, int endRow, int endCol) {
			return enemy(type, tilePoint(startRow, startCol), tilePoint(endRow, endCol));
		}

		public Builder enemy(String type, Point... movement) {
			components.add(new EnemyComponent(type, movement));
			return this;
		}

		public Builder coin(int row, int col) {
			components.add(new CoinComponent(row, col));
			return this;
		}

		public Builder bomb(int row, int col) {
			components.add(new BombComponent(row, col));
			return this;
		}

		public Builder zone(String type, Point... points) {
			components.add(new ZoneComponent(type, points));
			return this;
		}

		public Level build() {
			return new Level(mapNumber, numCoin, levelTime, cChecker, components);
		}
	}

	private interface LevelComponent extends Serializable {
		void addTo(Level level);
	}

	private static class EnemyComponent implements LevelComponent {
		private String type;
		private List<Point> movement;

		private EnemyComponent(String type, Point... movement) {
			this.type = type;
			this.movement = new ArrayList<>(List.of(movement));
		}

		@Override
		public void addTo(Level level) {
			level.putEnemy(new ArrayList<>(movement), type);
		}
	}

	private static class CoinComponent implements LevelComponent {
		private int row;
		private int col;

		private CoinComponent(int row, int col) {
			this.row = row;
			this.col = col;
		}

		@Override
		public void addTo(Level level) {
			level.putCoin(row, col);
		}
	}

	private static class BombComponent implements LevelComponent {
		private int row;
		private int col;

		private BombComponent(int row, int col) {
			this.row = row;
			this.col = col;
		}

		@Override
		public void addTo(Level level) {
			level.putBomb(row, col);
		}
	}

	private static class ZoneComponent implements LevelComponent {
		private String type;
		private List<Point> points;

		private ZoneComponent(String type, Point... points) {
			this.type = type;
			this.points = new ArrayList<>(List.of(points));
		}

		@Override
		public void addTo(Level level) {
			level.putZone(new ArrayList<>(points), type);
		}
	}
}
