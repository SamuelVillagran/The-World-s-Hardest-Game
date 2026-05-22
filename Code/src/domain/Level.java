package domain;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Runtime instance of a level.
 *
 * Level does not know how Level 1, 2 or 3 are configured. It only owns the
 * map, entities, zones, players and timer for the active level definition.
 */
public class Level implements CollisionContext, Serializable {

	private int coinsRequired;
	private LinkedHashMap<Integer, Element> elements;
	private Map map;
	private List<Player> players;
	private List<Zone> zones;
	private int timeLimitSeconds;
	private float timeRemaining;
	private List<LevelComponent> components;
	private boolean initialized;

	public Level(LevelDefinition definition) {
		this.coinsRequired = definition.coinsRequired();
		this.timeLimitSeconds = definition.timeLimitSeconds();
		this.timeRemaining = timeLimitSeconds;
		this.components = new ArrayList<>(definition.components());
		this.elements = new LinkedHashMap<>();
		this.players = new ArrayList<>();
		this.zones = new ArrayList<>();
		this.map = new Map(definition.mapNumber());
		registerTiles();
	}

	public static LevelBuilder builder(int mapNumber, CollisionChecker cChecker) {
		return new LevelBuilder(mapNumber);
	}

	public static Level create(int levelNumber, CollisionChecker cChecker) throws HardestGameException {
		return LevelCatalog.create(levelNumber);
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
	 * Checks if every required coin was collected and every player reached the goal.
	 */
	public boolean isCompleted() {
		return allRequiredCoinsCollected() && allPlayersInGoalZone();
	}

	private boolean allRequiredCoinsCollected() {
		int totalCoinsCollected = 0;
		for (Player player : players) {
			totalCoinsCollected += player.getCollectedCoins();
		}
		return totalCoinsCollected >= coinsRequired;
	}

	private boolean allPlayersInGoalZone() {
		for (Player player : players) {
			if (!isInsideGoalZone(player)) {
				return false;
			}
		}
		return true;
	}

	private boolean isInsideGoalZone(Player player) {
		for (Zone zone : zones) {
			if (zone instanceof GoalZone && zone.contains(player.getPosX(), player.getPosY())) {
				return true;
			}
		}
		return false;
	}

	public void spawnPlayers(List<Player> playersToSpawn) {
		Zone zone = getInitialZone();
		if (zone == null) {
			return;
		}
		for (Player player : playersToSpawn) {
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
			elements.put(nextElementId(), tile);
		}
	}

	public void setPlayers(List<Player> players) {
		removeCurrentPlayersFromElements();
		this.players = players;
		for (Player player : players) {
			elements.put(nextElementId(), player);
		}
	}

	private void removeCurrentPlayersFromElements() {
		elements.entrySet().removeIf(entry -> entry.getValue() instanceof Player);
	}

	public void removeElement(Element element) {
		elements.entrySet().stream()
				.filter(entry -> entry.getValue().equals(element))
				.map(entry -> entry.getKey())
				.findFirst()
				.ifPresent(key -> elements.remove(key));
	}

	public void update(CollisionChecker checker) throws HardestGameException {
		for (Enemy enemy : getEnemies()) {
			enemy.move(checker, this);
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

	public List<Damageable> getDamageablesInArea(float bx, float by, float width, float height) {
		List<Damageable> targets = elements.values().stream()
				.filter(e -> e instanceof Damageable)
				.filter(e -> overlapsArea(e, bx, by, width, height))
				.map(e -> (Damageable) e)
				.collect(Collectors.toCollection(ArrayList::new));

		for (Player player : players) {
			if (!targets.contains(player) && overlapsArea(player, bx, by, width, height)) {
				targets.add(player);
			}
		}
		return targets;
	}

	private boolean overlapsArea(Element e, float bx, float by, float width, float height) {
		return e.getPosX() < bx + width
				&& e.getPosX() + e.getWidth() > bx
				&& e.getPosY() < by + height
				&& e.getPosY() + e.getHeight() > by;
	}

	public boolean playerHasAllCoins(Player player) {
		return player.getCollectedCoins() >= coinsRequired;
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

	void addEnemy(List<Point> movement, String type) {
		Enemy enemy = new Enemy(movement);
		enemy.setStrategyMovement(EnemyMovementStrategyFactory.create(type, enemy));
		elements.put(nextElementId(), enemy);
	}

	void addCoin(int row, int col) {
		int offset = DimensionGame.TILESIZE / 4;
		Coin coin = new Coin(col * DimensionGame.TILESIZEHEIGHT + offset,
				row * DimensionGame.TILESIZEWIDTH + offset);
		elements.put(nextElementId(), coin);
	}

	void addBomb(int row, int col) {
		int offset = DimensionGame.TILESIZE / 4;
		Bomb bomb = new Bomb(col * DimensionGame.TILESIZEWIDTH + offset,
				row * DimensionGame.TILESIZEHEIGHT + offset);
		elements.put(nextElementId(), bomb);
	}

	void addZone(List<Point> figure, String type) {
		zones.add(ZoneFactory.create(type, figure));
	}

	private int nextElementId() {
		return elements.size() + 1;
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

	public int getLevelTime() {
		return timeLimitSeconds;
	}

	public float getTimeRemaining() {
		return timeRemaining;
	}

	public void resetTime() {
		timeRemaining = timeLimitSeconds;
	}

	public void tickTime(float delta) {
		timeRemaining = Math.max(0f, timeRemaining - delta);
	}

	public boolean isTimeUp() {
		return timeRemaining <= 0f;
	}
}
