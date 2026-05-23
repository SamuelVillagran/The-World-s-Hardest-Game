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
import java.util.List;

public class TheDOPOHardestGame implements Serializable {

	private boolean paused = false;
	private boolean gameOver = false;
	private boolean gameWon = false;

	private Level currentLevel;
	private ArrayList<Player> players;
	private int numCurrentLevel;
	private GameMode gameMode;
	private CollisionChecker cChecker;
	private static TheDOPOHardestGame game;

	
	private TheDOPOHardestGame() throws HardestGameException {
		cChecker = new CollisionChecker();
	}

	public static TheDOPOHardestGame getGame() throws HardestGameException {
		if (game == null) {
			game = new TheDOPOHardestGame();
		}
		return game;
	}

	public void startGame(GameMode gameMode, int numCurrentLevel) throws HardestGameException {
		this.gameMode = gameMode;
		this.numCurrentLevel = numCurrentLevel;
		this.players = new ArrayList<>(gameMode.createPlayers());
		this.gameOver = false;
		this.gameWon = false;
		this.paused = false;
		loadLevel(buildLevel(numCurrentLevel));
	}

	private Level buildLevel(int num) throws HardestGameException {
		return LevelCatalog.create(num);
	}

	public void loadLevel(Level level) {
		this.currentLevel = level;
		currentLevel.initialize();
		currentLevel.setPlayers(players);
		currentLevel.spawnPlayers(players);
	}

	public HashMap<String, String> getElementsToDraw() throws IOException {
		return currentLevel.getElementsToDraw();
	}
	
	/**
	 * Resets the singleton instance.
	 * Exclusive use for tests.
	 */
	public static void resetForTesting() {
		game = null;
	}
	
	/**
	 * Set up the game with a custom level and player list
	 * @return
	 */
	public void loadTestLevel(Level level, List<Player> players) {
		this.players = new ArrayList<Player>(players);
		loadLevel(level);
	}

	public Player getPlayer1() {
		return players.get(0);
	}

	public Player getPlayer2() {
		return players.get(1);
	}

	public HashMap<Integer, Element> getElements() {
		return currentLevel.getElements();
	}

	public int[][] loadMap() {
		return currentLevel.getMapTileNum();
	}

	public Tile[] loadTiles() {
		Tile[] tiles = new Tile[11];
		tiles[0] = new Floor(0, 0);
		tiles[1] = new Wall(0, 0);
		tiles[2] = new GreenTile(0, 0);
		return tiles;
	}

	public void movePlayer1(char direction) {
		if (players.size() > 0) {
			players.get(0).move(direction, currentLevel, cChecker);
		}
	}

	public void movePlayer2(char direction) {
		if (players.size() > 1) {
			players.get(1).move(direction, currentLevel, cChecker);
		}
	}

	public void movePlayers(char direction) {
		movePlayer1(direction);
	}

	public void setCurrentLevel(int numLevel) {
		numCurrentLevel = numLevel;
	}

	public void switchToLevel(int numLevel) throws HardestGameException {
		if (!LevelCatalog.hasLevel(numLevel)) {
			throw new HardestGameException("Nivel no existe");
		}
		numCurrentLevel = numLevel;
		for (Player player : players) {
			player.reset();
		}
		gameOver = false;
		gameWon = false;
		loadLevel(buildLevel(numCurrentLevel));
	}

	public void update(float delta) throws HardestGameException {
		if (paused || gameOver) {
			return;
		}

		currentLevel.tickTime(delta);
		currentLevel.update(cChecker);

		if (currentLevel.isCompleted()) {
			nextLevel();
			return;
		}

		if (currentLevel.isTimeUp() || gameMode.isGameOver(players, currentLevel)) {
			endGame(false);
		}
	}

	private void endGame(boolean won) {
		gameOver = true;
		gameWon = won;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isGameWon() {
		return gameWon;
	}

	public void nextLevel() {
		numCurrentLevel++;
		if (!LevelCatalog.hasLevel(numCurrentLevel)) {
			endGame(true);
			return;
		}
		for (Player player : players) {
			player.reset();
		}
		try {
			loadLevel(buildLevel(numCurrentLevel));
		} catch (HardestGameException e) {
			e.printStackTrace();
			endGame(false);
		}
	}

	public PlayerType getPlayerType(String type) {
		switch (type) {
			case "red":
				return PlayerType.RED;
			case "blue":
				return PlayerType.BLUE;
			case "green":
				return PlayerType.GREEN;
			default:
				return PlayerType.RED;
		}
	}

	public void pauseGame() {
		paused = true;
	}

	public void despauseGame() {
		paused = false;
	}

	public void setPaused() {
		pauseGame();
	}

	public void resumeGame() {
		despauseGame();
	}

	public float getTimeRemaining() {
		return currentLevel != null ? currentLevel.getTimeRemaining() : 0f;
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

	public static TheDOPOHardestGame open(File file) throws HardestGameException {
		if (!file.exists()) {
			throw new HardestGameException(HardestGameException.FILE_NO_FOUND);
		}

		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
			game = (TheDOPOHardestGame) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			throw new HardestGameException("Archivo corrupto o no compatible.");
		}
		return game;
	}

	public void saveAs(File file) throws HardestGameException, FileNotFoundException, IOException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
			out.writeObject(this);
		} catch (IOException e) {
			throw new HardestGameException("Error al escribir el archivo: " + e.getMessage());
		}
	}


	public GameMode getGameMode() {
		return gameMode;
	}

	public boolean isPaused() {
		return paused;
	}
}
