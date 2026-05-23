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
 * Level class, structured with the help of GPT 5.5 AI.
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

	/**
	 * Contructor of level
	 * @param definition definition is the definition of level, so set the components like
	 * enemies, coins, bombs, etc that going to have the level
	 */
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

	/**
	 * Set the builder of this level 
	 * @param mapNumber mapNumber is the map of tiles that going to have level
	 * @return A LevelBuilder that set the way that going to create the level
	 */
	public static LevelBuilder builder(int mapNumber) {
		return new LevelBuilder(mapNumber);
	}

	/**
	 * Create the specific level 
	 * @param levelNumber level number is the number of level that going to create
	 * @return Level all of entities, bombs and coins
	 * @throws HardestGameException 
	 */
	public static Level create(int levelNumber) throws HardestGameException {
		return LevelCatalog.create(levelNumber);
	}

	/**
	 * Initialize the components going to has the level
	 */
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

	/*
	 * Verify if every coins that players have collected are enough to pass the level
	 * @return true If every coin has been collected for players
	 * 			false otherwise
	 */
	private boolean allRequiredCoinsCollected() {
		int totalCoinsCollected = 0;
		for (Player player : players) {
			totalCoinsCollected += player.getCollectedCoins();
		}
		return totalCoinsCollected >= coinsRequired;
	}

	/*
	 * Verify id every player are in goal zone
	 * @return true if every players are in goal zone
	 */
	private boolean allPlayersInGoalZone() {
		for (Player player : players) {
			if (!isInsideGoalZone(player)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Verify if the player given is inside goal zone
	 * @param player player who going to verify if this is at the goal zone
	 * @return true If player is at the goal zone
	 */
	private boolean isInsideGoalZone(Player player) {
		for (GoalZone zone : getGoalZones()) {
			if (zone.contains(player.getPosX(), player.getPosY())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Spawn the players at the center of initial zone
	 * @param playersToSpawn playersToSpawn are players that going to spawn at the initial zone
	 */
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
	
	public List<GoalZone> getGoalZones() {
		return zones.stream()
				.filter(e -> e instanceof GoalZone)
				.map(e -> (GoalZone) e)
				.toList();
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

	/*
	 * Put the tiles that exists at the game in the elements of level 
	 */
	private void registerTiles() {
		for (Tile tile : map.getTiles()) {
			elements.put(nextElementId(), tile);
		}
	}

	/**
	 * Put players at the elements of level 
	 * @param players players is the list of players to put at the elements
	 */
	public void setPlayers(List<Player> players) {
		removeCurrentPlayersFromElements();
		this.players = players;
		for (Player player : players) {
			elements.put(nextElementId(), player);
		}
	}
	
	/**
	 * Insert a wall directly in the given pixel coordinates.
	 * @param posX horizontal pixel position.
	 * @param posY vertical pixel position.
	 */
	public void addWall(int posX, int posY) {
		elements.put(nextElementId(), new Wall(posX, posY));
	}
	
	/*
	 * 
	 */
	private void removeCurrentPlayersFromElements() {
		elements.entrySet().removeIf(entry -> entry.getValue() instanceof Player);
	}

	/*
	 * Remove a specific element of level's elements
	 * @param element element is element to found and pop to elements
	 */
	public void removeElement(Element element) {
		elements.entrySet().stream()
				.filter(entry -> entry.getValue().equals(element))
				.map(entry -> entry.getKey())
				.findFirst()
				.ifPresent(key -> elements.remove(key));
	}

	/**
	 * 
	 * @param checker
	 * @throws HardestGameException
	 */
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

	/**
	 * Verify if specific object is at a indicated zone 
	 * @param e e is element to verify are at the specific zone
	 * @param bx bx is the bound of x (most left) that element can be to is inside zone
	 * @param by by is the bound of y (most up) that element can be to is inside zone
	 * @param width width is the longitude of zone
	 * @param height height is the longitude of zone
	 * @return true If elements is inside bounds of zone
	 * 			false otherwise
	 */
	private boolean overlapsArea(Element e, float bx, float by, float width, float height) {
		return e.getPosX() < bx + width
				&& e.getPosX() + e.getWidth() > bx
				&& e.getPosY() < by + height
				&& e.getPosY() + e.getHeight() > by;
	}

	/**
	 * Verify if player has every coins of level
	 * @param player player that going to check If has every coins of level 
	 * @return true If players has all coins of level
	 * 			false otherwise
	 */
	public boolean playerHasAllCoins(Player player) {
		return player.getCollectedCoins() >= coinsRequired;
	}

	/**
	 * Make the funcionality of zones, make players a effect if player enters to some zone
	 */
	public void checkZones() {
		for (Player player : players) {
			for (Zone zone : zones) {
				if (zone.contains(player.getPosX(), player.getPosY())) {
					zone.whenPlayerEnter(player);
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

	/**
	 * Add a enemy to level
	 * @param movement movement that enemy going to have
	 * @param type tyoe of enemy thath going to create
	 */
	void addEnemy(List<Point> movement, String type) {
		Enemy enemy = new Enemy(movement);
		enemy.setStrategyMovement(EnemyMovementStrategyFactory.create(type, enemy));
		elements.put(nextElementId(), enemy);
	}

	/**
	 * Add a coin to level specific position
	 * @param row row That going to be the coin
	 * @param col col taht going to be the coin
	 */
	void addCoin(int row, int col) {
		int offset = DimensionGame.TILESIZE / 4;
		Coin coin = new Coin(col * DimensionGame.TILESIZEWIDTH + offset,
				row * DimensionGame.TILESIZEHEIGHT + offset);
		elements.put(nextElementId(), coin);
	}

	/**
	 * Add a bomb to level to specific position
	 * @param row row That going to be the bomb
	 * @param col col taht going to be the bomb
	 */
	void addBomb(int row, int col) {
		int offset = DimensionGame.TILESIZE / 4;
		Bomb bomb = new Bomb(col * DimensionGame.TILESIZEWIDTH + offset,
				row * DimensionGame.TILESIZEHEIGHT + offset);
		elements.put(nextElementId(), bomb);
	}

	/**
	 * Add a specific zone 
	 * @param figure figure is the shape of zone given for a list of points 
	 * @param type type is the type of figure that going to be added to level
	 */
	void addZone(List<Point> figure, String type) {
		zones.add(ZoneFactory.create(type, figure));
	}

	/*
	 * Give the next id of element that going to be inserted at elements
	 * @return Integer indicates the id of elements
	 */
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

	/**
	 * Reset the time of level
	 */
	public void resetTime() {
		timeRemaining = timeLimitSeconds;
	}
	
	/**
	 * Decrements the remaining time by the given delta, clamping the result to zero.
	 *
	 * @param delta The time elapsed since the last frame.
	 */
	public void tickTime(float delta) {
		timeRemaining = Math.max(0f, timeRemaining - delta);
	}

	/**
	 * Checks if the time has run out.
	 * @return true if no time remains, false otherwise.
	 */
	public boolean isTimeUp() {
		return timeRemaining <= 0f;
	}
}
