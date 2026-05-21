package presentation;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import domain.HardestGameException;
import domain.TheDOPOHardestGame;

public class Window extends JFrame {

	private static JFrame window; 
	private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem optionOpen, optionSaveAs, optionImport, optionExportAs, optionNew, optionExit;
    private GameContainer gameContainer;
    
	
    public Window() throws HardestGameException {
    	setScreen();
    	prepareElements();
    	prepareActions();
	}
	
    private void prepareActions() {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	optionSaveAs.addActionListener(
        		new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				saveAsAction();
        			}
        	});
    	optionOpen.addActionListener(
        		new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
    			    	openAction();
        		}
        	});
	}

	protected void saveAsAction() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("DAT Files", "dat"));
		int result = fileChooser.showSaveDialog(Window.this);
		if(result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			if(!selectedFile.getName().endsWith(".dat")) {
				selectedFile = new File(selectedFile.getAbsolutePath() + ".dat");
			}
			try {
			    TheDOPOHardestGame.getGame().saveAs(selectedFile);
			} catch (IOException io) { // Catch escrito con Gemini Pro 3.1
			    // 1. Agrega esto para ver la causa exacta en la consola de Eclipse:
			    io.printStackTrace(); 
			    
			    // 2. Opcionalmente, puedes mostrar la causa en la misma ventana:
			    JOptionPane.showMessageDialog(Window.this, 
			        "Error al guardar archivo:\n" + io.getMessage(), 
			        "Error",
			        JOptionPane.ERROR_MESSAGE);
			} catch (HardestGameException e) {
			    e.printStackTrace();
			}
		}
	}
	
	private void openAction() {
    	JFileChooser fileChooser = new JFileChooser();
    	fileChooser.setFileFilter(new FileNameExtensionFilter("DAT Files", "dat"));
    	int result = fileChooser.showOpenDialog(Window.this);
    	if(result == JFileChooser.APPROVE_OPTION) {
    		File selectedFile = fileChooser.getSelectedFile();
    		try {
    			gameContainer.loadSavedGame(selectedFile);
    		} catch(Exception ex){
    			ex.printStackTrace();
    			JOptionPane.showMessageDialog(Window.this, ex.getMessage(),"Error",
    					JOptionPane.ERROR_MESSAGE);
    		}
    	}
    }

	private void prepareElements() {
		prepareElementsMenu();
	}
	
	private void prepareElementsMenu() {
		menuBar  = new JMenuBar();
    	menu = new JMenu("Archivo");
    	optionNew = new JMenuItem("Nuevo");
    	optionSaveAs = new JMenuItem("Guardar");
    	optionOpen = new JMenuItem("Abrir");
    	optionImport = new JMenuItem("Importar");
    	optionExportAs = new JMenuItem("Exportar como");
    	optionExit = new JMenuItem("Salir");
    	
    	menu.add(optionNew);
    	menu.addSeparator();
    	menu.add(optionSaveAs);
    	menu.add(optionOpen);
    	menu.addSeparator();
    	menu.add(optionExportAs);
    	menu.add(optionImport);
    	menu.addSeparator();
    	menu.add(optionExit);
    	
    	menuBar.add(menu);
    	setJMenuBar(menuBar);
	}
	
	private void setScreen() throws HardestGameException {
		setResizable(false);
	    setTitle("The DOPO Hardest Game");
	    prepareElementsMenu();
	    gameContainer = new GameContainer(); // ← guardar referencia
	    add(gameContainer);
	    pack();
	    setLocationRelativeTo(null);
    }
    
    
	public static void main(String[] args) throws HardestGameException {
		window  = new Window();
		window.setVisible(true);
	}
}
