package domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Level1 extends Level {
	
	public Level1(CollisionChecker cCheker) {
	    map = new Map(1);
	    registerTiles();        // 1. Las baldosas toman los índices bajos (0, 1, 2...)
	    this.cCheker = cCheker; // 2. Asignas el checker antes de crear los enemigos
	    initialize();           // 3. Creas los enemigos al final
	}


	@Override
	public void initialize() { // Ayudado a poner por Gemini IA 2026, supervisado 
	    // Enemigo 1: Fila 8, empieza a la izquierda (col 9) y va a la derecha (col 25)
	    List<Point> movementEnemy1 = new LinkedList<>();
	    addPointToList(8, 7, movementEnemy1);  // Inicio (Izquierda)
	    addPointToList(8, 24, movementEnemy1); // Destino (Derecha)
	    putEnemy(movementEnemy1, "basic");

	    // Enemigo 2: Fila 9, empieza a la derecha (col 25) y va a la izquierda (col 9)
	    List<Point> movementEnemy2 = new LinkedList<>();
	    addPointToList(9, 24, movementEnemy2); // Inicio (Derecha)
	    addPointToList(9, 7, movementEnemy2);  // Destino (Izquierda)
	    putEnemy(movementEnemy2, "basic");

	    // Enemigo 3: Fila 10, empieza a la izquierda (col 9) y va a la derecha (col 25)
	    List<Point> movementEnemy3 = new LinkedList<>();
	    addPointToList(10, 7, movementEnemy3);  // Inicio (Izquierda)
	    addPointToList(10, 24, movementEnemy3); // Destino (Derecha)
	    putEnemy(movementEnemy3, "basic");

	    // Enemigo 4: Fila 11, empieza a la derecha (col 25) y va a la izquierda (col 9)
	    List<Point> movementEnemy4 = new LinkedList<>();
	    addPointToList(11, 24, movementEnemy4); // Inicio (Derecha)
	    addPointToList(11, 7, movementEnemy4);  // Destino (Izquierda)
	    putEnemy(movementEnemy4, "basic");
	    
	    addPointToList(11, 24, movementEnemy4); // Inicio (Derecha)
	    addPointToList(11, 7, movementEnemy4);  // Destino (Izquierda)
	    putEnemy(movementEnemy4, "basic");

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
	
	
	@Override
	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
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
