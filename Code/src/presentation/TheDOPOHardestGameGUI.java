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

	private static BufferedImage imageTitleScreen;

	private static final int FPS = 60;

	private KeyHandler keyH;
	private Thread gameThread;
	private HashMap<String, BufferedImage> cachedImages;
	private InfoPanel infoPanel;

	/**
	 * initialize the game panel and starts the game in domain.
	 * @throws IOException
	 * @throws HardestGameException
	 */
	public TheDOPOHardestGameGUI(GameMode gameMode, InfoPanel infoPanel) throws IOException, HardestGameException {
		this.infoPanel = infoPanel;
		TheDOPOHardestGame.getGame().startGame(gameMode, 1);
		cachedImages = new HashMap<>();
		prepareElements();
		prepareActions();
	}
	
	private void prepareActions() {
		keyH = new KeyHandler();
		this.addKeyListener(keyH);
	}

	private void prepareElements() throws IOException, HardestGameException {
		setScreen();
		loadImages(); // <-- cargar una sola vez
    }

	/*
	 * Load the paths of images of objects of game
	 */
    private void loadImages() throws IOException, HardestGameException {
    	HashMap<String, String> paths = TheDOPOHardestGame.getGame().getElementsToDraw();
        for (Entry<String, String> entry : paths.entrySet()) {
            String path = entry.getValue();
            InputStream stream = getClass().getResourceAsStream(path);
            if (stream == null) {
                System.err.println("loadImages| Recurso no encontrado en classpath " + path);
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
		this.setPreferredSize(new Dimension(DimensionGame.SCREENWIDTH, DimensionGame.SCREENHEIGHT));
		this.setBackground(Color.BLACK);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
	}

	/**
	 * Main loop: calculates deltaTime, process users input,
	 * updates domain and redraw.
	 */
	@Override
	public void run() {
		final double NS_INTERVAL = 1_000_000_000.0 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long refreshTimer = 0;

		while (gameThread != null) {
			long currentTime = System.nanoTime();
			long elapsed = currentTime - lastTime;
			delta += elapsed / NS_INTERVAL;
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
					if(TheDOPOHardestGame.getGame().isGameOver()) {
						gameThread = null;
						JOptionPane.showMessageDialog(this, "Juego perdido!", "Juego finalizado", JOptionPane.WARNING_MESSAGE);
					}
				} catch (HardestGameException e) {
					e.printStackTrace();
				}
			}

			//Actualiza InfoPanel
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

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Sends the input keyboard input and delegate the update to domain
	 */
	private void update(float deltaTime) throws HardestGameException {
		TheDOPOHardestGame game = TheDOPOHardestGame.getGame();
		if (keyH.getUp())    game.movePlayers('u');
		if (keyH.getDown())  game.movePlayers('d');
		if (keyH.getLeft())  game.movePlayers('l');
		if (keyH.getRigth()) game.movePlayers('r');
		game.update(deltaTime);
	}

	/**
	 * Starts the thread game loop.
	 */
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	/**
	 * Draw at a panel g2 different entitys
	 * @param g2
	 * @throws HardestGameException 
	 */
	public void draw(Graphics2D g2) throws HardestGameException {
        // Dibujar; Tiles, obstáculos, monedas 
        for (Element e : TheDOPOHardestGame.getGame().getElements().values()) {
            BufferedImage img = cachedImages.get(e.getNameClass());
            if (img != null) {
                g2.drawImage(img, e.getPosX(), e.getPosY(),
                    (int)(e.getWidth()),
                    (int)(e.getHeight()),
                    null);
            }
        }
    }
	
	/**
	 * Paint components at the graphic
	 */
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    try {
			draw(g2);
		} catch (HardestGameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    g2.dispose();
	}
	
	
}
