package domain;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Level implements CollisionContext, Serializable {
	protected static int numCoin;
	protected LinkedHashMap<Integer, Element> elements;
	protected static Map map;
	protected CollisionChecker cChecker;
	protected List<Player> players;
	protected List<Zone> zones;
	
	/* 
	 * elements = new HashMap<>();
		
		elements.put(elements.size()+1, new Obstacle());
		elements.put(elements.size()+1, new Coin());
		elements.put(elements.size()+1, new Floor());
		elements.put(0, new Player());
	 * 
	 */
	public Level() {
		elements = new LinkedHashMap<>();
		players = new ArrayList<>();
		zones = new ArrayList<>();
	}

	public abstract void initialize();

	public HashMap<Integer, Element> getElements() {
		return elements;
	}

	/**
	 * Check if the level has all its coins collected by players.
	 * @return true if there's no coins otherwise false.
	 */
	public  boolean isCompleted() {
		int totalCoinsCollected = 0;
		for(Player player : players) {
			totalCoinsCollected += player.getCollectedCoins();			
		}
		if(totalCoinsCollected < numCoin) {
			return false;
		}
		for(Player player : players) {
			if(!player.hasGoalCompleted()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Put at the correct position the players of level1
	 * @param pys pys are the list of players that are at the level
	 */
	
	public void spawnPlayers(List<Player> pys) {	
		Zone zone = getInitialZone();
	    for (Player player : players) {
	        player.setPosition(zone.getSpawnX(), zone.getSpawnY());
	        player.setRespawnPoint(zone.getSpawnX(), zone.getSpawnY());
	    }
	}

	public HashMap<String, String> getElementsToDraw() {
		HashMap<String, String> pathsElements;
		pathsElements = new HashMap();
		boolean containKey = false;
		String nameClass;
		for (Element e : elements.values()) {
			nameClass = e.getNameClass();
			containKey = pathsElements.containsKey(nameClass);
			if (!containKey) {
				pathsElements.put(nameClass, e.getPathImage());
			}
		}
		return pathsElements;
	}

	public int[][] getMapTileNum() {
		return map.getMapTileNum();
	}

	protected void registerTiles() {
		for (Tile tile : map.getTiles()) {
			elements.put(elements.size() + 1, tile);
		}
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
		for(Player py : players) {
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
		// 1. Move automatic entities (also ticks bomb counters)
		for(Enemy am : getEnemies()) {
			am.move(checker, this);
		}
		
		// 2. Fire any pending bomb explosions (3x3 area, players + enemies)
		for(Bomb bomb : getBombs()) {
			bomb.action();
			bomb.explodeIfPending(this);
		}
		
		// 3. Check direct contact between players and interactable elements
		for(Player player : players) {
			checker.checkContactsWithInteractable(player, this, this);
			if(player.isDead()) {
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

	/** Returns all bombs currently in the level. */
	private List<Bomb> getBombs() {
		return elements.values().stream()
				.filter(e -> e instanceof Bomb)
				.map(e -> (Bomb) e)
				.toList();
	}

	/**
	 * Returns all Damageable entities (players and enemies) whose bounding box
	 * overlaps the given axis-aligned rectangle.
	 * @param x      left edge of the area in pixels
	 * @param y      top  edge of the area in pixels
	 * @param width  width  of the area in pixels
	 * @param height height of the area in pixels
	 */
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

	/* 
	 * AABB check: does element e overlap the rectangle (x, y, w, h)? 
	 */
	private boolean overlapsArea(Element e, int x, int y, float width, float height) {
		return e.getPosX() < x + width
			&& e.getPosX() +  e.getWidth()  > x
			&& e.getPosY() < y + height
			&& e.getPosY() +  e.getHeight() > y;
	}

	public boolean playerHasAllCoins(Player player) {
		return player.getCollectedCoins() >= numCoin ;
	}

	public void checkZones() {
		for(Player player : players) {
			for(Zone zone : zones) {
				if(zone.contains(player.getPosX(), player.getPosY())) {
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

	protected void putEnemy(List<Point> movement, String type) {
		// Al sumarle 1000, los enemigos se guardan lejos de los índices de las baldosas
		// Esto hace que se dibujen SIEMPRE por encima del suelo y no queden invisibles
		Enemy enemy = new Enemy(movement);
		int unIdAlto = elements.size() + 1000;
		switch (type) {
			case "basic" -> enemy.setStrategyMovement(new Basic(enemy));
			case "vertical" -> enemy.setStrategyMovement(new Vertical(enemy));
			case "acelerate" -> enemy.setStrategyMovement(new Acelerate(enemy));
		}
		elements.put(unIdAlto, enemy);
	}
	
	protected void putCoin(int row, int col) {
		int desface = DimensionGame.TILESIZE/4;
		Coin coin = new Coin(col*DimensionGame.TILESIZEHEIGHT+desface, row*DimensionGame.TILESIZEWIDTH+desface);
		elements.put(elements.size()+1, coin);
	}
	
	protected void putZone(ArrayList<Point> figure, String type) {
		switch (type) {
			case "goal" ->  zones.add(new GoalZone(new Figure(figure)));
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
	
	protected void addPointToList(int row, int col, List<Point> list) {
		list.add(new Point(col * DimensionGame.TILESIZEWIDTH, row * DimensionGame.TILESIZEHEIGHT));
	}
	
	public Zone getInitialZone() {
		for(Zone zone : zones) {
			if(zone instanceof InitialZone) {
				return zone;
			}
		}
		return null;
	}
	
	public void putBomb(int row, int col) {
		int desface = DimensionGame.TILESIZE/4;
		Bomb bomb =  new Bomb(col * DimensionGame.TILESIZEWIDTH+desface, row * DimensionGame.TILESIZEHEIGHT+desface);
		elements.put(elements.size()+1, bomb);
	}
	
	public abstract int getLevelTime();
}
