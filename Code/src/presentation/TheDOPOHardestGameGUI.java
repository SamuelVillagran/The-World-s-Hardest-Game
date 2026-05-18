package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import domain.DimensionGame;
import domain.Element;
import domain.Enemy;
import domain.GameMode;
import domain.HardestGameException;
import domain.TheDOPOHardestGame;
import domain.Tile;

public class TheDOPOHardestGameGUI extends JPanel implements Runnable {

	private static BufferedImage imageTitleScreen;

	private KeyHandler keyH;
	private Thread gameThread;
	private HashMap<String, BufferedImage> cachedImages;
	private InfoPanel infoPanel;
	private int secondsRemaining;
	
	public static final int FPS = 60;
	public static final int LEVEL_TIME_SECONDS = 90;
	
	/**
	 * Inicializate the game panel
	 * @throws IOException 
	 * @throws HardestGameException 
	 */
	public TheDOPOHardestGameGUI(GameMode gameMode, InfoPanel infoPanel) throws IOException, HardestGameException {
		this.infoPanel = infoPanel;
		secondsRemaining = LEVEL_TIME_SECONDS;
		TheDOPOHardestGame.getGame().startGame(gameMode, 1);
		cachedImages = new HashMap<>();
		prepareElements();
		prepareActions();
	}
	
	private void prepareActions() {
		
		keyH = new KeyHandler() {
			
		};
			
			
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
	 * Make a thread to game run with time
	 */
	@Override
	public void run() {

		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;

		while (gameThread != null) {

			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			if (delta >= 1) {
				// 1. UPDATE: 
				try {
					update();
				} catch (HardestGameException e) {
					e.printStackTrace();
				}
				// 2. DRAW:
				repaint();
				delta--;
			}

			if (timer >= 1000000000) {
				timer -= 1000000000;
				if(secondsRemaining > 0) secondsRemaining--;
				try {
					infoPanel.refresh(TheDOPOHardestGame.getGame().getPlayer1(), secondsRemaining);
				}catch (HardestGameException e) {
					e.printStackTrace();
				}
			}
			try { // No ahogar la CPU 
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Make the interaction of keyboard with the player
	 * @throws HardestGameException 
	 */
	private void update() throws HardestGameException {
		if (keyH.getUp() == true) {
			TheDOPOHardestGame.getGame().movePlayers('u');
		}
		if (keyH.getDown() == true) {
			TheDOPOHardestGame.getGame().movePlayers('d');
		}
		if (keyH.getLeft() == true) {
			TheDOPOHardestGame.getGame().movePlayers('l');
		}
		if (keyH.getRigth() == true) {
			TheDOPOHardestGame.getGame().movePlayers('r');	
		}
		
		TheDOPOHardestGame.getGame().update();
	
	}

	/**
	 * Make game thread run
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
        HashMap<Integer, Element> elements = TheDOPOHardestGame.getGame().getElements();

        // Dibujar; Tiles, obstáculos, monedas 
        for (Element e : elements.values()) {
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
