package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.DimensionGame;
import domain.HardestGameException;
import domain.Player;
import domain.PlayerType;
import domain.TheDOPOHardestGame;

public class InfoPanel extends JPanel{
	private static final Font INFO_FONT = new Font("Arial", Font.BOLD, 15);
	private JLabel colorChoosed;
	private JLabel typeLabel;
	private JLabel nameLabel;
	private JLabel deathsLabel;
	private JLabel coinsLabel;
	private JLabel timeLabel;
	
	// Jugador 2
    private JLabel colorChoosed2; // Ayudado con Claude Sonnet 4.6
    private JLabel typeLabel2;
    private JLabel nameLabel2;
    private JLabel deathsLabel2;
    private JLabel coinsLabel2;
    private JLabel separator;
	
	public InfoPanel() throws HardestGameException {
		setPreferredSize(new Dimension(TheDOPOHardestGame.getGame().getScreenWidth(), TheDOPOHardestGame.getGame().getTileSizeHeight()));
		setBackground(new Color(20, 20, 20));
		setLayout(new FlowLayout(FlowLayout.LEFT, 22, 18));

		colorChoosed = new JLabel("  ");
		colorChoosed.setOpaque(true);
		colorChoosed.setPreferredSize(new Dimension(28, 28));
		colorChoosed.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

		
		
		typeLabel  = makeLabel("Tipo: -",   Color.WHITE);
		nameLabel  = makeLabel("Nombre: -", Color.LIGHT_GRAY);
		deathsLabel = makeLabel("Muertes: 0", new Color(255, 100, 100));
		coinsLabel  = makeLabel("Monedas: 0", new Color(255, 220, 0));
		timeLabel   = makeLabel("Tiempo: --", new Color(100, 220, 255));

		separator = makeLabel("|", Color.WHITE); // Ayudado con Claude Sonnet 4.6

		colorChoosed2 = new JLabel("  ");
		colorChoosed2.setOpaque(true);
		colorChoosed2.setPreferredSize(new Dimension(28, 28));
		colorChoosed2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

		typeLabel2   = makeLabel("Tipo: -", Color.WHITE);
		nameLabel2   = makeLabel("Nombre: -", Color.LIGHT_GRAY);
		deathsLabel2 = makeLabel("Muertes: 0", new Color(255,100,100));
		coinsLabel2  = makeLabel("Monedas: 0", new Color(255,220,0));
		
		
		add(colorChoosed);
		add(typeLabel);
		add(nameLabel);
		add(deathsLabel);
		add(coinsLabel);
		add(timeLabel);
		
		add(separator);
		add(colorChoosed2);
		add(typeLabel2);
		add(nameLabel2);
		add(deathsLabel2);
		add(coinsLabel2);
	}
	
	
	private JLabel makeLabel(String text, Color color) {// Ayudado con Claude Sonnet 4.6
		JLabel label = new JLabel(text);
		label.setForeground(color);
		label.setFont(INFO_FONT);
		return label;
	}
	
	// ── Modo un solo jugador ──────────────────────────────────────────
    public void refresh(Player player, float timeRemaining) { 
        updatePlayerLabels(player, colorChoosed, typeLabel, nameLabel, deathsLabel, coinsLabel);
        timeLabel.setText("Tiempo: " + String.format("%.1f", Math.max(timeRemaining, 0f)) + "s");

        // Ocultar sección P2 si estaba visible
        separator.setVisible(false);
        colorChoosed2.setVisible(false);
        typeLabel2.setVisible(false);
        nameLabel2.setVisible(false);
        deathsLabel2.setVisible(false);
        coinsLabel2.setVisible(false);
    }

    // ── Modo PVP ─────────────────────────────────────────────────────
    public void refresh(Player player1, Player player2, float timeRemaining) {// Ayudado con Claude Sonnet 4.6
        updatePlayerLabels(player1, colorChoosed, typeLabel, nameLabel, deathsLabel, coinsLabel);
        timeLabel.setText("Tiempo: " + String.format("%.1f", Math.max(timeRemaining, 0f)) + "s");

        updatePlayerLabels(player2, colorChoosed2, typeLabel2, nameLabel2, deathsLabel2, coinsLabel2);

        // Mostrar sección P2
        separator.setVisible(true);
        colorChoosed2.setVisible(true);
        typeLabel2.setVisible(true);
        nameLabel2.setVisible(true);
        deathsLabel2.setVisible(true);
        coinsLabel2.setVisible(true);
    }

    // ── Lógica compartida ─────────────────────────────────────────────
    private void updatePlayerLabels(Player player, JLabel colorBox, JLabel type,
                                     JLabel name, JLabel deaths, JLabel coins) {// Ayudado con Claude Sonnet 4.6
        switch (player.getPlayerType()) {
            case RED   -> { colorBox.setBackground(Color.RED);                    type.setText("Tipo: Red");   }
            case BLUE  -> { colorBox.setBackground(new Color(60, 120, 255));      type.setText("Tipo: Blue");  }
            case GREEN -> { colorBox.setBackground(new Color(50, 200, 50));       type.setText("Tipo: Green"); }
        }
        name.setText("Nombre: "  + player.getName());
        deaths.setText("Muertes: " + player.getDeaths());
        coins.setText("Monedas: " + player.getCollectedCoins());
    }
}
