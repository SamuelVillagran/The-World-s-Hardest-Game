package domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class TheDOPOHardestGame {

	private static Level currentLevel;
	private ArrayList<Player> players;
	private static int numCurrentLevel;
	private GameMode gameMode;
	private CollisionChecker cChecker;
	private static TheDOPOHardestGame game;
	
	/**
	 * Constructor class to start game once Window is open.
	 * @throws HardestGameException 
	 */
	private TheDOPOHardestGame () throws HardestGameException {
		cChecker = new CollisionChecker();
	}
	
	/**
	 * Allow get instance of principal game
	 * @return The one game instance
	 * @throws HardestGameException 
	 */
	public static TheDOPOHardestGame getGame() throws HardestGameException {
		if (game == null) {
			game = new TheDOPOHardestGame();
		}
		return game;
	}
	
	/**
	 * Start the game with a specific characteristics.
	 * @param gameMode specific Game Mode it could take values;
	 * PlayerMode, PlaverVsMachine, PlayerVsPlayer.
	 * @param numCurrentLevel the actual number level.
	 * @throws HardestGameException
	 */
	public void startGame(GameMode gameMode, int numCurrentLevel) throws HardestGameException {
		this.gameMode = gameMode;
		players = new ArrayList<>(gameMode.createPlayers());
		this.numCurrentLevel = numCurrentLevel; 
		this.currentLevel = new Level1(cChecker);
		this.numCurrentLevel = numCurrentLevel;
		currentLevel.spawnPlayers(players);
		currentLevel.setPlayers(players);
	}
	
	/**
	 * Give elements to GUI can draw it 
	 * @return HashMap HashMap with first String with name element and second path element's file
	 * @throws IOException
	 */
	public HashMap<String, String> getElementsToDraw() throws IOException{
		HashMap<String, String> elementsPath = this.currentLevel.getElementsToDraw();
		return elementsPath;
	}

	public Player getPlayer1() {
		return players.get(0);
	}
	
	public Player getPlayer2() {
		return players.get(1);
	}
	
	public HashMap<Integer, Element> getElements() {
		return this.currentLevel.getElements();
	}
	
	/**
	 * Load the buffered text's map and convert it into an integer matrix. This matrix is named mapTileNum.
	 * @param filePathMap
	 */
	public int[][] loadMap() {
		return this.currentLevel.getMapTileNum();
	}
	
	/**
	 * Load every tile that there are at the game
	 * @return An Array of different types of tile 0: Floor, 1: Obstacle
	 */
	public Tile[] loadTiles() {
		Tile[] tiles = new Tile[11];
		tiles[0] = new Floor(0, 0);
		tiles[1] = new Wall(0, 0);
		tiles[2] = new GreenTile(0, 0);
		return tiles;
	}
	
	/**
	 * Move every player to specific direction
	 * @param direction direction is 'l': left, 'r': right, 'u': up or 'd': down
	 */
	public void movePlayers(char direction) {
		for (Player py : players) {
			py.move(direction, currentLevel, cChecker);
		}
	}
	
	public void setCurrentLevel(int numLevel) {
		numCurrentLevel = numLevel;
	}
	
	public List<Enemy> getEnemies() {
		return currentLevel.getEnemies();
	}
	
	public void update() {
		for (Enemy e : getEnemies()) {
			e.move();
		}
		
		currentLevel.update(cChecker);
	}
}
