package domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Level implements CollisionContext {
	protected static int numCoin;
	protected LinkedHashMap<Integer, Element> elements;
	protected static Map map;
	protected CollisionChecker cCheker;
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

	public abstract boolean isCompleted();

	public abstract void spawnPlayers(List<Player> pys);

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
		for (AutomaticMovement am : getElementsAutomaticMovement()) {
			am.move();
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
			case "basic" -> enemy.setStrategyMovement(new Basic(enemy, cCheker, this));
			case "vertical" -> enemy.setStrategyMovement(new Vertical(enemy, cCheker, this));
			case "acelerate" -> enemy.setStrategyMovement(new Acelerate(enemy, cCheker, this));
		}
		elements.put(unIdAlto, enemy);
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
		list.add(new Point(col * DimensionGame.TILESIZEWIDTH, row * DimensionGame.TILESIZEHEIGHT ));
	}
}
