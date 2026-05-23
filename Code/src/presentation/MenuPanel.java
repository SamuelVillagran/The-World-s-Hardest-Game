package presentation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel{
	private GridBagConstraints gridBag;
	private JButton btnPlayer, btnPvP, btnVsMachine;
	
	public MenuPanel(GameContainer container) {
		prepareElements();
		prepareActions(container);
		
	}

	private final void prepareElements() {
		setOpaque(false);
		setLayout(new GridBagLayout());
		gridBag = new GridBagConstraints();
		gridBag.gridx = 0;
		gridBag.fill = GridBagConstraints.HORIZONTAL;
		gridBag.insets = new Insets(12, 60, 12, 60);

		btnPlayer = createButton("Player");
		btnPvP = createButton("Player vs Player");
		btnVsMachine = createButton("Player vs Machine");

		gridBag.gridy = 0;
		add(btnPlayer, gridBag);
		gridBag.gridy = 1;
		add(btnPvP, gridBag);
		gridBag.gridy = 2;
		add(btnVsMachine, gridBag);
	}
	
	private final void prepareActions(GameContainer container) {
		
		btnPlayer.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						container.onModeSelected(ModeType.SINGLE_PLAYER);
						
					}
				});
		
		btnPvP.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						container.onModeSelected(ModeType.PVP);
					}
				});
		
		
		btnVsMachine.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						container.onModeSelected(ModeType.PVM);
					}
				});
//		btnPlayer.addActionListener(
//				new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						container.showMode(GameContainer.PLAYER_MODE);
//					}
//				});
	}
	
	private JButton createButton(String text) { // Propuesta OpenAI modificada.
		JButton btn = new JButton(text);
		btn.setFont(new Font("Arial", Font.BOLD, 18));
		btn.setBackground(new Color(170, 165, 220));
		btn.setOpaque(true);
		btn.setPreferredSize(new Dimension(280, 55));
		return btn;
	}
	
	
	

}
