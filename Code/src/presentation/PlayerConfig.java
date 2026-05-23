package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.HardestGameException;
import domain.PlayerType;



public class PlayerConfig extends JPanel{
	
	private static final Color TEXT_COLOR    = new Color(220, 220, 220);
	private static final Color SECTION_COLOR = new Color(160, 160, 160);

	private JTextField nameField;
	private JComboBox<String> colorCombo;
	private BufferedImage backgroundImage;
	private JButton startBtn;
	private PlayerType selectedType = PlayerType.BLUE;
	private PlayerType selectedType2 = PlayerType.RED;
	private JTextField nameField2;
	private JPanel player2Panel;
	private ModeType mode;
	
	public PlayerConfig(GameContainer container) {
		loadImage();
		prepareElements(container);
		
	}
	
	public PlayerConfig(GameContainer container, ModeType mode) {
		this.mode = mode;
	    loadImage();
        prepareElements(container);
	}
	
	private JPanel buildPlayer2Section() { // Ayudado a hacer con Claude Sonnet 4.6
		JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setOpaque(false);

	    JLabel label = sectionLabel("Jugador 2 — Nombre");
	    label.setForeground(Color.BLACK);
	    label.setAlignmentX(LEFT_ALIGNMENT);

	    nameField2 = new JTextField();
	    nameField2.setFont(new Font("Arial", Font.PLAIN, 16));
	    nameField2.setBackground(new Color(30, 30, 30));
	    nameField2.setForeground(Color.WHITE);
	    nameField2.setCaretColor(Color.WHITE);
	    nameField2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
	    nameField2.setAlignmentX(LEFT_ALIGNMENT);

	    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    buttons.setOpaque(false);
	    buttons.add(colorColumn2(Color.BLUE,             "Azul",  PlayerType.BLUE));
	    buttons.add(colorColumn2(Color.RED,              "Rojo",  PlayerType.RED));
	    buttons.add(colorColumn2(new Color(30, 160, 30), "Verde", PlayerType.GREEN));

	    panel.add(label);
	    panel.add(Box.createVerticalStrut(4));
	    panel.add(nameField2);
	    panel.add(Box.createVerticalStrut(4));
	    panel.add(buttons);
	    return panel;
	}

	private JPanel colorColumn2(Color color, String labelText, PlayerType type) {
	    JPanel col = new JPanel(new BorderLayout(0, 5));
	    col.setOpaque(false);

	    JButton btn = new JButton();
	    btn.setBackground(color);
	    btn.setPreferredSize(new Dimension(52, 52));
	    btn.setBorderPainted(false);
	    btn.setFocusPainted(false);
	    btn.setOpaque(true);
	    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

	    JLabel lbl = new JLabel(labelText, JLabel.CENTER);
	    lbl.setForeground(TEXT_COLOR);
	    lbl.setFont(new Font("Arial", Font.BOLD, 13));

	    col.add(btn, BorderLayout.CENTER);
	    col.add(lbl, BorderLayout.SOUTH);
	    btn.addActionListener(e -> selectedType2 = type);
	    return col;
	}

