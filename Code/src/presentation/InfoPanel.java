package presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.DimensionGame;
import domain.Player;
import domain.PlayerType;

public class InfoPanel extends JPanel{
	private static final Font INFO_FONT = new Font("Arial", Font.BOLD, 15);
	private JLabel colorChoosed;
	private JLabel typeLabel;
	private JLabel nameLabel;
	private JLabel deathsLabel;
	private JLabel coinsLabel;
	private JLabel timeLabel;
	
	public InfoPanel() {
		setPreferredSize(new Dimension(DimensionGame.SCREENWIDTH, DimensionGame.TITTLE_HEIGHT));
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

		add(colorChoosed);
		add(typeLabel);
		add(nameLabel);
		add(deathsLabel);
		add(coinsLabel);
		add(timeLabel);
	}
	
	private JLabel makeLabel(String text, Color color) {
		JLabel label = new JLabel(text);
		label.setForeground(color);
		label.setFont(INFO_FONT);
		return label;
	}
	
	public void refresh(Player player, int secondsRemaining) {
		PlayerType type = player.getPlayerType();

		if (type == PlayerType.RED) {
			colorChoosed.setBackground(Color.RED);
			typeLabel.setText("Tipo: Red");
		} else if (type == PlayerType.BLUE) {
			colorChoosed.setBackground(new Color(60, 120, 255));
			typeLabel.setText("Tipo: Blue");
		} else if (type == PlayerType.GREEN) {
			colorChoosed.setBackground(new Color(50, 200, 50));
			typeLabel.setText("Tipo: Green");
		}

		nameLabel.setText("Nombre: " + player.getName());
		deathsLabel.setText("Muertes: " + player.getDeaths());
		coinsLabel.setText("Monedas: " + player.getCountCoins());
		timeLabel.setText("Tiempo: " + secondsRemaining + "s");
	}
}
