package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class Level implements CollisionContext{
	protected static int numCoin; 
	protected LinkedHashMap<Integer, Element> elements;
	protected static Map map;
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
		numCoin = 0;
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
		
		pathsElements = new HashMap<String, String>();
		for (Element e : elements.values()) {
			String nameClass = e.getNameClass();
			if (!pathsElements.containsKey(nameClass)) {
				pathsElements.put(nameClass, e.getPathImage());
			}
		}
		return pathsElements;
	}

	public int[][] loadMap() {
		return map.getMapTileNum();
	}

	public int[][] getMapTileNum() {
		return map.getMapTileNum();
	}
	
	protected void registerTiles() {
		for(Tile tile : map.getTiles()) {
			elements.put(elements.size() +1, tile);
		}
	}
	
	public void setPlayers(List<Player> players) {
		this.players = players;
		for(Player py : players) {
			elements.put(elements.size() + 1, py);
		}
	}

	public void removeElement(Element element) {
		
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
}