	private final void loadImage() {
		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream("/background/playerConfig.png"));
		} catch (IOException e) {
			backgroundImage = null;
		}
	}
	
	private final void prepareElements(GameContainer container) {
		setOpaque(false);
		setLayout(new BorderLayout());
		
		// Paneles para rellenar laterales y parte superior
		add(spacerH(175),  BorderLayout.NORTH);
		add(spacerH(40),  BorderLayout.SOUTH);
		add(spacerW(300), BorderLayout.WEST);
		add(spacerW(300), BorderLayout.EAST);
		
		// Contenido Centro - GridLayout con 4 filas
		int rows = mode == ModeType.PVP ? 5 : 4;
		JPanel content = new JPanel(new GridLayout(rows, 1, 0, 18));
		content.setOpaque(false);

		content.add(buildNameRow());
		content.add(buildColorRow());
		content.add(buildComboRow());

		add(content, BorderLayout.CENTER);
		
		if (mode == ModeType.PVP) {
            player2Panel = buildPlayer2Section();
            content.add(player2Panel);
        }

		content.add(buildStartRow(container));
        add(content, BorderLayout.CENTER);
	}
	
	private JPanel buildNameRow() {
		JPanel row = new JPanel(new BorderLayout(0, 6));
		row.setOpaque(false);

		JLabel label = sectionLabel("Nombre del jugador");
		label.setForeground(Color.BLACK);
		nameField = new JTextField();
		nameField.setFont(new Font("Arial", Font.PLAIN, 16));
		nameField.setBackground(new Color(30, 30, 30));
		nameField.setForeground(Color.WHITE);
		nameField.setCaretColor(Color.WHITE);
		nameField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(90, 90, 90)),
				BorderFactory.createEmptyBorder(4, 8, 4, 8)));

		row.add(label,     BorderLayout.NORTH);
		row.add(nameField, BorderLayout.CENTER);
		return row;
	}

	// Seleccion de tipo de jugador
	private JPanel buildColorRow() {
		JPanel row = new JPanel(new BorderLayout(0, 6));
		row.setOpaque(false);

		JLabel label = sectionLabel("Tipo de jugador");
		label.setForeground(Color.BLACK);

		JPanel buttons = new JPanel();
		buttons.setOpaque(false);
		buttons.add(colorColumn(Color.BLUE,             "Azul", PlayerType.BLUE));
		buttons.add(colorColumn(Color.RED,              "Rojo", PlayerType.RED));
		buttons.add(colorColumn(new Color(30, 160, 30), "Verde", PlayerType.GREEN));

		row.add(label,   BorderLayout.NORTH);
		row.add(buttons, BorderLayout.CENTER);
		return row;
	}

	// Selección color del borde.
	private JPanel buildComboRow() {
		JPanel row = new JPanel(new BorderLayout(0, 6));
		row.setOpaque(false);

		JLabel label = sectionLabel("Color borde");
		label.setForeground(Color.BLACK);

		colorCombo = new JComboBox<>(new String[]{"Amarillo", "Negro", "Fucsia"});
		colorCombo.setFont(new Font("Arial", Font.PLAIN, 15));
		colorCombo.setBackground(new Color(30, 30, 30));
		colorCombo.setForeground(Color.WHITE);
		colorCombo.setMaximumSize(new Dimension(220, 34));

		JPanel comboWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		comboWrapper.setOpaque(false);
		comboWrapper.add(colorCombo);

		row.add(label,        BorderLayout.NORTH);
		row.add(comboWrapper, BorderLayout.CENTER);
		return row;
	}

	// Boton Iniciar.
	private JPanel buildStartRow(GameContainer container) {
		JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		row.setOpaque(false);

		startBtn = new JButton("Iniciar");
		startBtn.setFont(new Font("Arial", Font.BOLD, 17));
		startBtn.setBackground(new Color(30, 130, 30));
		startBtn.setForeground(Color.WHITE);
		//startBtn.setOpaque(true);
		startBtn.setPreferredSize(new Dimension(200, 46));
		prepareActionStart(container);
		
		row.add(startBtn);
		return row;
	}
	
	private JPanel colorColumn(Color color, String labelText, PlayerType type) {
		JPanel col = new JPanel(new BorderLayout(0, 5));
		col.setOpaque(false);

		JButton btn = new JButton();
		btn.setBackground(color);
		btn.setPreferredSize(new Dimension(52, 52));
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setOpaque(true);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		JLabel lbl = new JLabel(labelText, JLabel.CENTER);
		lbl.setForeground(TEXT_COLOR);
		lbl.setFont(new Font("Arial", Font.BOLD, 13));

		col.add(btn, BorderLayout.CENTER);
		col.add(lbl, BorderLayout.SOUTH);
		btn.addActionListener(e -> selectedType = type);
		return col;
	}

	private JLabel sectionLabel(String text) {
		JLabel lbl = new JLabel(text);
		lbl.setForeground(SECTION_COLOR);
		lbl.setFont(new Font("Arial", Font.BOLD, 13));
		return lbl;
	}
	
	private JPanel spacerH(int height) {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setPreferredSize(new Dimension(1, height));
		return p;
	}

	private JPanel spacerW(int width) {
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setPreferredSize(new Dimension(width, 1));
		return p;
	}
	
	public String getPlayerName() {
		return nameField.getText().trim();
	}

	public String getSelectedComboColor() {
		return (String) colorCombo.getSelectedItem();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	    if (backgroundImage != null) {
	        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
	    }
	}
	
	private void prepareActionStart(GameContainer container) {
		startBtn.addActionListener(e -> {
		String name = nameField.getText();
		if(name.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Ingrese un nombre");
			return;
		}
			
		PlayerType type2 = null;
	    String name2 = null;
	    try {
             if (mode == ModeType.PVP) {
                  name2 = nameField2.getText();
                  if (name2.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Ingrese nombre del jugador 2");
                        return;
                  }
                  container.onPlayerConfigConfirmed(selectedType, name, selectedType2, name2);
                } else {
                  container.onPlayerConfigConfirmed(selectedType, name, null, null);
                }
            } catch (IOException | HardestGameException ex) {
                ex.printStackTrace();
            }
		});
	}
}
