package domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class TheDOPOHardestGame implements Serializable{

	private static final int TOTAL_LEVELS = 3;
	private int secondsRemaining;

	private boolean paused = false;
	
	private static Level currentLevel;
	private ArrayList<Player> players;
	private static int numCurrentLevel;
	private GameMode gameMode;
	private CollisionChecker cChecker;
	private static TheDOPOHardestGame game;
	private boolean gameOver;
	
	private final ArrayList<GameObserver> observers = new ArrayList<>();
	
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
		this.numCurrentLevel = numCurrentLevel; 
		players = new ArrayList<>(gameMode.createPlayers());
		loadLevel(buildLevel(numCurrentLevel));
	}
	
	private Level buildLevel(int num) throws HardestGameException {
		switch(num){
			case 1: return new Level1(cChecker);
			case 2: return new Level2(cChecker);
			case 3: return new Level3(cChecker);
			default : throw new HardestGameException("Nivel no existe");
		}
	}
	
	public void loadLevel(Level level) {
		this.currentLevel = level;
		currentLevel.initialize();
		currentLevel.setPlayers(players);
		currentLevel.spawnPlayers(players);
		currentLevel.resetTime();
	}
	
	public float getTimeRemaining() {
		return currentLevel.getTimeRemaining();
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
	
	/**
	 * Move player 1 to specific direction
	 * @param direction direction is 'l': left, 'r': right, 'u': up or 'd': down
	 */
	public void movePlayer1(char direction) {
		players.get(0).move(direction, currentLevel, cChecker);
	}
	
	/**
	 * Move player 2 to specific direction
	 * @param direction direction is 'l': left, 'r': right, 'u': up or 'd': down
	 */
	public void movePlayer2(char direction) {
		players.get(1).move(direction, currentLevel, cChecker);
		
	}
	
	public void setCurrentLevel(int numLevel) {
		numCurrentLevel = numLevel;
	}
	
	public void update(float delta) throws HardestGameException {
		currentLevel.tickTime(delta);
		currentLevel.update(cChecker);
		
		if(currentLevel.isCompleted()) {
			nextLevel();
			return;
		}
		
		if(currentLevel.isTimeUp() || gameMode.isGameOver(players, currentLevel)) {
			endGame();
			return;
		}
		
		if(gameMode.isGameOver(players, currentLevel)) {
			Player winner = gameMode.getWinner(players);
			endGame();
			return;
		}
	}
	
	private void endGame() {
		gameOver = true;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public void nextLevel() {
		numCurrentLevel ++;
		if(!hasNextLevel(numCurrentLevel)) {
			//Implementar EndGame
			return;
		}
		for(Player player : players) {
			player.reset();
		}
		try {
			loadLevel(buildLevel(numCurrentLevel));
		} catch (HardestGameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean hasNextLevel(int num) {
		return num <= TOTAL_LEVELS;
	}
	
	public PlayerType getPlayerType(String type) {
		switch (type) {
			case "red": return PlayerType.RED;
			case "blue": return PlayerType.BLUE;
			case "green": return PlayerType.GREEN;
			default: return PlayerType.RED;
		}
	}
	
	public void pauseGame() {
		paused = true;
	}
	
	public void despauseGame() {
		paused = false;
	}
	

	public int getScreenWidth() {
		return DimensionGame.getScreenWidth();
	}
	
	public int getScreenHeight() {
		return DimensionGame.getScreenHeight();
	}

	public int getTileSizeHeight() {
		return DimensionGame.getTileSizeHeight();
	}
	
	public boolean isPaused() {
		return paused;
	}
	/**
     * Opens a specified file.
     * @param file the name or path of file to be saved.
     * @return Forest game.
     * @throws ForestException if there are problems with the disk or files.
     * 			or file is corrupt.
     */
    public static TheDOPOHardestGame open(File file) throws HardestGameException {
    	if(!file.exists()) {
    		throw new HardestGameException(HardestGameException.FILE_NO_FOUND);
    	}
    	
    	try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
    		game = (TheDOPOHardestGame) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
		}
		return game; 
    }
    
    /**
     * Save like a file the game running
     * @param file
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws ForestException
     */
    public void saveAs(File file) throws HardestGameException, FileNotFoundException, IOException {
   
    	try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
    		try {
				out.writeObject(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} 
    }
}
