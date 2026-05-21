package presentation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import domain.DimensionGame;
import domain.GameMode;
import domain.HardestGameException;
import domain.PlayerType;
import domain.TheDOPOHardestGame;

public class GameContainer extends JPanel{

	private BufferedImage backgroundImage;
	private CardLayout cardLayout;
	private JPanel cardPanel;
	private TheDOPOHardestGameGUI playerModePanel;
	private GameSetup setup;
	private InfoPanel infoPanel;
	private TheDOPOHardestGameGUI activeGamePanel;
	
	public static final String MENU_MODE = "menu";
	public static final String PLAYER_CONFIG_MODE = "playerConfig";
	public static final String GAME_MODE = "game";

	
	public GameContainer() throws HardestGameException {
		prepareElements();
	}

	private void prepareElements() throws HardestGameException{
		setup = new GameSetup();
		loadImages();
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(TheDOPOHardestGame.getGame().getScreenWidth(), TheDOPOHardestGame.getGame().getScreenHeight()));
		
		/*tittlePanel = buildTittlePanel();
		add(tittlePanel, BorderLayout.NORTH);*/
		
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		cardPanel.setOpaque(false);
		
		cardPanel.add(new MenuPanel(this), MENU_MODE);
		cardPanel.add(new PlayerConfig(this), PLAYER_CONFIG_MODE);
		
		infoPanel = new InfoPanel();
		infoPanel.setVisible(false);
		add(infoPanel, BorderLayout.NORTH);
		
		cardLayout.show(cardPanel, MENU_MODE);
		add(cardPanel, BorderLayout.CENTER);
	}
	
	private void loadImages() {
		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/background/backgroundMain.png"));
		} catch (IOException  e) {
			backgroundImage = null;
		}
	}
	
	private JPanel buildTittlePanel() throws HardestGameException {
		JPanel tittle = new JPanel();
		tittle.setOpaque(false);
		tittle.setPreferredSize(new Dimension(TheDOPOHardestGame.getGame().getScreenWidth(), TheDOPOHardestGame.getGame().getTileSizeHeight()));
		return tittle;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Solo dibujar el fondo si NO estamos jugando (infoPanel.isVisible() == true cuando jugamos)
		// Dibujar una imagen de fondo 60 veces por segundo ralentiza mucho el juego.
		if (backgroundImage != null && !infoPanel.isVisible()) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		}
	}
	
	public void showMode(String mode) {
		infoPanel.setVisible(GAME_MODE.equals(mode));;
		cardLayout.show(cardPanel, mode);
		revalidate();
		repaint();
	}

	public TheDOPOHardestGameGUI getPlayerModePanel() {
		return playerModePanel;
	}
	
	public void onModeSelected(ModeType mode) {
		setup.setMode(mode);
		showMode(PLAYER_CONFIG_MODE);
	}
	
	public void onPlayerConfigConfirmed(PlayerType type, String name) throws IOException, HardestGameException {
		setup.setPlayer(type, name);
		startGame(); 
	}
	
	public void startGame() throws IOException, HardestGameException {
		GameMode gameMode = setup.build();
		try {
			launchGamePanel(new TheDOPOHardestGameGUI(gameMode, infoPanel));
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al iniciar el juego: " + e.getMessage());
		}
	}
	
	/**
	 * Resumes a game loaded from a .dat file.
	 * The TheDOPOHardestGame singleton must already be restored before calling this.
	 */
	public void resumeGame() {
		try {
			launchGamePanel(new TheDOPOHardestGameGUI(infoPanel));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al reanudar el juego: " + e.getMessage());
		}
	}

	private void launchGamePanel(TheDOPOHardestGameGUI gamePanel) {
		if (activeGamePanel != null) {
			cardPanel.remove(activeGamePanel);
		}
		activeGamePanel = gamePanel;
		cardPanel.add(gamePanel, GAME_MODE);
		showMode(GAME_MODE);
		gamePanel.startGameThread();
		gamePanel.requestFocusInWindow();
	}
}
