package domain;

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
	protected CollisionChecker cCheker;

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
		for(Tile tile : map.getTiles()) {
			elements.put(elements.size() +1, tile);
		}
	}

	protected List<Enemy> getEnemies() {
		return elements.values().stream()
	            .filter(e -> e instanceof Enemy)
	            .map(e -> (Enemy) e)
	            .toList();
	}

	
}
