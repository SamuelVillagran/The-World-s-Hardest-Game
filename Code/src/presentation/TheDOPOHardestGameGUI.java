package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import domain.DimensionGame;
import domain.Element;
import domain.GameMode;
import domain.HardestGameException;
import domain.TheDOPOHardestGame;

public class TheDOPOHardestGameGUI extends JPanel implements Runnable {

	private static final int FPS = 60;

	private KeyHandler keyH;
	private Thread gameThread;
	private HashMap<String, BufferedImage> cachedImages;
	private HashMap<String, String> lastKnownPath;
	private InfoPanel infoPanel;

	public TheDOPOHardestGameGUI(GameMode gameMode, InfoPanel infoPanel) throws IOException, HardestGameException {
		this.infoPanel = infoPanel;
		TheDOPOHardestGame.getGame().startGame(gameMode, 3);
		cachedImages = new HashMap<>();
		lastKnownPath = new HashMap<>();
		prepareElements();
		prepareActions();
	}

	public TheDOPOHardestGameGUI(InfoPanel infoPanel) throws IOException, HardestGameException {
		this.infoPanel = infoPanel;
		cachedImages = new HashMap<>();
		prepareElements();
		prepareActions();
	}

	private void prepareActions() {
		keyH = new KeyHandler();
		addKeyListener(keyH);
	}

	private void prepareElements() throws IOException, HardestGameException {
		setScreen();
		loadImages();
	}

	private void loadImages() throws IOException, HardestGameException { // Help to make try/catch to GPT-4o
		HashMap<String, String> paths = TheDOPOHardestGame.getGame().getElementsToDraw();
		for (Entry<String, String> entry : paths.entrySet()) {
			String path = entry.getValue();
			InputStream stream = getClass().getResourceAsStream(path);
			if (stream == null) {
				System.err.println("loadImages | Recurso no encontrado: " + path);
				continue;
			}
			try {
				BufferedImage img = ImageIO.read(stream);
				cachedImages.put(entry.getKey(), img);
			} catch (IOException e) {
				System.err.println("loadImages | Error al leer imagen " + path);
				e.printStackTrace();
			}
		}
	}

	private void setScreen() {
		setPreferredSize(new Dimension(DimensionGame.SCREENWIDTH, DimensionGame.SCREENHEIGHT));
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		setFocusable(true);
	}

	@Override
	public void run() {
		final double nsInterval = 1_000_000_000.0 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long refreshTimer = 0;

		while (gameThread != null) {
			long currentTime = System.nanoTime();
			long elapsed = currentTime - lastTime;
			delta += elapsed / nsInterval;
			refreshTimer += elapsed;
			lastTime = currentTime;

			if (delta >= 1) {
				float deltaTime = 1.0f / FPS;
				try {
					update(deltaTime);
				} catch (HardestGameException e) {
					e.printStackTrace();
				}
				repaint();
				delta--;

				try {
					TheDOPOHardestGame game = TheDOPOHardestGame.getGame();
					if (game.isGameOver()) {
						gameThread = null;
						String message = game.isGameWon() ? "Juego completado!" : "Juego perdido!";
						int messageType = game.isGameWon() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE;
						JOptionPane.showMessageDialog(this, message, "Juego finalizado", messageType);
					}
				} catch (HardestGameException e) {
					e.printStackTrace();
				}
			}

			if (refreshTimer >= 100_000_000L) {
				refreshTimer = 0;
				SwingUtilities.invokeLater(() -> {
					try {
						TheDOPOHardestGame game = TheDOPOHardestGame.getGame();
						infoPanel.refresh(game.getPlayer1(), game.getTimeRemaining());
					} catch (HardestGameException e) {
						e.printStackTrace();
					}
				});
			}

			try {// Help to optimize by GPT-4o
				Thread.sleep(2);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private void update(float deltaTime) throws HardestGameException {
		TheDOPOHardestGame game = TheDOPOHardestGame.getGame();
		if (keyH.getW()) {
			game.movePlayer1('u');
		}
		if (keyH.getS()) {
			game.movePlayer1('d');
		}
		if (keyH.getA()) {
			game.movePlayer1('l');
		}
		if (keyH.getD()) {
			game.movePlayer1('r');
		}
		if (keyH.getUp()) {
			game.movePlayer2('u');
		}
		if (keyH.getDown()) {
			game.movePlayer2('d');
		}
		if (keyH.getLeft()) {
			game.movePlayer2('l');
		}
		if (keyH.getRigth()) {
			game.movePlayer2('r');
		}
		game.update(deltaTime);
	}

	public void startGameThread() {
		if (gameThread != null) {
			return;
		}
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void draw(Graphics2D g2) throws HardestGameException {
		String nameClass;
        String currentPath;
		for (Element e : TheDOPOHardestGame.getGame().getElements().values()) {// Ayudado a corregir con Claude Sonnet 4.6 IA
			nameClass = e.getNameClass();
	        currentPath = e.getPathImage();

	        // Si el path cambió, invalida la imagen cacheada
	        if (!currentPath.equals(lastKnownPath.get(nameClass))) {
	            cachedImages.remove(nameClass);
	            lastKnownPath.put(nameClass, currentPath);
	        }

	        BufferedImage img = cachedImages.get(nameClass);
	        if (img == null) {
	            try {
	                InputStream stream = getClass().getResourceAsStream(currentPath);
	                if (stream != null) {
	                    img = ImageIO.read(stream);
	                    cachedImages.put(nameClass, img);
	                }
	            } catch (IOException ex) {
	                System.err.println("draw | Error: " + currentPath);
	            }
	        }

	        if (img != null) {
	            g2.drawImage(img, (int) e.getPosX(), (int) e.getPosY(),
	                    (int) e.getWidth(), (int) e.getHeight(), null);
	        }
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		try {
			draw(g2);
		} catch (HardestGameException e) {
			e.printStackTrace();
		}
		g2.dispose();
	}
}
