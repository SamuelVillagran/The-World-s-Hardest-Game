package presentation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import domain.GameMode;
import domain.HardestGameException;
import domain.PlayerType;
import domain.TheDOPOHardestGame;

public class GameContainer extends JPanel {

	private BufferedImage backgroundImage;
	private CardLayout cardLayout;
	private JPanel cardPanel;
	private GameSetup setup;
	private InfoPanel infoPanel;
	private TheDOPOHardestGameGUI activeGamePanel;

	public static final String MENU_MODE = "menu";
	public static final String PLAYER_CONFIG_MODE = "playerConfig";
	public static final String GAME_MODE = "game";

	
	public GameContainer(){
		prepareElements();
	}

	private final void prepareElements() {
		try {
		setup = new GameSetup();
		
		loadImages();
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(TheDOPOHardestGame.getGame().getScreenWidth(),
				TheDOPOHardestGame.getGame().getScreenHeight()));
		
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		cardPanel.setOpaque(false);

		cardPanel.add(new MenuPanel(this), MENU_MODE);

		infoPanel = new InfoPanel();
		infoPanel.setVisible(false);
		add(infoPanel, BorderLayout.NORTH);

		cardLayout.show(cardPanel, MENU_MODE);
		add(cardPanel, BorderLayout.CENTER);
		
		cardPanel.add(new PlayerConfig(this, setup.getMode()), PLAYER_CONFIG_MODE);
		}catch(HardestGameException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Advertencia!", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void loadImages() {
		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/background/backgroundMain.png"));
		} catch (IOException e) {
			backgroundImage = null;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null && !infoPanel.isVisible()) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
		}
	}

	public void showMode(String mode) {
		infoPanel.setVisible(GAME_MODE.equals(mode));
		cardLayout.show(cardPanel, mode);
		revalidate();
		repaint();
	}

	public void onModeSelected(ModeType mode) {
		setup.setMode(mode);
		cardPanel.add(new PlayerConfig(this, mode), PLAYER_CONFIG_MODE);
		showMode(PLAYER_CONFIG_MODE);
	}

	public void onPlayerConfigConfirmed(PlayerType type, String name) throws IOException, HardestGameException {
		setup.setPlayer(type, name);
		startGame();
	}
	
	public void onPlayerConfigConfirmed(PlayerType type, String name,
        PlayerType type2, String name2) throws IOException, HardestGameException {
		setup.setPlayer(type, name);
		if (type2 != null) {
			setup.setPlayer2(type2, name2);
		}
		startGame();
	}

	public void startGame() throws IOException {
		try {
			GameMode gameMode = setup.build();
			launchGamePanel(new TheDOPOHardestGameGUI(gameMode, infoPanel, this));
		} catch (Exception e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error al cargar el juego", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void resumeGame() {
		try {
			launchGamePanel(new TheDOPOHardestGameGUI(infoPanel, this));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al reanudar el juego: " + e.getMessage());
		}
	}

	public void goToMenu() {
		if (activeGamePanel != null) {
			activeGamePanel.stopThread();
		}
		infoPanel.setVisible(false);
		showMode(MENU_MODE);
	}

	public void restartFromLevel1() {
		if (activeGamePanel != null) {
			activeGamePanel.stopThread();
		}
		try {
			startGame();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al reiniciar: " + e.getMessage());
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

	public void loadSavedGame(File selectedFile) throws IOException, HardestGameException {
		TheDOPOHardestGame.open(selectedFile);
		resumeGame();
	}
}
