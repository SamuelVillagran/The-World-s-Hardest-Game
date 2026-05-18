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

public class GameContainer extends JPanel{

	private BufferedImage backgroundImage;
	private CardLayout cardLayout;
	private JPanel cardPanel;
	private TheDOPOHardestGameGUI playerModePanel;
	private GameSetup setup;
	private InfoPanel infoPanel;
	
	public static final String MENU_MODE = "menu";
	public static final String PLAYER_CONFIG_MODE = "playerConfig";
	public static final String GAME_MODE = "game";
	
	public GameContainer() {
		prepareElements();
	}

	private void prepareElements(){
		setup = new GameSetup();
		loadImages();
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(DimensionGame.SCREENWIDTH, DimensionGame.SCREENHEIGHT));
		
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
	
	private JPanel buildTittlePanel() {
		JPanel tittle = new JPanel();
		tittle.setOpaque(false);
		tittle.setPreferredSize(new Dimension(DimensionGame.SCREENWIDTH, DimensionGame.TITTLE_HEIGHT));
		return tittle;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null) {
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
			TheDOPOHardestGameGUI gamePanel = new TheDOPOHardestGameGUI(gameMode, infoPanel);
			cardPanel.add(gamePanel, GAME_MODE);
			showMode(GAME_MODE);
			gamePanel.startGameThread();
			gamePanel.requestFocusInWindow();

		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al iniciar el juego: " + e.getMessage());
		}
		
	}
}
