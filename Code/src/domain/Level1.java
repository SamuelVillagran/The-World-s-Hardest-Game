package domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Level1 extends Level {
	private CollisionChecker cCheker;

	public Level1() {
		numCoin = 3;
		map = new Map(1);
		registerTiles();
		initialize();
		
	}
	
	public Level1(CollisionChecker cCheker) {
		numCoin = 3;
	    map = new Map(1);
	    registerTiles();        // 1. Las baldosas toman los índices bajos (0, 1, 2...)
	    this.cCheker = cCheker; // 2. Asignas el checker antes de crear los enemigos
	    initialize();           // 3. Creas los enemigos al final
	}


	@Override
	public void initialize() { // Ayudado a poner por Gemini IA 2026, supervisado 
	    // Enemigo 1: Fila 8, empieza a la izquierda (col 9) y va a la derecha (col 25)
	    List<Point> movementEnemy1 = new LinkedList<>();
	    addPointToListMovement(8, 7, movementEnemy1);  // Inicio (Izquierda)
	    addPointToListMovement(8, 24, movementEnemy1); // Destino (Derecha)
	    putEnemy(movementEnemy1);

	    // Enemigo 2: Fila 9, empieza a la derecha (col 25) y va a la izquierda (col 9)
	    List<Point> movementEnemy2 = new LinkedList<>();
	    addPointToListMovement(9, 24, movementEnemy2); // Inicio (Derecha)
	    addPointToListMovement(9, 7, movementEnemy2);  // Destino (Izquierda)
	    putEnemy(movementEnemy2);

	    // Enemigo 3: Fila 10, empieza a la izquierda (col 9) y va a la derecha (col 25)
	    List<Point> movementEnemy3 = new LinkedList<>();
	    addPointToListMovement(10, 7, movementEnemy3);  // Inicio (Izquierda)
	    addPointToListMovement(10, 24, movementEnemy3); // Destino (Derecha)
	    putEnemy(movementEnemy3);

	    // Enemigo 4: Fila 11, empieza a la derecha (col 25) y va a la izquierda (col 9)
	    List<Point> movementEnemy4 = new LinkedList<>();
	    addPointToListMovement(11, 24, movementEnemy4); // Inicio (Derecha)
	    addPointToListMovement(11, 7, movementEnemy4);  // Destino (Izquierda)
	    putEnemy(movementEnemy4);
	    
	    zones.add(new InitialZone(new Figure(new ArrayList<Point>(List.of(
	    		new Point(2*DimensionGame.TILESIZE, 6*DimensionGame.TILESIZE),
		    	new Point(5*DimensionGame.TILESIZE, 6*DimensionGame.TILESIZE),
		    	new Point(2*DimensionGame.TILESIZE,13*DimensionGame.TILESIZE),
		    	new Point(5*DimensionGame.TILESIZE,13*DimensionGame.TILESIZE))))));
	    
	    zones.add(new GoalZone(new Figure(new ArrayList<Point>(List.of(
	    		new Point(26*DimensionGame.TILESIZE, 6*DimensionGame.TILESIZE),
		    	new Point(29*DimensionGame.TILESIZE, 6*DimensionGame.TILESIZE),
		    	new Point(26*DimensionGame.TILESIZE,13*DimensionGame.TILESIZE),
		    	new Point(29*DimensionGame.TILESIZE,13*DimensionGame.TILESIZE))))));
	}
	
	private void addPointToListMovement(int row, int col, List<Point> movement) {
	    movement.add(new Point(col * DimensionGame.TILESIZEWIDTH, row * DimensionGame.TILESIZEHEIGHT ));
	}
	
	private void putEnemy(List<Point> movement) {
	    // Al sumarle 1000, los enemigos se guardan lejos de los índices de las baldosas
	    // Esto hace que se dibujen SIEMPRE por encima del suelo y no queden invisibles
	    int unIdAlto = elements.size() + 1000;
	    elements.put(unIdAlto, new Basic(movement, cCheker, this));
	}
	
	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List<Solid> getSolidElements() {
		List<Solid> solids = new ArrayList<>();
		for(Element element : elements.values()) {
			if(element instanceof Solid) {
				solids.add((Solid) element);
			}
		}
		return solids;
	}
	
	@Override 
	public List<Interactable> getInteractableElements(){
		List<Interactable> interactable = new ArrayList<>();
		for(Element element : elements.values()) {
			if(element instanceof Interactable) {
				interactable.add((Interactable)element);
			}
		}
		return interactable;
	}
	
	/**
	 * Put at the correct position the players of level1
	 * @param pys pys are the list of players that are at the level
	 */
	@Override
	public void spawnPlayers(List<Player> pys) {	
		Zone zone = getInitialZone();
	    for (Player player : players) {
	        player.setPosition(zone.getSpawnX(), zone.getSpawnY());
	        player.setRespawnPoint(zone.getSpawnX(), zone.getSpawnY());
	    }
	}
	
	public Zone getInitialZone() {
		for(Zone zone : zones) {
			if(zone instanceof InitialZone) {
				return zone;
			}
		}
		return null;
	}
}
