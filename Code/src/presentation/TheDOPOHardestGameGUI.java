package presentation;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import domain.Element;
import domain.GameMode;
import domain.GameObserver;
import domain.HardestGameException;
import domain.TheDOPOHardestGame;

public class TheDOPOHardestGameGUI extends JPanel implements GameObserver {

	private static BufferedImage imageTitleScreen;
	private KeyHandler keyH;
	private HashMap<String, BufferedImage> cachedImages;
	private InfoPanel infoPanel;
	
	
	/**
	 * Inicializate the game panel
	 * @throws IOException 
	 * @throws HardestGameException 
	 */
	public TheDOPOHardestGameGUI(InfoPanel infoPanel) throws IOException, HardestGameException {
		this.infoPanel = infoPanel;
		/*
		secondsRemaining = LEVEL_TIME_SECONDS;
		TheDOPOHardestGame.getGame().startGame(gameMode, 1); */
		cachedImages = new HashMap<>();
		prepareElements();
		prepareActions();
	}
	
	private void prepareActions() {
		keyH = new KeyHandler();
		this.addKeyListener(keyH);
		
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
	            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	                try {
	                    TheDOPOHardestGame game = TheDOPOHardestGame.getGame();
	                    if (game.isPaused()) {
	                        game.despauseGame();
	                    } else {
	                        game.pauseGame();
	                    }
	                } catch (HardestGameException ex) {
	                    ex.printStackTrace();
	                }
	            }
	        }
	    });
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
    
	private void setScreen() throws HardestGameException {
		this.setPreferredSize(new Dimension(TheDOPOHardestGame.getGame().getScreenWidth(), TheDOPOHardestGame.getGame().getScreenHeight()));
		this.setBackground(Color.BLACK);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
	}

	/**
	 * Make the interaction of keyboard with the player
	 * @throws HardestGameException 
	 */
	private void update() throws HardestGameException {
		if (keyH.getW() == true) {
			TheDOPOHardestGame.getGame().movePlayer1('u');
		}
		if (keyH.getS() == true) {
			TheDOPOHardestGame.getGame().movePlayer1('d');
		}
		if (keyH.getA() == true) {
			TheDOPOHardestGame.getGame().movePlayer1('l');
		}
		if (keyH.getD() == true) {
			TheDOPOHardestGame.getGame().movePlayer1('r');	
		}
		
		if (keyH.getUp() == true) {
			TheDOPOHardestGame.getGame().movePlayer2('u');
		}
		if (keyH.getDown() == true) {
			TheDOPOHardestGame.getGame().movePlayer2('d');
		}
		if (keyH.getLeft() == true) {
			TheDOPOHardestGame.getGame().movePlayer2('l');
		}
		if (keyH.getRigth() == true) {
			TheDOPOHardestGame.getGame().movePlayer2('r');	
		}
		TheDOPOHardestGame.getGame().update();
	}

	/**
	 * Draw at a panel g2 different entitys
=======
	 * Draw at a panel g2 different entities
>>>>>>> origin/Hernan
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
			e.printStackTrace();
		}
	    g2.dispose();
	}

	/**
	 * Make the interaction of keyboard with the player
	 * @throws HardestGameException 
	 */
	@Override
	public void preUpdate(){
		try {
			TheDOPOHardestGame.getGame().despauseGame();
			update();
		} catch(HardestGameException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void postUpdate() {
		repaint();
	}

	@Override
	public void secondsElapsed(int secondsRemaining) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					infoPanel.refresh(secondsRemaining);
				} catch(HardestGameException e) {
					JOptionPane.showMessageDialog(TheDOPOHardestGameGUI.this, e.getMessage(),"Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
